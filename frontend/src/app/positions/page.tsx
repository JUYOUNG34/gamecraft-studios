'use client'

import { useState, useEffect } from 'react'
import Link from 'next/link'
import { Card, CardContent } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Alert, AlertDescription } from '@/components/ui/alert'
import { Search, AlertCircle, Star } from 'lucide-react'
import { usePositions } from '@/hooks/usePositions'
import { JobPositionCard } from '@/components/positions/JobPositionCard'
import { PositionFilters } from '@/components/positions/PositionFilters'
import { PositionPagination } from '@/components/positions/PositionPagination'
import { PositionStats } from '@/components/positions/PositionStats'
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

export default function PositionsPage() {
    const [searchTerm, setSearchTerm] = useState('')
    const [companyFilter, setCompanyFilter] = useState('')
    const [locationFilter, setLocationFilter] = useState('')
    const [experienceLevelFilter, setExperienceLevelFilter] = useState('')
    const [jobTypeFilter, setJobTypeFilter] = useState('')
    const [skillFilter, setSkillFilter] = useState('')
    const [sortBy, setSortBy] = useState('latest')
    const [currentPage, setCurrentPage] = useState(0)

    const {
        positions,
        totalPages,
        totalElements,
        isLoading,
        error,
        filterOptions,
        fetchPositions
    } = usePositions()

    // 필터 변경 시 검색 실행
    useEffect(() => {
        fetchPositions({
            page: currentPage,
            search: searchTerm,
            company: companyFilter,
            location: locationFilter,
            experienceLevel: experienceLevelFilter,
            jobType: jobTypeFilter,
            skill: skillFilter,
            sort: sortBy
        })
    }, [currentPage, searchTerm, companyFilter, locationFilter,
        experienceLevelFilter, jobTypeFilter, skillFilter, sortBy, fetchPositions])

    const handleSearch = () => {
        setCurrentPage(0)
        fetchPositions({
            page: 0,
            search: searchTerm,
            company: companyFilter,
            location: locationFilter,
            experienceLevel: experienceLevelFilter,
            jobType: jobTypeFilter,
            skill: skillFilter,
            sort: sortBy
        })
    }

    const handleResetFilters = () => {
        setSearchTerm('')
        setCompanyFilter('')
        setLocationFilter('')
        setExperienceLevelFilter('')
        setJobTypeFilter('')
        setSkillFilter('')
        setSortBy('latest')
        setCurrentPage(0)
    }

    const handleBookmark = (jobId: number) => {
        // 북마크 로직 (현재는 토스트만 표시)
        console.log('Bookmarking job:', jobId)
        toast.success('북마크에 추가되었습니다!')
    }

    const handleShare = (jobId: number, title: string) => {
        if (navigator.share) {
            navigator.share({
                title: title,
                url: `${window.location.origin}/positions/${jobId}`
            })
        } else {
            navigator.clipboard.writeText(`${window.location.origin}/positions/${jobId}`)
            toast.success('링크가 클립보드에 복사되었습니다!')
        }
    }

    const handlePageChange = (page: number) => {
        setCurrentPage(page)
        window.scrollTo({ top: 0, behavior: 'smooth' })
    }

    if (error) {
        return (
            <div className="container py-8">
                <Alert variant="destructive">
                    <AlertCircle className="h-4 w-4" />
                    <AlertDescription>
                        데이터를 불러올 수 없습니다. 네트워크 연결을 확인하고 다시 시도해주세요.
                    </AlertDescription>
                </Alert>
            </div>
        )
    }

    return (
        <div className="min-h-screen bg-gray-50">
            {/* 헤더 섹션 */}
            <div className="bg-white border-b">
                <div className="container py-8">
                    <div className="text-center mb-8">
                        <h1 className="text-4xl font-bold mb-4">
                            게임 업계 채용공고
                        </h1>
                        <p className="text-xl text-muted-foreground max-w-2xl mx-auto">
                            국내 최고의 게임 회사들에서 새로운 기회를 찾아보세요
                        </p>
                    </div>

                    {/* 검색 및 필터 */}
                    <PositionFilters
                        searchTerm={searchTerm}
                        setSearchTerm={setSearchTerm}
                        companyFilter={companyFilter}
                        setCompanyFilter={setCompanyFilter}
                        locationFilter={locationFilter}
                        setLocationFilter={setLocationFilter}
                        experienceLevelFilter={experienceLevelFilter}
                        setExperienceLevelFilter={setExperienceLevelFilter}
                        jobTypeFilter={jobTypeFilter}
                        setJobTypeFilter={setJobTypeFilter}
                        sortBy={sortBy}
                        setSortBy={setSortBy}
                        filterOptions={filterOptions}
                        onSearch={handleSearch}
                        onReset={handleResetFilters}
                    />
                </div>
            </div>

            {/* 결과 섹션 */}
            <div className="container py-8">
                {/* 통계 정보 */}
                <div className="mb-8">
                    <PositionStats
                        totalElements={totalElements}
                        searchTerm={searchTerm}
                        filterOptions={filterOptions}
                    />
                </div>

                {/* 채용공고 목록 */}
                {isLoading ? (
                    <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {[...Array(6)].map((_, i) => (
                            <Card key={i} className="animate-pulse">
                                <CardContent className="p-6">
                                    <div className="space-y-4">
                                        <div className="h-4 bg-gray-200 rounded w-3/4"></div>
                                        <div className="h-3 bg-gray-200 rounded w-1/2"></div>
                                        <div className="h-3 bg-gray-200 rounded w-2/3"></div>
                                        <div className="flex space-x-2">
                                            <div className="h-6 bg-gray-200 rounded w-16"></div>
                                            <div className="h-6 bg-gray-200 rounded w-16"></div>
                                        </div>
                                    </div>
                                </CardContent>
                            </Card>
                        ))}
                    </div>
                ) : positions && positions.length > 0 ? (
                    <>
                        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
                            {positions.map((job: JobPosition) => (
                                <JobPositionCard
                                    key={job.id}
                                    job={job}
                                    onBookmark={handleBookmark}
                                    onShare={handleShare}
                                />
                            ))}
                        </div>

                        {/* 페이지네이션 */}
                        <PositionPagination
                            currentPage={currentPage}
                            totalPages={totalPages}
                            onPageChange={handlePageChange}
                        />

                        {/* 결과 요약 */}
                        <div className="mt-6 text-center text-sm text-muted-foreground">
                            전체 {totalElements?.toLocaleString()}개 중 {positions.length}개 표시
                        </div>
                    </>
                ) : (
                    <Card>
                        <CardContent className="p-8 text-center">
                            <Search className="h-12 w-12 mx-auto text-muted-foreground mb-4" />
                            <h3 className="text-lg font-semibold mb-2">검색 결과가 없습니다</h3>
                            <p className="text-muted-foreground mb-4">
                                다른 검색어나 필터를 시도해보세요.
                            </p>
                            <Button onClick={handleResetFilters}>
                                필터 초기화
                            </Button>
                        </CardContent>
                    </Card>
                )}

                {/* 추가 정보 섹션 */}
                {!isLoading && positions.length === 0 && !searchTerm && (
                    <div className="mt-8">
                        <Card className="bg-blue-50 border-blue-200">
                            <CardContent className="p-6">
                                <h3 className="font-semibold text-blue-900 mb-2">💡 채용공고 찾기 팁</h3>
                                <ul className="text-sm text-blue-800 space-y-1">
                                    <li>• 관심 있는 기술 스택으로 검색해보세요</li>
                                    <li>• 여러 지역을 조합해서 찾아보세요</li>
                                    <li>• 정기적으로 새로운 공고를 확인하세요</li>
                                    <li>• 북마크 기능을 활용해 관심 공고를 저장하세요</li>
                                </ul>
                            </CardContent>
                        </Card>
                    </div>
                )}

                {/* 관련 링크 */}
                <div className="mt-12 text-center">
                    <Card>
                        <CardContent className="p-6">
                            <h3 className="text-lg font-semibold mb-4">더 많은 기회를 찾고 있나요?</h3>
                            <div className="flex flex-col sm:flex-row gap-3 justify-center">
                                <Button asChild variant="outline">
                                    <Link href="/dashboard/applications">
                                        <Star className="h-4 w-4 mr-2" />
                                        내 지원 현황
                                    </Link>
                                </Button>
                                <Button asChild variant="outline">
                                    <Link href="/dashboard/profile">
                                        프로필 완성하기
                                    </Link>
                                </Button>
                                <Button asChild>
                                    <Link href="/dashboard/applications/new">
                                        지원서 작성하기
                                    </Link>
                                </Button>
                            </div>
                        </CardContent>
                    </Card>
                </div>
            </div>
        </div>
    )
}