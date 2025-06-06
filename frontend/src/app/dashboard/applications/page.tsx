// frontend/src/app/dashboard/applications/page.tsx
'use client'

import { useState, useMemo } from 'react'
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
    DropdownMenuTrigger,
    DropdownMenuSeparator
} from '@/components/ui/dropdown-menu'
import {
    Plus, Search, Filter, MoreHorizontal, Eye, Edit, Trash2,
    Calendar, Clock, CheckCircle, XCircle, AlertCircle, FileText,
    Building, Loader2,  Copy
} from 'lucide-react'
import { useApplications } from '@/hooks/useApplications'
import { useRequireAuth } from '@/hooks/useAuth'
import { toast } from 'sonner'

// 상태 정보 타입 정의
interface StatusInfo {
    label: string
    color: string
    icon: React.ComponentType<React.SVGProps<SVGSVGElement>>
}

type ApplicationStatusMap = {
    [key: string]: StatusInfo
}

export default function ApplicationsPage() {
    const isAuthenticated = useRequireAuth()
    const { myApplications,  isLoadingApplications, applicationsError } = useApplications()

    const [searchTerm, setSearchTerm] = useState('')
    const [statusFilter, setStatusFilter] = useState<string>('all')
    const [selectedTab, setSelectedTab] = useState('all')

    // 상태 정보 매핑
    const statusInfo: ApplicationStatusMap = {
        'SUBMITTED': {
            label: '지원완료',
            color: 'bg-gray-100 text-gray-800 border-gray-200',
            icon: Clock
        },
        'REVIEWING': {
            label: '서류검토',
            color: 'bg-blue-100 text-blue-800 border-blue-200',
            icon: FileText
        },
        'INTERVIEW_SCHEDULED': {
            label: '면접대기',
            color: 'bg-purple-100 text-purple-800 border-purple-200',
            icon: Calendar
        },
        'INTERVIEW_COMPLETED': {
            label: '면접완료',
            color: 'bg-orange-100 text-orange-800 border-orange-200',
            icon: AlertCircle
        },
        'ACCEPTED': {
            label: '합격',
            color: 'bg-green-100 text-green-800 border-green-200',
            icon: CheckCircle
        },
        'REJECTED': {
            label: '불합격',
            color: 'bg-red-100 text-red-800 border-red-200',
            icon: XCircle
        },
        'WITHDRAWN': {
            label: '지원취소',
            color: 'bg-gray-100 text-gray-800 border-gray-200',
            icon: XCircle
        }
    }

    const getStatusInfo = (status: string): StatusInfo => {
        return statusInfo[status] || statusInfo['SUBMITTED']
    }

    // 통계 계산
    const stats = useMemo(() => {
        const total = myApplications.length
        const active = myApplications.filter(app =>
            ['SUBMITTED', 'REVIEWING', 'INTERVIEW_SCHEDULED', 'INTERVIEW_COMPLETED'].includes(app.status)
        ).length
        const accepted = myApplications.filter(app => app.status === 'ACCEPTED').length
        const rejected = myApplications.filter(app => app.status === 'REJECTED').length

        return { total, active, accepted, rejected }
    }, [myApplications])

    // 필터링된 지원서 목록
    const filteredApplications = useMemo(() => {
        let filtered = [...myApplications]

        // 검색 필터링
        if (searchTerm.trim()) {
            const searchLower = searchTerm.toLowerCase()
            filtered = filtered.filter(app =>
                app.company.toLowerCase().includes(searchLower) ||
                app.position.toLowerCase().includes(searchLower)
            )
        }

        // 상태 필터링
        if (statusFilter !== 'all') {
            filtered = filtered.filter(app => app.status === statusFilter)
        }

        // 탭 필터링
        if (selectedTab !== 'all') {
            const tabStatusMap: Record<string, string[]> = {
                active: ['SUBMITTED', 'REVIEWING', 'INTERVIEW_SCHEDULED', 'INTERVIEW_COMPLETED'],
                completed: ['ACCEPTED', 'REJECTED', 'WITHDRAWN']
            }
            if (tabStatusMap[selectedTab]) {
                filtered = filtered.filter(app => tabStatusMap[selectedTab].includes(app.status))
            }
        }

        // 최신순 정렬
        return filtered.sort((a, b) => new Date(b.submittedAt).getTime() - new Date(a.submittedAt).getTime())
    }, [myApplications, searchTerm, statusFilter, selectedTab])

    // 이벤트 핸들러
    const handleViewDetail = (applicationId: string) => {
        // TODO: 상세 페이지로 이동
        console.log('Viewing application:', applicationId)
        toast.info('상세 페이지 기능은 준비 중입니다.')
    }

    const handleEdit = (applicationId: string) => {
        // TODO: 수정 페이지로 이동
        console.log('Editing application:', applicationId)
        toast.info('지원서 수정 기능은 준비 중입니다.')
    }

    const handleDelete = async (applicationId: string) => {
        if (!confirm('정말로 이 지원서를 삭제하시겠습니까?')) {
            return
        }

        try {
            // TODO: 실제 삭제 API 구현
            console.log('Deleting application:', applicationId)
            toast.success('지원서가 삭제되었습니다.')
        } catch (error) {
            console.error('Delete error:', error)
            toast.error('지원서 삭제에 실패했습니다.')
        }
    }

    const handleCopyApplicationId = (applicationId: string) => {
        navigator.clipboard.writeText(applicationId)
        toast.success('지원서 ID가 클립보드에 복사되었습니다.')
    }

    // 날짜 포맷팅
    const formatDate = (dateString: string) => {
        try {
            return new Date(dateString).toLocaleDateString('ko-KR', {
                year: 'numeric',
                month: 'short',
                day: 'numeric'
            })
        } catch {
            return dateString
        }
    }

    // 로딩 상태
    if (isLoadingApplications) {
        return (
            <div className="container py-8 max-w-6xl">
                <div className="flex items-center justify-center h-64">
                    <Loader2 className="h-8 w-8 animate-spin text-primary" />
                    <span className="ml-3 text-muted-foreground">지원 현황을 불러오는 중...</span>
                </div>
            </div>
        )
    }

    // 에러 상태
    if (applicationsError) {
        return (
            <div className="container py-8 max-w-6xl">
                <Card>
                    <CardContent className="p-8 text-center">
                        <AlertCircle className="h-12 w-12 mx-auto text-red-500 mb-4" />
                        <h3 className="text-lg font-semibold mb-2">데이터를 불러올 수 없습니다</h3>
                        <p className="text-muted-foreground mb-4">
                            네트워크 연결을 확인하고 다시 시도해주세요.
                        </p>
                        <Button onClick={() => window.location.reload()}>
                            다시 시도
                        </Button>
                    </CardContent>
                </Card>
            </div>
        )
    }

    // 로그인 상태 확인
    if (!isAuthenticated) {
        return null
    }

    return (
        <div className="container py-8 max-w-6xl">
            {/* 헤더 */}
            <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between mb-8 gap-4">
                <div>
                    <h1 className="text-3xl font-bold">지원 현황</h1>
                    <p className="text-muted-foreground mt-1">
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
                                <SelectItem value="SUBMITTED">지원완료</SelectItem>
                                <SelectItem value="REVIEWING">서류검토</SelectItem>
                                <SelectItem value="INTERVIEW_SCHEDULED">면접대기</SelectItem>
                                <SelectItem value="INTERVIEW_COMPLETED">면접완료</SelectItem>
                                <SelectItem value="ACCEPTED">합격</SelectItem>
                                <SelectItem value="REJECTED">불합격</SelectItem>
                            </SelectContent>
                        </Select>
                    </div>
                </CardContent>
            </Card>

            {/* 탭 및 지원 목록 */}
            <Tabs value={selectedTab} onValueChange={setSelectedTab}>
                <TabsList className="mb-6">
                    <TabsTrigger value="all">전체 ({stats.total})</TabsTrigger>
                    <TabsTrigger value="active">진행 중 ({stats.active})</TabsTrigger>
                    <TabsTrigger value="completed">완료 ({stats.accepted + stats.rejected})</TabsTrigger>
                </TabsList>

                <TabsContent value={selectedTab}>
                    <div className="space-y-4">
                        {filteredApplications.length === 0 ? (
                            <Card>
                                <CardContent className="p-8 text-center">
                                    <FileText className="h-12 w-12 mx-auto text-muted-foreground mb-4" />
                                    <h3 className="text-lg font-semibold mb-2">
                                        {myApplications.length === 0 ? '지원 내역이 없습니다' : '검색 결과가 없습니다'}
                                    </h3>
                                    <p className="text-muted-foreground mb-4">
                                        {myApplications.length === 0
                                            ? '새로운 회사에 지원해보세요!'
                                            : '다른 검색어나 필터를 시도해보세요.'
                                        }
                                    </p>
                                    {myApplications.length === 0 && (
                                        <Button asChild>
                                            <Link href="/dashboard/applications/new">
                                                첫 지원서 작성하기
                                            </Link>
                                        </Button>
                                    )}
                                </CardContent>
                            </Card>
                        ) : (
                            filteredApplications.map((application) => {
                                const statusData = getStatusInfo(application.status)
                                const StatusIcon = statusData.icon

                                return (
                                    <Card key={application.id} className="hover:shadow-md transition-shadow">
                                        <CardContent className="p-6">
                                            <div className="flex items-start justify-between">
                                                <div className="flex-1 min-w-0">
                                                    <div className="flex items-start justify-between mb-3">
                                                        <div className="min-w-0 flex-1">
                                                            <h3 className="text-lg font-semibold truncate">{application.position}</h3>
                                                            <div className="flex items-center text-muted-foreground text-sm mt-1">
                                                                <Building className="h-4 w-4 mr-1 flex-shrink-0" />
                                                                <span className="font-medium truncate">{application.company}</span>
                                                            </div>
                                                        </div>

                                                        <DropdownMenu>
                                                            <DropdownMenuTrigger asChild>
                                                                <Button variant="ghost" size="sm" className="ml-2">
                                                                    <MoreHorizontal className="h-4 w-4" />
                                                                </Button>
                                                            </DropdownMenuTrigger>
                                                            <DropdownMenuContent align="end">
                                                                <DropdownMenuItem onClick={() => handleViewDetail(application.id)}>
                                                                    <Eye className="h-4 w-4 mr-2" />
                                                                    상세 보기
                                                                </DropdownMenuItem>
                                                                <DropdownMenuItem onClick={() => handleEdit(application.id)}>
                                                                    <Edit className="h-4 w-4 mr-2" />
                                                                    수정하기
                                                                </DropdownMenuItem>
                                                                <DropdownMenuSeparator />
                                                                <DropdownMenuItem onClick={() => handleCopyApplicationId(application.id)}>
                                                                    <Copy className="h-4 w-4 mr-2" />
                                                                    ID 복사
                                                                </DropdownMenuItem>
                                                                <DropdownMenuSeparator />
                                                                <DropdownMenuItem
                                                                    onClick={() => handleDelete(application.id)}
                                                                    className="text-destructive focus:text-destructive"
                                                                >
                                                                    <Trash2 className="h-4 w-4 mr-2" />
                                                                    삭제하기
                                                                </DropdownMenuItem>
                                                            </DropdownMenuContent>
                                                        </DropdownMenu>
                                                    </div>

                                                    <div className="flex items-center space-x-4 text-sm text-muted-foreground mb-4">
                                                        <div className="flex items-center">
                                                            <Calendar className="h-4 w-4 mr-1 flex-shrink-0" />
                                                            <span>지원일: {formatDate(application.submittedAt)}</span>
                                                        </div>
                                                    </div>

                                                    <div className="flex items-center justify-between">
                                                        <div className="flex items-center space-x-3">
                                                            <Badge variant="outline" className={statusData.color}>
                                                                <StatusIcon className="h-3 w-3 mr-1" />
                                                                {statusData.label}
                                                            </Badge>
                                                        </div>

                                                        {/* 추가 정보 표시 영역 */}
                                                        <div className="text-xs text-muted-foreground">
                                                            ID: {application.id.slice(-8)}
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

            {/* 결과 요약 */}
            {filteredApplications.length > 0 && (
                <div className="mt-6 text-center text-sm text-muted-foreground">
                    전체 {myApplications.length}개 중 {filteredApplications.length}개 지원서 표시
                </div>
            )}

            {/* 페이지네이션 placeholder */}
            {filteredApplications.length > 10 && (
                <div className="mt-8 flex justify-center">
                    <Card className="p-4">
                        <p className="text-sm text-muted-foreground text-center">
                            📄 더 많은 지원 내역을 보려면 페이지네이션을 구현해야 합니다
                        </p>
                    </Card>
                </div>
            )}

            {/* 도움말 */}
            {myApplications.length === 0 && (
                <div className="mt-8">
                    <Card className="bg-blue-50 border-blue-200">
                        <CardContent className="p-6">
                            <h3 className="font-semibold text-blue-900 mb-2">💡 시작하기 팁</h3>
                            <ul className="text-sm text-blue-800 space-y-1">
                                <li>• 관심 있는 회사에 지원서를 작성해보세요</li>
                                <li>• 포트폴리오와 기술 스택을 상세히 작성하면 좋습니다</li>
                                <li>• 지원 동기는 구체적이고 진솔하게 작성해주세요</li>
                            </ul>
                        </CardContent>
                    </Card>
                </div>
            )}
        </div>
    )
}