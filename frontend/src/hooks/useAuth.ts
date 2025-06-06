// frontend/src/hooks/useAuth.ts
'use client'

import { useEffect } from 'react'
import { useRouter } from 'next/navigation'
import { useAuthStore } from '@/store/authStore'
import { apiClient } from '@/lib/api'
import { toast } from 'sonner'

export function useAuth() {
    const router = useRouter()
    const { user, isAuthenticated, login, logout } = useAuthStore()

    // 카카오 로그인 처리
    const handleKakaoLogin = () => {
        window.location.href = apiClient.auth.getKakaoLoginUrl()
    }

    // 로그아웃 처리
    const handleLogout = async () => {
        try {
            // 백엔드 로그아웃 API 호출
            await apiClient.auth.logout()

            // 클라이언트 상태 정리
            logout()

            // 성공 메시지 및 리다이렉트
            toast.success('로그아웃되었습니다')
            router.push('/')
        } catch (error) {
            console.error('로그아웃 오류:', error)

            // 서버 로그아웃 실패해도 클라이언트에서는 로그아웃 처리
            logout()
            toast.info('로그아웃되었습니다')
            router.push('/')
        }
    }

    // 사용자 정보 확인 및 동기화
    const checkAuthStatus = async () => {
        try {
            const response = await apiClient.auth.getUserInfo()

            if (response.success && response.user) {
                const userData = {
                    id: response.user.id.toString(),
                    email: response.user.email,
                    nickname: response.user.name,
                    profileImage: response.user.profileImage,
                    role: response.user.role as 'USER' | 'ADMIN'
                }

                login(userData, 'server-session-token')
                return true
            }
        } catch (error) {
            console.error('인증 상태 확인 실패:', error)
            if (isAuthenticated) {
                logout()
            }
        }
        return false
    }

    // 관리자 권한 승격
    const promoteToAdmin = async () => {
        try {
            const response = await apiClient.admin.promoteToAdmin()

            if (response.success) {
                toast.success(response.message)
                // 사용자 정보 다시 가져오기
                await checkAuthStatus()
                return true
            } else {
                toast.error(response.message)
                return false
            }
        } catch (error) {
            console.error('관리자 승격 오류:', error)
            toast.error('관리자 승격에 실패했습니다')
            return false
        }
    }

    // 로그인 필요 페이지 접근 제어
    const requireAuth = () => {
        if (!isAuthenticated) {
            toast.error('로그인이 필요합니다')
            router.push('/auth/login')
            return false
        }
        return true
    }

    // 관리자 권한 체크
    const requireAdmin = () => {
        if (!isAuthenticated) {
            toast.error('로그인이 필요합니다')
            router.push('/auth/login')
            return false
        }

        if (user?.role !== 'ADMIN') {
            toast.error('관리자 권한이 필요합니다')
            router.push('/dashboard')
            return false
        }

        return true
    }

    return {
        user,
        isAuthenticated,
        handleKakaoLogin,
        handleLogout,
        checkAuthStatus,
        promoteToAdmin,
        requireAuth,
        requireAdmin,
        // 추가: 로그아웃 상태 관리
        isLoggingOut: false, // TODO: 로그아웃 로딩 상태 관리 시 사용
    }
}

// 페이지 컴포넌트에서 사용할 인증 체크 훅
export function useRequireAuth() {
    const { isAuthenticated } = useAuthStore()
    const router = useRouter()

    useEffect(() => {
        if (!isAuthenticated) {
            router.push('/auth/login')
        }
    }, [isAuthenticated, router])

    return isAuthenticated
}

// 관리자 페이지용 훅
export function useRequireAdmin() {
    const { user, isAuthenticated } = useAuthStore()
    const router = useRouter()

    useEffect(() => {
        if (!isAuthenticated) {
            router.push('/auth/login')
            return
        }

        if (user?.role !== 'ADMIN') {
            router.push('/dashboard')
            return
        }
    }, [isAuthenticated, user?.role, router])

    return isAuthenticated && user?.role === 'ADMIN'
}