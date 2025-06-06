// frontend/src/lib/api.ts
import {
    ApiResponse,
    UserInfoResponse,
    ApplicationListResponse,
    CreateApplicationResponse,
    FormInfoResponse,
    AdminDashboardResponse,
    AdminApplicationListResponse
} from '@/types/api'

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080'

export interface CreateApplicationRequest {
    company: string
    position: string
    experienceLevel: 'JUNIOR' | 'MIDDLE' | 'SENIOR' | 'LEAD'
    jobType: 'FULL_TIME' | 'CONTRACT' | 'FREELANCE' | 'INTERN'
    skills: string[]
    coverLetter: string
    expectedSalary?: string
    availableStartDate?: string
    workLocation?: string
}

class ApiClient {
    private baseURL: string

    constructor(baseURL: string = API_BASE_URL) {
        this.baseURL = baseURL
    }

    private async request<T>(
        endpoint: string,
        options: RequestInit = {}
    ): Promise<T> {
        try {
            const url = `${this.baseURL}${endpoint}`

            const defaultHeaders = {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
            }

            const config: RequestInit = {
                ...options,
                headers: {
                    ...defaultHeaders,
                    ...options.headers,
                },
                credentials: 'include', // 쿠키 포함
            }

            const response = await fetch(url, config)
            const data = await response.json()

            if (!response.ok) {
                throw new Error(data.message || `HTTP error! status: ${response.status}`)
            }

            return data
        } catch (error) {
            console.error('API request failed:', error)
            throw error
        }
    }

    // 인증 관련 API
    auth = {
        getKakaoLoginUrl: () => `${this.baseURL}/oauth2/authorization/kakao`,

        getUserInfo: async (): Promise<UserInfoResponse> => {
            return this.request<UserInfoResponse>('/auth/kakao/user-info')
        },

        logout: async (): Promise<ApiResponse> => {
            return this.request<ApiResponse>('/auth/kakao/logout', {
                method: 'POST',
            })
        },
    }

    // 지원서 관련 API
    applications = {
        getFormInfo: async (): Promise<FormInfoResponse> => {
            return this.request<FormInfoResponse>('/application/form-info')
        },

        create: async (data: CreateApplicationRequest): Promise<CreateApplicationResponse> => {
            return this.request<CreateApplicationResponse>('/application/create', {
                method: 'POST',
                body: JSON.stringify(data),
            })
        },

        getMyList: async (): Promise<ApplicationListResponse> => {
            return this.request<ApplicationListResponse>('/application/my-list')
        },
    }

    // 관리자 관련 API
    admin = {
        promoteToAdmin: async (): Promise<ApiResponse> => {
            return this.request<ApiResponse>('/admin/promote-to-admin', {
                method: 'POST',
            })
        },

        getDashboard: async (): Promise<AdminDashboardResponse> => {
            return this.request<AdminDashboardResponse>('/admin/dashboard')
        },

        getAllApplications: async (status?: string, company?: string): Promise<AdminApplicationListResponse> => {
            const params = new URLSearchParams()
            if (status) params.append('status', status)
            if (company) params.append('company', company)

            const query = params.toString()
            return this.request<AdminApplicationListResponse>(`/admin/applications${query ? `?${query}` : ''}`)
        },

        getApplicationDetail: async (id: number): Promise<ApiResponse> => {
            return this.request<ApiResponse>(`/admin/applications/${id}`)
        },

        updateApplicationStatus: async (id: number, status: string, adminNotes?: string): Promise<ApiResponse> => {
            return this.request<ApiResponse>(`/admin/applications/${id}/status`, {
                method: 'PUT',
                body: JSON.stringify({ status, adminNotes }),
            })
        },
    }

    // 일반 API
    general = {
        getHello: async (): Promise<ApiResponse> => {
            return this.request<ApiResponse>('/hello')
        },

        getApiStatus: async (): Promise<ApiResponse> => {
            return this.request<ApiResponse>('/api-status')
        },
    }
}

export const apiClient = new ApiClient()
export default apiClient