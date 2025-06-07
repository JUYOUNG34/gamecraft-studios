'use client'

import { useState, useCallback, useEffect } from 'react'
import { useQuery } from '@tanstack/react-query'
import { toast } from 'sonner'

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080'

interface JobPosition {
    id: number
    slug: string
    title: string
    company: string
    companyLogo?: string
    location: string
    jobType: string
    jobTypeDescription: string
    experienceLevel: string
    experienceLevelDescription: string
    salaryRange?: string
    remoteWorkAvailable: boolean
    requiredSkills: string[]
    preferredSkills: string[]
    viewCount: number
    applicationCount: number
    createdAt: string
    applicationDeadline?: string
}

interface JobPositionDetail extends JobPosition {
    companyDescription?: string
    description: string
    requirements: string
    preferredQualifications?: string
    benefits: string[]
    contactEmail?: string
    contactPerson?: string
    isActive: boolean
}

interface PositionsResponse {
    success: boolean
    jobs: JobPosition[]
    pagination: {
        currentPage: number
        totalPages: number
        totalElements: number
        size: number
        hasNext: boolean
        hasPrevious: boolean
    }
    message?: string
}

interface PositionDetailResponse {
    success: boolean
    job: JobPositionDetail
    message?: string
}

interface FilterOptionsResponse {
    success: boolean
    companies: Array<{ name: string; count: number }>
    locations: Array<{ name: string; count: number }>
    experienceLevels: Record<string, string>
    jobTypes: Record<string, string>
    message?: string
}

interface SearchParams {
    page?: number
    size?: number
    search?: string
    company?: string
    location?: string
    experienceLevel?: string
    jobType?: string
    skill?: string
    sort?: string
}

class PositionsApi {
    private baseURL: string

    constructor(baseURL: string = API_BASE_URL) {
        this.baseURL = baseURL
    }

    private async request<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
        try {
            const url = `${this.baseURL}${endpoint}`
            const response = await fetch(url, {
                ...options,
                headers: {
                    'Content-Type': 'application/json',
                    ...options.headers,
                },
                credentials: 'include',
            })

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`)
            }

            const data = await response.json()
            return data
        } catch (error) {
            console.error('API request failed:', error)
            throw error
        }
    }

    async getPositions(params: SearchParams = {}): Promise<PositionsResponse> {
        const searchParams = new URLSearchParams()

        Object.entries(params).forEach(([key, value]) => {
            if (value !== undefined && value !== null && value !== '') {
                searchParams.append(key, value.toString())
            }
        })

        const query = searchParams.toString()
        return this.request<PositionsResponse>(`/positions${query ? `?${query}` : ''}`)
    }

    async getPositionDetail(id: number): Promise<PositionDetailResponse> {
        return this.request<PositionDetailResponse>(`/positions/${id}`)
    }

    async getPositionBySlug(slug: string): Promise<PositionDetailResponse> {
        return this.request<PositionDetailResponse>(`/positions/slug/${slug}`)
    }

    async getFilterOptions(): Promise<FilterOptionsResponse> {
        return this.request<FilterOptionsResponse>('/positions/filter-options')
    }

    async getPopularPositions(limit: number = 6): Promise<{ success: boolean; jobs: JobPosition[] }> {
        return this.request<{ success: boolean; jobs: JobPosition[] }>(`/positions/popular?limit=${limit}`)
    }

    async getRecentPositions(days: number = 7): Promise<{ success: boolean; jobs: JobPosition[] }> {
        return this.request<{ success: boolean; jobs: JobPosition[] }>(`/positions/recent?days=${days}`)
    }

    async getDeadlineSoonPositions(days: number = 7): Promise<{ success: boolean; jobs: JobPosition[] }> {
        return this.request<{ success: boolean; jobs: JobPosition[] }>(`/positions/deadline-soon?days=${days}`)
    }
}

const positionsApi = new PositionsApi()

export function usePositions() {
    const [searchParams, setSearchParams] = useState<SearchParams>({
        page: 0,
        size: 12,
        sort: 'latest'
    })

    const [positions, setPositions] = useState<JobPosition[]>([])
    const [totalPages, setTotalPages] = useState(0)
    const [totalElements, setTotalElements] = useState(0)
    const [isLoading, setIsLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)

    // 필터 옵션 조회
    const {
        data: filterOptionsData,
        isLoading: isLoadingFilterOptions,
    } = useQuery({
        queryKey: ['positions', 'filter-options'],
        queryFn: async () => {
            const response = await positionsApi.getFilterOptions()
            return response.success ? response : null
        },
        staleTime: 5 * 60 * 1000, // 5분간 캐시
    })

    const fetchPositions = useCallback(async (params: SearchParams = {}) => {
        setIsLoading(true)
        setError(null)

        try {
            const mergedParams = { ...searchParams, ...params }
            setSearchParams(mergedParams)

            const response = await positionsApi.getPositions(mergedParams)

            if (response.success) {
                setPositions(response.jobs)
                setTotalPages(response.pagination.totalPages)
                setTotalElements(response.pagination.totalElements)
            } else {
                setError(response.message || '채용공고를 불러오는데 실패했습니다')
            }
        } catch (error) {
            console.error('채용공고 조회 오류:', error)
            setError('네트워크 오류가 발생했습니다')
        } finally {
            setIsLoading(false)
        }
    }, [searchParams])

    // 초기 데이터 로드
    useEffect(() => {
        fetchPositions()
    }, [])

    return {
        positions,
        totalPages,
        totalElements,
        isLoading,
        error,
        filterOptions: filterOptionsData || undefined,
        isLoadingFilterOptions,
        fetchPositions,
        searchParams,
    }
}

// 개별 채용공고 상세 조회 훅
export function usePositionDetail(id: number | null) {
    const {
        data: positionData,
        isLoading,
        error,
        refetch,
    } = useQuery({
        queryKey: ['positions', 'detail', id],
        queryFn: async () => {
            if (!id) return null
            const response = await positionsApi.getPositionDetail(id)
            return response.success ? response.job : null
        },
        enabled: !!id,
        retry: 1,
    })

    return {
        position: positionData,
        isLoading,
        error,
        refetch,
    }
}

// 슬러그로 채용공고 조회 훅
export function usePositionBySlug(slug: string | null) {
    const {
        data: positionData,
        isLoading,
        error,
        refetch,
    } = useQuery({
        queryKey: ['positions', 'slug', slug],
        queryFn: async () => {
            if (!slug) return null
            const response = await positionsApi.getPositionBySlug(slug)
            return response.success ? response.job : null
        },
        enabled: !!slug,
        retry: 1,
    })

    return {
        position: positionData,
        isLoading,
        error,
        refetch,
    }
}

// 인기 채용공고 훅
export function usePopularPositions(limit: number = 6) {
    const {
        data: popularData,
        isLoading,
        error,
    } = useQuery({
        queryKey: ['positions', 'popular', limit],
        queryFn: async () => {
            const response = await positionsApi.getPopularPositions(limit)
            return response.success ? response.jobs : []
        },
        staleTime: 2 * 60 * 1000, // 2분간 캐시
    })

    return {
        popularPositions: popularData || [],
        isLoading,
        error,
    }
}

// 최신 채용공고 훅
export function useRecentPositions(days: number = 7) {
    const {
        data: recentData,
        isLoading,
        error,
    } = useQuery({
        queryKey: ['positions', 'recent', days],
        queryFn: async () => {
            const response = await positionsApi.getRecentPositions(days)
            return response.success ? response.jobs : []
        },
        staleTime: 2 * 60 * 1000, // 2분간 캐시
    })

    return {
        recentPositions: recentData || [],
        isLoading,
        error,
    }
}

// 마감임박 채용공고 훅
export function useDeadlineSoonPositions(days: number = 7) {
    const {
        data: deadlineData,
        isLoading,
        error,
    } = useQuery({
        queryKey: ['positions', 'deadline-soon', days],
        queryFn: async () => {
            const response = await positionsApi.getDeadlineSoonPositions(days)
            return response.success ? response.jobs : []
        },
        staleTime: 1 * 60 * 1000, // 1분간 캐시 (마감임박이므로 자주 업데이트)
    })

    return {
        deadlineSoonPositions: deadlineData || [],
        isLoading,
        error,
    }
}

// 채용공고 북마크 관리 훅 (메모리 저장)
export function useBookmarks() {
    const [bookmarks, setBookmarks] = useState<Set<number>>(new Set())

    const addBookmark = useCallback((jobId: number) => {
        setBookmarks(prev => new Set([...prev, jobId]))
        toast.success('북마크에 추가되었습니다!')
    }, [])

    const removeBookmark = useCallback((jobId: number) => {
        setBookmarks(prev => {
            const newBookmarks = new Set(prev)
            newBookmarks.delete(jobId)
            return newBookmarks
        })
        toast.success('북마크에서 제거되었습니다!')
    }, [])

    const toggleBookmark = useCallback((jobId: number) => {
        if (bookmarks.has(jobId)) {
            removeBookmark(jobId)
        } else {
            addBookmark(jobId)
        }
    }, [bookmarks, addBookmark, removeBookmark])

    const isBookmarked = useCallback((jobId: number) => {
        return bookmarks.has(jobId)
    }, [bookmarks])

    return {
        bookmarks: Array.from(bookmarks),
        addBookmark,
        removeBookmark,
        toggleBookmark,
        isBookmarked,
    }
}