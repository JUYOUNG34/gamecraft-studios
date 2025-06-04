'use client'
import { useState } from 'react'
import Link from 'next/link'
import { useRouter } from 'next/navigation'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Separator } from '@/components/ui/separator'
import { Alert, AlertDescription } from '@/components/ui/alert'
import { Gamepad2, MessageCircle, AlertCircle } from 'lucide-react'
import { useAuthStore } from '@/store/authStore'

export default function LoginPage() {
    const [isLoading, setIsLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)
    const router = useRouter()
    const { login } = useAuthStore()

    // 카카오 로그인 URL 생성
    const getKakaoLoginUrl = () => {
        const kakaoClientId = process.env.NEXT_PUBLIC_KAKAO_CLIENT_ID
        const redirectUri = `${window.location.origin}/auth/callback`
        const scope = 'profile_nickname,profile_image,account_email'

        return `https://kauth.kakao.com/oauth/authorize?client_id=${kakaoClientId}&redirect_uri=${redirectUri}&response_type=code&scope=${scope}`
    }

    const handleKakaoLogin = () => {
        if (!process.env.NEXT_PUBLIC_KAKAO_CLIENT_ID) {
            setError('카카오 로그인 설정이 필요합니다.')
            return
        }

        setIsLoading(true)
        setError(null)

        try {
            window.location.href = getKakaoLoginUrl()
        } catch (error) {
            console.error('카카오 로그인 오류:', error)
            setError('로그인 중 오류가 발생했습니다.')
            setIsLoading(false)
        }
    }

    // 데모용 로그인 (개발 중에만 사용)
    const handleDemoLogin = () => {
        setIsLoading(true)

        // 임시 사용자 데이터로 로그인
        setTimeout(() => {
            const demoUser = {
                id: 'demo-user-1',
                email: 'demo@gamecraft.com',
                nickname: '데모유저',
                profileImage: '',
                role: 'USER' as const
            }

            login(demoUser, 'demo-token')
            router.push('/dashboard')
            setIsLoading(false)
        }, 1000)
    }

    return (
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-primary/5 via-purple-50/30 to-background p-4">
            <div className="w-full max-w-md space-y-8">
                {/* 로고 및 헤더 */}
                <div className="text-center">
                    <Link href="/" className="inline-flex items-center space-x-2 mb-6">
                        <Gamepad2 className="h-10 w-10 text-primary" />
                        <span className="text-3xl font-bold bg-gradient-to-r from-primary to-purple-600 bg-clip-text text-transparent">
              GameCraft
            </span>
                    </Link>
                    <h1 className="text-2xl font-bold text-gray-900">
                        게임 개발자 커리어의 시작
                    </h1>
                    <p className="text-gray-600 mt-2">
                        카카오 계정으로 간편하게 로그인하세요
                    </p>
                </div>

                {/* 로그인 카드 */}
                <Card className="border-2">
                    <CardHeader className="space-y-1">
                        <CardTitle className="text-xl text-center">로그인</CardTitle>
                        <CardDescription className="text-center">
                            소셜 계정으로 빠르게 시작하세요
                        </CardDescription>
                    </CardHeader>

                    <CardContent className="space-y-4">
                        {/* 에러 메시지 */}
                        {error && (
                            <Alert variant="destructive">
                                <AlertCircle className="h-4 w-4" />
                                <AlertDescription>{error}</AlertDescription>
                            </Alert>
                        )}

                        {/* 카카오 로그인 버튼 */}
                        <Button
                            onClick={handleKakaoLogin}
                            disabled={isLoading}
                            className="w-full bg-[#FEE500] hover:bg-[#FEE500]/90 text-black border-0 h-12"
                            size="lg"
                        >
                            {isLoading ? (
                                <div className="flex items-center">
                                    <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-black mr-2"></div>
                                    로그인 중...
                                </div>
                            ) : (
                                <>
                                    <MessageCircle className="mr-2 h-5 w-5" />
                                    카카오로 시작하기
                                </>
                            )}
                        </Button>

                        <div className="relative">
                            <div className="absolute inset-0 flex items-center">
                                <Separator className="w-full" />
                            </div>
                            <div className="relative flex justify-center text-xs uppercase">
                <span className="bg-background px-2 text-muted-foreground">
                  또는
                </span>
                            </div>
                        </div>

                        {/* 데모 로그인 버튼 (개발용) */}
                        <Button
                            onClick={handleDemoLogin}
                            disabled={isLoading}
                            variant="outline"
                            className="w-full h-12"
                            size="lg"
                        >
                            {isLoading ? (
                                <div className="flex items-center">
                                    <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-gray-900 mr-2"></div>
                                    로그인 중...
                                </div>
                            ) : (
                                '데모 계정으로 체험하기'
                            )}
                        </Button>

                        {/* 약관 동의 */}
                        <p className="text-xs text-center text-gray-500 leading-relaxed">
                            로그인하면{' '}
                            <Link href="/terms" className="underline hover:text-primary">
                                서비스 약관
                            </Link>
                            {' '}및{' '}
                            <Link href="/privacy" className="underline hover:text-primary">
                                개인정보 처리방침
                            </Link>
                            에 동의하는 것으로 간주됩니다.
                        </p>
                    </CardContent>
                </Card>

                {/* 추가 링크 */}
                <div className="text-center space-y-2">
                    <p className="text-sm text-gray-600">
                        처음 방문하시나요?{' '}
                        <Link href="/about" className="text-primary hover:underline font-medium">
                            GameCraft Studios 알아보기
                        </Link>
                    </p>
                    <p className="text-sm text-gray-600">
                        기업 담당자이신가요?{' '}
                        <Link href="/enterprise" className="text-primary hover:underline font-medium">
                            기업 서비스 문의
                        </Link>
                    </p>
                </div>
            </div>
        </div>
    )
}