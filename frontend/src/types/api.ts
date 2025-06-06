// frontend/src/types/api.ts
export interface ApiResponse<T = any> {
    success: boolean
    message: string
    data?: T
    error?: string
}

// 사용자 정보 응답
export interface UserInfoResponse extends ApiResponse {
    user?: {
        id: number
        kakaoId: string
        name: string
        email: string
        profileImage?: string
        role: 'USER' | 'ADMIN'
        status: string
        createdAt: string
    }
    userStatus?: string
    nextSteps?: Record<string, string>
}

// 지원서 목록 응답
export interface ApplicationListResponse extends ApiResponse {
    totalCount: number
    applications: ApplicationSummary[]
}

// 지원서 생성 응답
export interface CreateApplicationResponse extends ApiResponse {
    applicationId: number
    company: string
    position: string
    status: string
}

// 지원서 폼 정보 응답
export interface FormInfoResponse extends ApiResponse {
    companies: string[]
    positions: string[]
    experienceLevels: Record<string, string>
    jobTypes: Record<string, string>
}

// 관리자 대시보드 응답
export interface AdminDashboardResponse extends ApiResponse {
    statistics: {
        totalApplications: number
        totalUsers: number
        statusStats: Record<string, number>
        companyStats: Record<string, number>
        recentApplicationsCount: number
    }
    trends?: {
        daily: Record<string, number>
        weekly: Record<string, number>
        monthly: Record<string, number>
    }
    experienceAnalysis?: any
    companyAnalysis?: any
    statusAnalysis?: any
    positionAnalysis?: any
}

// 관리자 지원서 목록 응답
export interface AdminApplicationListResponse extends ApiResponse {
    totalCount: number
    applications: AdminApplicationItem[]
}

export interface AdminApplicationItem {
    id: number
    applicantName: string
    applicantEmail: string
    company: string
    position: string
    status: string
    statusDescription: string
    experienceLevel: string
    jobType: string
    submittedAt: string
    updatedAt: string
}

export interface ApplicationSummary {
    id: string
    company: string
    position: string
    status: string
    submittedAt: string
}