'use client'

import Link from 'next/link'
import { Card, CardContent } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger,
    DropdownMenuSeparator
} from '@/components/ui/dropdown-menu'
import {
    Building, MapPin, Briefcase, Eye, Users, Calendar, ChevronDown,
    BookmarkPlus, Share2, ExternalLink
} from 'lucide-react'
import { toast } from 'sonner'

interface JobPosition {
    id: number
    slug: string
    title: string
    company: string
    companyLogo?: string
    location: string
    jobType: string
    jobTypeDescription: string
    experienceLevel: string
    experienceLevelDescription: string
    salaryRange?: string
    remoteWorkAvailable: boolean
    requiredSkills: string[]
    preferredSkills: string[]
    viewCount: number
    applicationCount: number
    createdAt: string
    applicationDeadline?: string
}

interface JobPositionCardProps {
    job: JobPosition
    onBookmark?: (jobId: number) => void
    onShare?: (jobId: number, title: string) => void
}

export function JobPositionCard({ job, onBookmark, onShare }: JobPositionCardProps) {
    const formatDate = (dateString: string) => {
        try {
            return new Date(dateString).toLocaleDateString('ko-KR', {
                month: 'short',
                day: 'numeric'
            })
        } catch {
            return dateString
        }
    }

    const getDaysAgo = (dateString: string) => {
        try {
            const date = new Date(dateString)
            const now = new Date()
            const diffTime = Math.abs(now.getTime() - date.getTime())
            const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))

            if (diffDays === 1) return '1일 전'
            if (diffDays < 7) return `${diffDays}일 전`
            if (diffDays < 30) return `${Math.floor(diffDays / 7)}주 전`
            return `${Math.floor(diffDays / 30)}개월 전`
        } catch {
            return ''
        }
    }

    const handleBookmark = () => {
        if (onBookmark) {
            onBookmark(job.id)
        }
    }

    const handleShare = () => {
        if (onShare) {
            onShare(job.id, job.title)
        }
    }

    return (
        <Card className="hover:shadow-lg transition-shadow group">
            <CardContent className="p-6">
                {/* 회사 로고 및 정보 */}
                <div className="flex items-start justify-between mb-4">
                    <div className="flex items-center space-x-3">
                        <div className="w-12 h-12 rounded-lg bg-gray-100 flex items-center justify-center">
                            {job.companyLogo ? (
                                <img
                                    src={job.companyLogo}
                                    alt={job.company}
                                    className="w-8 h-8 object-contain"
                                />
                            ) : (
                                <Building className="h-6 w-6 text-gray-400" />
                            )}
                        </div>
                        <div>
                            <h3 className="font-semibold text-sm text-muted-foreground">
                                {job.company}
                            </h3>
                            <p className="text-xs text-muted-foreground">
                                {getDaysAgo(job.createdAt)}
                            </p>
                        </div>
                    </div>

                    <DropdownMenu>
                        <DropdownMenuTrigger asChild>
                            <Button variant="ghost" size="sm" className="opacity-0 group-hover:opacity-100 transition-opacity">
                                <ChevronDown className="h-4 w-4" />
                            </Button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent align="end">
                            <DropdownMenuItem onClick={handleBookmark}>
                                <BookmarkPlus className="h-4 w-4 mr-2" />
                                북마크 추가
                            </DropdownMenuItem>
                            <DropdownMenuItem onClick={handleShare}>
                                <Share2 className="h-4 w-4 mr-2" />
                                공유하기
                            </DropdownMenuItem>
                            <DropdownMenuSeparator />
                            <DropdownMenuItem asChild>
                                <Link href={`/positions/${job.id}`} target="_blank">
                                    <ExternalLink className="h-4 w-4 mr-2" />
                                    새 탭에서 열기
                                </Link>
                            </DropdownMenuItem>
                        </DropdownMenuContent>
                    </DropdownMenu>
                </div>

                {/* 포지션 제목 */}
                <Link href={`/positions/${job.id}`}>
                    <h2 className="text-lg font-semibold mb-3 line-clamp-2 hover:text-primary transition-colors">
                        {job.title}
                    </h2>
                </Link>

                {/* 위치 및 조건 */}
                <div className="space-y-2 mb-4">
                    <div className="flex items-center text-sm text-muted-foreground">
                        <MapPin className="h-4 w-4 mr-2 flex-shrink-0" />
                        <span>{job.location}</span>
                        {job.remoteWorkAvailable && (
                            <Badge variant="secondary" className="ml-2 text-xs">
                                원격근무
                            </Badge>
                        )}
                    </div>

                    <div className="flex items-center text-sm text-muted-foreground">
                        <Briefcase className="h-4 w-4 mr-2 flex-shrink-0" />
                        <span>{job.jobTypeDescription}</span>
                        <span className="mx-2">•</span>
                        <span>{job.experienceLevelDescription}</span>
                    </div>

                    {job.salaryRange && (
                        <div className="text-sm font-medium text-green-600">
                            {job.salaryRange}
                        </div>
                    )}
                </div>

                {/* 기술 스택 */}
                <div className="mb-4">
                    <div className="flex flex-wrap gap-1">
                        {job.requiredSkills?.slice(0, 4).map((skill, index) => (
                            <Badge key={index} variant="outline" className="text-xs">
                                {skill}
                            </Badge>
                        ))}
                        {job.requiredSkills?.length > 4 && (
                            <Badge variant="outline" className="text-xs">
                                +{job.requiredSkills.length - 4}
                            </Badge>
                        )}
                    </div>
                </div>

                {/* 하단 정보 */}
                <div className="flex items-center justify-between text-xs text-muted-foreground pt-2 border-t">
                    <div className="flex items-center space-x-3">
                        <div className="flex items-center">
                            <Eye className="h-3 w-3 mr-1" />
                            {job.viewCount}
                        </div>
                        <div className="flex items-center">
                            <Users className="h-3 w-3 mr-1" />
                            {job.applicationCount}
                        </div>
                    </div>

                    {job.applicationDeadline && (
                        <div className="flex items-center text-red-600">
                            <Calendar className="h-3 w-3 mr-1" />
                            마감 {formatDate(job.applicationDeadline)}
                        </div>
                    )}
                </div>

                {/* 지원 버튼 */}
                <Link href={`/dashboard/applications/new?position=${job.id}`}>
                    <Button className="w-full mt-4">
                        지원하기
                    </Button>
                </Link>
            </CardContent>
        </Card>
    )
}