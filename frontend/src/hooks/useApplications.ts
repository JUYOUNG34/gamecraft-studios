// frontend/src/hooks/useApplications.ts
'use client'

import { useState, useCallback } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { apiClient, CreateApplicationRequest } from '@/lib/api'
import {
    ApplicationListResponse,
    FormInfoResponse,
    AdminDashboardResponse,
    AdminApplicationListResponse
} from '@/types/api'
import { toast } from 'sonner'

export function useApplications() {
    const queryClient = useQueryClient()

    // 내 지원서 목록 조회
    const {
        data: myApplicationsData,
        isLoading: isLoadingApplications,
        error: applicationsError,
    } = useQuery({
        queryKey: ['applications', 'my'],
        queryFn: async (): Promise<ApplicationListResponse | null> => {
            const response = await apiClient.applications.getMyList()
            return response.success ? response : null
        },
        retry: 1,
    })

    // 지원서 폼 정보 조회
    const {
        data: formInfoData,
        isLoading: isLoadingFormInfo,
    } = useQuery({
        queryKey: ['applications', 'form-info'],
        queryFn: async (): Promise<FormInfoResponse | null> => {
            const response = await apiClient.applications.getFormInfo()
            return response.success ? response : null
        },
        staleTime: 5 * 60 * 1000, // 5분간 캐시
    })

    // 지원서 생성 뮤테이션
    const createApplicationMutation = useMutation({
        mutationFn: async (data: CreateApplicationRequest) => {
            return apiClient.applications.create(data)
        },
        onSuccess: (response) => {
            if (response.success) {
                toast.success(response.message)
                // 내 지원서 목록 다시 가져오기
                queryClient.invalidateQueries({ queryKey: ['applications', 'my'] })
            } else {
                toast.error(response.message)
            }
        },
        onError: (error: Error) => {
            toast.error(error.message || '지원서 제출에 실패했습니다')
        },
    })

    const createApplication = useCallback(
        (data: CreateApplicationRequest) => {
            return createApplicationMutation.mutateAsync(data)
        },
        [createApplicationMutation]
    )

    return {
        // 데이터
        myApplications: myApplicationsData?.applications || [],
        totalApplications: myApplicationsData?.totalCount || 0,
        formInfo: formInfoData,

        // 로딩 상태
        isLoadingApplications,
        isLoadingFormInfo,
        isCreatingApplication: createApplicationMutation.isPending,

        // 에러
        applicationsError,

        // 메서드
        createApplication,
    }
}

// 관리자용 지원서 관리 훅
export function useAdminApplications() {
    const queryClient = useQueryClient()
    const [statusFilter, setStatusFilter] = useState<string>('ALL')
    const [companyFilter, setCompanyFilter] = useState<string>('')

    // 모든 지원서 조회
    const {
        data: allApplicationsData,
        isLoading: isLoadingApplications,
        error: applicationsError,
    } = useQuery({
        queryKey: ['admin', 'applications', statusFilter, companyFilter],
        queryFn: async (): Promise<AdminApplicationListResponse | null> => {
            const response = await apiClient.admin.getAllApplications(
                statusFilter === 'ALL' ? undefined : statusFilter,
                companyFilter || undefined
            )
            return response.success ? response : null
        },
        retry: 1,
    })

    // 관리자 대시보드 조회
    const {
        data: dashboardData,
        isLoading: isLoadingDashboard,
    } = useQuery({
        queryKey: ['admin', 'dashboard'],
        queryFn: async (): Promise<AdminDashboardResponse | null> => {
            const response = await apiClient.admin.getDashboard()
            return response.success ? response : null
        },
        staleTime: 2 * 60 * 1000, // 2분간 캐시
    })

    // 지원서 상태 업데이트 뮤테이션
    const updateStatusMutation = useMutation({
        mutationFn: async ({
                               id,
                               status,
                               adminNotes,
                           }: {
            id: number
            status: string
            adminNotes?: string
        }) => {
            return apiClient.admin.updateApplicationStatus(id, status, adminNotes)
        },
        onSuccess: (response) => {
            if (response.success) {
                toast.success(response.message)
                // 지원서 목록과 대시보드 다시 가져오기
                queryClient.invalidateQueries({ queryKey: ['admin', 'applications'] })
                queryClient.invalidateQueries({ queryKey: ['admin', 'dashboard'] })
            } else {
                toast.error(response.message)
            }
        },
        onError: (error: Error) => {
            toast.error(error.message || '상태 업데이트에 실패했습니다')
        },
    })

    const updateApplicationStatus = useCallback(
        (id: number, status: string, adminNotes?: string) => {
            return updateStatusMutation.mutateAsync({ id, status, adminNotes })
        },
        [updateStatusMutation]
    )

    return {
        // 데이터
        applications: allApplicationsData?.applications || [],
        totalApplications: allApplicationsData?.totalCount || 0,
        dashboard: dashboardData?.statistics,

        // 필터
        statusFilter,
        setStatusFilter,
        companyFilter,
        setCompanyFilter,

        // 로딩 상태
        isLoadingApplications,
        isLoadingDashboard,
        isUpdatingStatus: updateStatusMutation.isPending,

        // 에러
        applicationsError,

        // 메서드
        updateApplicationStatus,
    }
}