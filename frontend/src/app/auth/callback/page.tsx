// frontend/src/app/auth/callback/page.tsx
'use client'

import { useEffect, useState } from 'react'
import { useRouter, useSearchParams } from 'next/navigation'
import { Card, CardContent } from '@/components/ui/card'
import { Alert, AlertDescription } from '@/components/ui/alert'
import { Button } from '@/components/ui/button'
import { Gamepad2, CheckCircle, AlertCircle, Loader2 } from 'lucide-react'
import { useAuth } from '@/hooks/useAuth'
import Link from 'next/link'

export default function AuthCallbackPage() {
    const [status, setStatus] = useState<'loading' | 'success' | 'error'>('loading')
    const [message, setMessage] = useState('')
    const router = useRouter()
    const searchParams = useSearchParams()
    const { checkAuthStatus } = useAuth()

    useEffect(() => {
        const handleCallback = async () => {
            try {
                const success = searchParams.get('success')
                const error = searchParams.get('error')
                const errorDescription = searchParams.get('error_description')

                // 에러가 있는 경우
                if (error) {
                    setStatus('error')
                    const decodedError = errorDescription ? decodeURIComponent(errorDescription) : '로그인 중 오류가 발생했습니다.'
                    setMessage(decodedError)
                    return
                }

                // 성공 파라미터가 있는 경우
                if (success === 'true') {
                    // 잠시 대기 (백엔드에서 카카오 토큰 교환 및 사용자 생성 처리 시간)
                    await new Promise(resolve => setTimeout(resolve, 1500))

                    // 사용자 정보 확인
                    const authSuccess = await checkAuthStatus()

                    if (authSuccess) {
                        setStatus('success')
                        setMessage('로그인이 완료되었습니다!')

                        // 2초 후 대시보드로 이동
                        setTimeout(() => {
                            router.push('/dashboard')
                        }, 2000)
                    } else {
                        setStatus('error')
                        setMessage('사용자 정보를 가져오는데 실패했습니다.')
                    }
                } else {
                    // success 파라미터가 없는 경우
                    setStatus('error')
                    setMessage('잘못된 접근입니다.')
                }

            } catch (error) {
                console.error('콜백 처리 오류:', error)
                setStatus('error')
                setMessage('로그인 처리 중 오류가 발생했습니다.')
            }
        }

        // 컴포넌트 마운트 후 잠시 대기
        const timer = setTimeout(handleCallback, 500)

        return () => clearTimeout(timer)
    }, [searchParams, checkAuthStatus, router])

    return (
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-primary/5 via-purple-50/30 to-background p-4">
            <div className="w-full max-w-md space-y-8">
                {/* 로고 */}
                <div className="text-center">
                    <Link href="/" className="inline-flex items-center space-x-2 mb-6">
                        <Gamepad2 className="h-10 w-10 text-primary" />
                        <span className="text-3xl font-bold bg-gradient-to-r from-primary to-purple-600 bg-clip-text text-transparent">
              GameCraft
            </span>
                    </Link>
                </div>

                {/* 상태 카드 */}
                <Card className="border-2">
                    <CardContent className="p-8 text-center space-y-6">
                        {status === 'loading' && (
                            <>
                                <Loader2 className="h-12 w-12 animate-spin mx-auto text-primary" />
                                <div>
                                    <h2 className="text-xl font-semibold mb-2">로그인 처리 중...</h2>
                                    <p className="text-muted-foreground">
                                        카카오 로그인을 완료하고 있습니다.<br />
                                        잠시만 기다려주세요.
                                    </p>
                                </div>
                            </>
                        )}

                        {status === 'success' && (
                            <>
                                <CheckCircle className="h-12 w-12 mx-auto text-green-600" />
                                <div>
                                    <h2 className="text-xl font-semibold mb-2 text-green-700">로그인 성공!</h2>
                                    <p className="text-muted-foreground">
                                        {message}<br />
                                        대시보드로 이동합니다...
                                    </p>
                                </div>
                            </>
                        )}

                        {status === 'error' && (
                            <>
                                <AlertCircle className="h-12 w-12 mx-auto text-red-600" />
                                <div>
                                    <h2 className="text-xl font-semibold mb-2 text-red-700">로그인 실패</h2>
                                    <Alert variant="destructive" className="mb-4 text-left">
                                        <AlertDescription>{message}</AlertDescription>
                                    </Alert>
                                    <div className="space-y-2">
                                        <Button asChild className="w-full">
                                            <Link href="/auth/login">다시 로그인하기</Link>
                                        </Button>
                                        <Button asChild variant="outline" className="w-full">
                                            <Link href="/">홈으로 돌아가기</Link>
                                        </Button>
                                    </div>
                                </div>
                            </>
                        )}
                    </CardContent>
                </Card>

                {/* 도움말 */}
                <div className="text-center">
                    <p className="text-sm text-gray-600">
                        문제가 지속되면{' '}
                        <Link href="/contact" className="text-primary hover:underline">
                            고객지원
                        </Link>
                        에 문의해주세요.
                    </p>
                </div>
            </div>
        </div>
    )
}