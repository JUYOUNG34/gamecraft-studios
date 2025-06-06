// frontend/src/components/layout/Header.tsx
'use client'
import Link from 'next/link'
import { Button } from '@/components/ui/button'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'
import { useAuthStore } from '@/store/authStore'
import { useAuth } from '@/hooks/useAuth'
import {
    Gamepad2,
    User,
    Settings,
    LogOut,
    Plus,
    Loader2,
    FileText,
    Shield
} from 'lucide-react'
import { useState } from 'react'

export function Header() {
    const { user, isAuthenticated } = useAuthStore()
    const { handleLogout } = useAuth()
    const [isLoggingOut, setIsLoggingOut] = useState(false)

    const handleLogoutClick = async () => {
        setIsLoggingOut(true)
        try {
            await handleLogout()
        } finally {
            setIsLoggingOut(false)
        }
    }

    return (
        <header className="sticky top-0 z-50 w-full border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
            <div className="container flex h-16 items-center justify-between">
                {/* 로고 */}
                <Link href="/" className="flex items-center space-x-2">
                    <Gamepad2 className="h-8 w-8 text-primary" />
                    <span className="text-2xl font-bold bg-gradient-to-r from-primary to-purple-600 bg-clip-text text-transparent">
                        GameCraft Studios
                    </span>
                </Link>

                {/* 네비게이션 */}
                <nav className="hidden md:flex items-center space-x-6">
                    <Link
                        href="/about"
                        className="text-sm font-medium text-muted-foreground hover:text-primary transition-colors"
                    >
                        소개
                    </Link>
                    <Link
                        href="/positions"
                        className="text-sm font-medium text-muted-foreground hover:text-primary transition-colors"
                    >
                        채용공고
                    </Link>
                    <Link
                        href="/developers"
                        className="text-sm font-medium text-muted-foreground hover:text-primary transition-colors"
                    >
                        개발자 목록
                    </Link>
                </nav>

                {/* 사용자 메뉴 */}
                <div className="flex items-center space-x-4">
                    {isAuthenticated ? (
                        <>
                            <Button asChild variant="outline" size="sm">
                                <Link href="/dashboard/applications/new">
                                    <Plus className="h-4 w-4 mr-2" />
                                    지원하기
                                </Link>
                            </Button>

                            <DropdownMenu>
                                <DropdownMenuTrigger asChild>
                                    <Button variant="ghost" className="relative h-8 w-8 rounded-full">
                                        <Avatar className="h-8 w-8">
                                            <AvatarImage src={user?.profileImage} alt={user?.nickname} />
                                            <AvatarFallback>
                                                {user?.nickname?.charAt(0).toUpperCase()}
                                            </AvatarFallback>
                                        </Avatar>
                                    </Button>
                                </DropdownMenuTrigger>

                                <DropdownMenuContent className="w-56" align="end" forceMount>
                                    <DropdownMenuLabel className="font-normal">
                                        <div className="flex flex-col space-y-1">
                                            <p className="text-sm font-medium leading-none">{user?.nickname}</p>
                                            <p className="text-xs leading-none text-muted-foreground">
                                                {user?.email}
                                            </p>
                                            {user?.role === 'ADMIN' && (
                                                <div className="pt-1">
                                                    <span className="inline-flex items-center rounded-md bg-red-50 px-2 py-1 text-xs font-medium text-red-700 ring-1 ring-inset ring-red-600/10">
                                                        관리자
                                                    </span>
                                                </div>
                                            )}
                                        </div>
                                    </DropdownMenuLabel>

                                    <DropdownMenuSeparator />

                                    <DropdownMenuItem asChild>
                                        <Link href="/dashboard">
                                            <User className="mr-2 h-4 w-4" />
                                            대시보드
                                        </Link>
                                    </DropdownMenuItem>

                                    <DropdownMenuItem asChild>
                                        <Link href="/dashboard/applications">
                                            <FileText className="mr-2 h-4 w-4" />
                                            지원 현황
                                        </Link>
                                    </DropdownMenuItem>

                                    <DropdownMenuItem asChild>
                                        <Link href="/dashboard/profile">
                                            <Settings className="mr-2 h-4 w-4" />
                                            프로필 설정
                                        </Link>
                                    </DropdownMenuItem>

                                    {user?.role === 'ADMIN' && (
                                        <>
                                            <DropdownMenuSeparator />
                                            <DropdownMenuItem asChild>
                                                <Link href="/admin/dashboard">
                                                    <Shield className="mr-2 h-4 w-4" />
                                                    관리자 패널
                                                </Link>
                                            </DropdownMenuItem>
                                        </>
                                    )}

                                    <DropdownMenuSeparator />

                                    <DropdownMenuItem
                                        onClick={handleLogoutClick}
                                        disabled={isLoggingOut}
                                        className="text-red-600 focus:text-red-600"
                                    >
                                        {isLoggingOut ? (
                                            <>
                                                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                                                로그아웃 중...
                                            </>
                                        ) : (
                                            <>
                                                <LogOut className="mr-2 h-4 w-4" />
                                                로그아웃
                                            </>
                                        )}
                                    </DropdownMenuItem>
                                </DropdownMenuContent>
                            </DropdownMenu>
                        </>
                    ) : (
                        <div className="flex items-center space-x-2">
                            <Button asChild variant="ghost">
                                <Link href="/auth/login">로그인</Link>
                            </Button>
                            <Button asChild>
                                <Link href="/auth/login">시작하기</Link>
                            </Button>
                        </div>
                    )}
                </div>
            </div>
        </header>
    )
}