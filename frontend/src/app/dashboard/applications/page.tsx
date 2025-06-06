// frontend/src/app/dashboard/applications/page.tsx
'use client'

import { useState } from 'react'
import Link from 'next/link'
import { Card, CardContent } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { Input } from '@/components/ui/input'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger
} from '@/components/ui/dropdown-menu'
import {
    Plus, Search, Filter, MoreHorizontal, Eye, Edit, Trash2,
    Calendar, Clock, CheckCircle, XCircle, AlertCircle, FileText,
    Building, MapPin, Banknote
} from 'lucide-react'
import { toast } from 'sonner'

interface Application {
    id: string
    company: string
    position: string
    department: string
    location: string
    salary: string
    appliedAt: string
    status: 'pending' | 'reviewing' | 'interview' | 'technical' | 'final' | 'accepted' | 'rejected'
    lastUpdate: string
    recruiter?: string
    notes?: string
    nextStep?: string
    nextStepDate?: string
}

export default function ApplicationsPage() {
    const [searchTerm, setSearchTerm] = useState('')
    const [statusFilter, setStatusFilter] = useState<string>('all')
    const [selectedTab, setSelectedTab] = useState('all')

    // 가상의 지원 데이터
    const [applications] = useState<Application[]>([
        {
            id: '1',
            company: '카카오게임즈',
            position: '웹 플랫폼 개발자',
            department: '플랫폼개발팀',
            location: '판교',
            salary: '협의',
            appliedAt: '2025-06-01',
            status: 'interview',
            lastUpdate: '2025-06-03',
            recruiter: '김채용 매니저',
            nextStep: '기술 면접',
            nextStepDate: '2025-06-10'
        },
        {
            id: '2',
            company: 'Krafton',
            position: '게임 클라이언트 개발자',
            department: '배틀그라운드팀',
            location: '서울',
            salary: '6000만원',
            appliedAt: '2025-05-28',
            status: 'reviewing',
            lastUpdate: '2025-05-30',
            recruiter: '박인사 팀장'
        },
        {
            id: '3',
            company: 'Pearl Abyss',
            position: '백엔드 개발자',
            department: '서버개발팀',
            location: '안양',
            salary: '5500만원',
            appliedAt: '2025-05-25',
            status: 'technical',
            lastUpdate: '2025-06-02',
            recruiter: '이기술 매니저',
            nextStep: '기술 과제 제출',
            nextStepDate: '2025-06-08'
        },
        {
            id: '4',
            company: 'NCSoft',
            position: '프론트엔드 개발자',
            department: '웹서비스팀',
            location: '판교',
            salary: '5000만원',
            appliedAt: '2025-05-20',
            status: 'accepted',
            lastUpdate: '2025-06-01',
            recruiter: '정합격 매니저'
        },
        {
            id: '5',
            company: 'Smilegate',
            position: '풀스택 개발자',
            department: '플랫폼팀',
            location: '서울',
            salary: '협의',
            appliedAt: '2025-05-15',
            status: 'rejected',
            lastUpdate: '2025-05-22',
            recruiter: '최불합격 팀장'
        },
        {
            id: '6',
            company: 'Nexon',
            position: 'DevOps 엔지니어',
            department: '인프라팀',
            location: '판교',
            salary: '7000만원',
            appliedAt: '2025-05-10',
            status: 'pending',
            lastUpdate: '2025-05-10'
        }
    ])

    const getStatusInfo = (status: Application['status']) => {
        const statusMap = {
            pending: {
                label: '지원완료',
                color: 'bg-gray-100 text-gray-800',
                icon: Clock
            },
            reviewing: {
                label: '서류검토',
                color: 'bg-blue-100 text-blue-800',
                icon: FileText
            },
            interview: {
                label: '면접대기',
                color: 'bg-purple-100 text-purple-800',
                icon: Calendar
            },
            technical: {
                label: '기술과제',
                color: 'bg-orange-100 text-orange-800',
                icon: AlertCircle
            },
            final: {
                label: '최종면접',
                color: 'bg-yellow-100 text-yellow-800',
                icon: Calendar
            },
            accepted: {
                label: '합격',
                color: 'bg-green-100 text-green-800',
                icon: CheckCircle
            },
            rejected: {
                label: '불합격',
                color: 'bg-red-100 text-red-800',
                icon: XCircle
            }
        }
        return statusMap[status]
    }

    const getFilteredApplications = () => {
        let filtered = applications

        // 검색 필터링
        if (searchTerm) {
            filtered = filtered.filter(app =>
                app.company.toLowerCase().includes(searchTerm.toLowerCase()) ||
                app.position.toLowerCase().includes(searchTerm.toLowerCase())
            )
        }

        // 상태 필터링
        if (statusFilter !== 'all') {
            filtered = filtered.filter(app => app.status === statusFilter)
        }

        // 탭 필터링
        if (selectedTab !== 'all') {
            const tabStatusMap: Record<string, Application['status'][]> = {
                active: ['pending', 'reviewing', 'interview', 'technical', 'final'],
                completed: ['accepted', 'rejected']
            }
            if (tabStatusMap[selectedTab]) {
                filtered = filtered.filter(app => tabStatusMap[selectedTab].includes(app.status))
            }
        }

        return filtered.sort((a, b) => new Date(b.lastUpdate).getTime() - new Date(a.lastUpdate).getTime())
    }

    const handleDelete = (id: string) => {
        toast.success('지원서가 삭제되었습니다')
    }

    const getStatistics = () => {
        const total = applications.length
        const active = applications.filter(app =>
            ['pending', 'reviewing', 'interview', 'technical', 'final'].includes(app.status)
        ).length
        const accepted = applications.filter(app => app.status === 'accepted').length
        const rejected = applications.filter(app => app.status === 'rejected').length

        return { total, active, accepted, rejected }
    }

    const stats = getStatistics()
    const filteredApplications = getFilteredApplications()

    return (
        <div className="container py-8 max-w-6xl">
            {/* 헤더 */}
            <div className="flex items-center justify-between mb-8">
                <div>
                    <h1 className="text-3xl font-bold">지원 현황</h1>
                    <p className="text-muted-foreground">
                        내가 지원한 회사들의 진행 상황을 확인하고 관리하세요
                    </p>
                </div>

                <Button asChild>
                    <Link href="/dashboard/applications/new">
                        <Plus className="h-4 w-4 mr-2" />
                        새 지원서 작성
                    </Link>
                </Button>
            </div>

            {/* 통계 카드 */}
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
                <Card>
                    <CardContent className="p-4">
                        <div className="text-2xl font-bold">{stats.total}</div>
                        <p className="text-sm text-muted-foreground">총 지원</p>
                    </CardContent>
                </Card>
                <Card>
                    <CardContent className="p-4">
                        <div className="text-2xl font-bold text-blue-600">{stats.active}</div>
                        <p className="text-sm text-muted-foreground">진행 중</p>
                    </CardContent>
                </Card>
                <Card>
                    <CardContent className="p-4">
                        <div className="text-2xl font-bold text-green-600">{stats.accepted}</div>
                        <p className="text-sm text-muted-foreground">합격</p>
                    </CardContent>
                </Card>
                <Card>
                    <CardContent className="p-4">
                        <div className="text-2xl font-bold text-red-600">{stats.rejected}</div>
                        <p className="text-sm text-muted-foreground">불합격</p>
                    </CardContent>
                </Card>
            </div>

            {/* 필터 및 검색 */}
            <Card className="mb-6">
                <CardContent className="p-4">
                    <div className="flex flex-col md:flex-row gap-4">
                        <div className="flex-1">
                            <div className="relative">
                                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
                                <Input
                                    placeholder="회사명 또는 포지션으로 검색..."
                                    value={searchTerm}
                                    onChange={(e) => setSearchTerm(e.target.value)}
                                    className="pl-10"
                                />
                            </div>
                        </div>

                        <Select value={statusFilter} onValueChange={setStatusFilter}>
                            <SelectTrigger className="w-full md:w-[180px]">
                                <Filter className="h-4 w-4 mr-2" />
                                <SelectValue placeholder="상태 필터" />
                            </SelectTrigger>
                            <SelectContent>
                                <SelectItem value="all">모든 상태</SelectItem>
                                <SelectItem value="pending">지원완료</SelectItem>
                                <SelectItem value="reviewing">서류검토</SelectItem>
                                <SelectItem value="interview">면접대기</SelectItem>
                                <SelectItem value="technical">기술과제</SelectItem>
                                <SelectItem value="accepted">합격</SelectItem>
                                <SelectItem value="rejected">불합격</SelectItem>
                            </SelectContent>
                        </Select>
                    </div>
                </CardContent>
            </Card>

            {/* 탭 및 지원 목록 */}
            <Tabs value={selectedTab} onValueChange={setSelectedTab}>
                <TabsList className="mb-6">
                    <TabsTrigger value="all">전체 ({applications.length})</TabsTrigger>
                    <TabsTrigger value="active">진행 중 ({stats.active})</TabsTrigger>
                    <TabsTrigger value="completed">완료 ({stats.accepted + stats.rejected})</TabsTrigger>
                </TabsList>

                <TabsContent value={selectedTab}>
                    <div className="space-y-4">
                        {filteredApplications.length === 0 ? (
                            <Card>
                                <CardContent className="p-8 text-center">
                                    <FileText className="h-12 w-12 mx-auto text-muted-foreground mb-4" />
                                    <h3 className="text-lg font-semibold mb-2">지원 내역이 없습니다</h3>
                                    <p className="text-muted-foreground mb-4">
                                        새로운 회사에 지원해보세요!
                                    </p>
                                    <Button asChild>
                                        <Link href="/dashboard/applications/new">
                                            첫 지원서 작성하기
                                        </Link>
                                    </Button>
                                </CardContent>
                            </Card>
                        ) : (
                            filteredApplications.map((application) => {
                                const statusInfo = getStatusInfo(application.status)
                                const StatusIcon = statusInfo.icon

                                return (
                                    <Card key={application.id} className="hover:shadow-md transition-shadow">
                                        <CardContent className="p-6">
                                            <div className="flex items-start justify-between">
                                                <div className="flex-1">
                                                    <div className="flex items-start justify-between mb-3">
                                                        <div>
                                                            <h3 className="text-lg font-semibold">{application.position}</h3>
                                                            <div className="flex items-center text-muted-foreground text-sm mt-1">
                                                                <Building className="h-4 w-4 mr-1" />
                                                                <span className="font-medium">{application.company}</span>
                                                                <span className="mx-2">•</span>
                                                                <span>{application.department}</span>
                                                            </div>
                                                        </div>

                                                        <DropdownMenu>
                                                            <DropdownMenuTrigger asChild>
                                                                <Button variant="ghost" size="sm">
                                                                    <MoreHorizontal className="h-4 w-4" />
                                                                </Button>
                                                            </DropdownMenuTrigger>
                                                            <DropdownMenuContent align="end">
                                                                <DropdownMenuItem>
                                                                    <Eye className="h-4 w-4 mr-2" />
                                                                    상세 보기
                                                                </DropdownMenuItem>
                                                                <DropdownMenuItem>
                                                                    <Edit className="h-4 w-4 mr-2" />
                                                                    수정하기
                                                                </DropdownMenuItem>
                                                                <DropdownMenuItem
                                                                    onClick={() => handleDelete(application.id)}
                                                                    className="text-destructive"
                                                                >
                                                                    <Trash2 className="h-4 w-4 mr-2" />
                                                                    삭제하기
                                                                </DropdownMenuItem>
                                                            </DropdownMenuContent>
                                                        </DropdownMenu>
                                                    </div>

                                                    <div className="flex items-center space-x-4 text-sm text-muted-foreground mb-3">
                                                        <div className="flex items-center">
                                                            <MapPin className="h-4 w-4 mr-1" />
                                                            {application.location}
                                                        </div>
                                                        <div className="flex items-center">
                                                            <Banknote className="h-4 w-4 mr-1" />
                                                            {application.salary}
                                                        </div>
                                                        <div className="flex items-center">
                                                            <Calendar className="h-4 w-4 mr-1" />
                                                            지원일: {application.appliedAt}
                                                        </div>
                                                    </div>

                                                    <div className="flex items-center justify-between">
                                                        <div className="flex items-center space-x-3">
                                                            <Badge className={statusInfo.color}>
                                                                <StatusIcon className="h-3 w-3 mr-1" />
                                                                {statusInfo.label}
                                                            </Badge>

                                                            {application.recruiter && (
                                                                <span className="text-sm text-muted-foreground">
                                  담당자: {application.recruiter}
                                </span>
                                                            )}
                                                        </div>

                                                        <div className="text-right">
                                                            <p className="text-sm text-muted-foreground">
                                                                마지막 업데이트: {application.lastUpdate}
                                                            </p>
                                                            {application.nextStep && application.nextStepDate && (
                                                                <p className="text-sm font-medium text-primary">
                                                                    다음: {application.nextStep} ({application.nextStepDate})
                                                                </p>
                                                            )}
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </CardContent>
                                    </Card>
                                )
                            })
                        )}
                    </div>
                </TabsContent>
            </Tabs>

            {/* 페이지네이션 (나중에 추가) */}
            {filteredApplications.length > 10 && (
                <div className="mt-8 flex justify-center">
                    <p className="text-sm text-muted-foreground">
                        더 많은 지원 내역을 보려면 페이지네이션을 구현해야 합니다
                    </p>
                </div>
            )}
        </div>
    )
}