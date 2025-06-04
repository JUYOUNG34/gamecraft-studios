'use client'

import { useAuthStore } from '@/store/authStore'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { Plus, Briefcase, User, Settings, TrendingUp } from 'lucide-react'
import Link from 'next/link'

export default function DashboardPage() {
    const { user } = useAuthStore()

    const stats = [
        {
            title: '지원한 회사',
            value: '3',
            description: '이번 달',
            icon: Briefcase,
            color: 'text-blue-600'
        },
        {
            title: '프로필 조회수',
            value: '127',
            description: '+12% from last month',
            icon: TrendingUp,
            color: 'text-green-600'
        },
        {
            title: '스킬 매칭률',
            value: '85%',
            description: '카카오게임즈 요구사항',
            icon: User,
            color: 'text-purple-600'
        }
    ]

    const recentApplications = [
        {
            company: '카카오게임즈',
            position: '웹 플랫폼 개발자',
            status: 'pending',
            appliedAt: '2025-06-01'
        },
        {
            company: 'Krafton',
            position: '게임 클라이언트 개발자',
            status: 'reviewing',
            appliedAt: '2025-05-28'
        },
        {
            company: 'Pearl Abyss',
            position: '백엔드 개발자',
            status: 'interview',
            appliedAt: '2025-05-25'
        }
    ]

    const getStatusColor = (status: string) => {
        switch (status) {
            case 'pending':
                return 'bg-yellow-100 text-yellow-800'
            case 'reviewing':
                return 'bg-blue-100 text-blue-800'
            case 'interview':
                return 'bg-green-100 text-green-800'
            default:
                return 'bg-gray-100 text-gray-800'
        }
    }

    const getStatusText = (status: string) => {
        switch (status) {
            case 'pending':
                return '대기중'
            case 'reviewing':
                return '검토중'
            case 'interview':
                return '면접예정'
            default:
                return status
        }
    }

    return (
        <div className="container py-8">
            {/* 헤더 */}
            <div className="flex items-center justify-between mb-8">
                <div className="flex items-center space-x-4">
                    <Avatar className="h-12 w-12">
                        <AvatarImage src={user?.profileImage} alt={user?.nickname} />
                        <AvatarFallback>
                            {user?.nickname?.charAt(0).toUpperCase()}
                        </AvatarFallback>
                    </Avatar>
                    <div>
                        <h1 className="text-2xl font-bold">안녕하세요, {user?.nickname}님!</h1>
                        <p className="text-muted-foreground">
                            오늘도 멋진 개발자 여정을 이어가세요 🚀
                        </p>
                    </div>
                </div>

                <div className="flex space-x-3">
                    <Button asChild variant="outline">
                        <Link href="/dashboard/profile">
                            <Settings className="h-4 w-4 mr-2" />
                            프로필 설정
                        </Link>
                    </Button>
                    <Button asChild>
                        <Link href="/dashboard/applications/new">
                            <Plus className="h-4 w-4 mr-2" />
                            새 지원서
                        </Link>
                    </Button>
                </div>
            </div>

            {/* 통계 카드들 */}
            <div className="grid md:grid-cols-3 gap-6 mb-8">
                {stats.map((stat, index) => {
                    const Icon = stat.icon
                    return (
                        <Card key={index}>
                            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                                <CardTitle className="text-sm font-medium">
                                    {stat.title}
                                </CardTitle>
                                <Icon className={`h-4 w-4 ${stat.color}`} />
                            </CardHeader>
                            <CardContent>
                                <div className="text-2xl font-bold">{stat.value}</div>
                                <p className="text-xs text-muted-foreground">
                                    {stat.description}
                                </p>
                            </CardContent>
                        </Card>
                    )
                })}
            </div>

            {/* 최근 지원 현황 */}
            <Card>
                <CardHeader>
                    <CardTitle>최근 지원 현황</CardTitle>
                    <CardDescription>
                        최근에 지원한 포지션들의 진행 상황을 확인하세요
                    </CardDescription>
                </CardHeader>
                <CardContent>
                    <div className="space-y-4">
                        {recentApplications.map((application, index) => (
                            <div
                                key={index}
                                className="flex items-center justify-between p-4 border rounded-lg hover:bg-muted/50 transition-colors"
                            >
                                <div className="flex-1">
                                    <div className="flex items-center space-x-3">
                                        <div>
                                            <h3 className="font-medium">{application.position}</h3>
                                            <p className="text-sm text-muted-foreground">
                                                {application.company}
                                            </p>
                                        </div>
                                    </div>
                                </div>

                                <div className="flex items-center space-x-4">
                                    <Badge className={getStatusColor(application.status)}>
                                        {getStatusText(application.status)}
                                    </Badge>
                                    <span className="text-sm text-muted-foreground">
                    {application.appliedAt}
                  </span>
                                </div>
                            </div>
                        ))}
                    </div>

                    <div className="mt-6 text-center">
                        <Button asChild variant="outline" className="w-full">
                            <Link href="/dashboard/applications">
                                모든 지원 현황 보기
                            </Link>
                        </Button>
                    </div>
                </CardContent>
            </Card>
        </div>
    )
}
