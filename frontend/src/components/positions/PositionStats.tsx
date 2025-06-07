'use client'

import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import { Building, MapPin, Users, Briefcase, TrendingUp, Clock } from 'lucide-react'

interface PositionStatsProps {
    totalElements: number
    searchTerm?: string
    filterOptions?: {
        companies?: Array<{ name: string; count: number }>
        locations?: Array<{ name: string; count: number }>
        experienceLevels?: Record<string, string>
        jobTypes?: Record<string, string>
    }
}

export function PositionStats({ totalElements, searchTerm, filterOptions }: PositionStatsProps) {
    const topCompanies = filterOptions?.companies?.slice(0, 5) || []
    const topLocations = filterOptions?.locations?.slice(0, 5) || []

    return (
        <div className="space-y-6">
            {/* 검색 결과 헤더 */}
            <div className="text-center">
                <h2 className="text-2xl font-semibold">
                    채용공고 {totalElements?.toLocaleString() || 0}개
                </h2>
                {searchTerm && (
                    <p className="text-muted-foreground mt-1">
                        "{searchTerm}" 검색 결과
                    </p>
                )}
            </div>

            {/* 빠른 통계 */}
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                <Card>
                    <CardContent className="p-4 text-center">
                        <div className="flex items-center justify-center mb-2">
                            <Building className="h-5 w-5 text-primary" />
                        </div>
                        <div className="text-2xl font-bold">
                            {filterOptions?.companies?.length || 0}
                        </div>
                        <p className="text-sm text-muted-foreground">참여 기업</p>
                    </CardContent>
                </Card>

                <Card>
                    <CardContent className="p-4 text-center">
                        <div className="flex items-center justify-center mb-2">
                            <MapPin className="h-5 w-5 text-green-600" />
                        </div>
                        <div className="text-2xl font-bold">
                            {filterOptions?.locations?.length || 0}
                        </div>
                        <p className="text-sm text-muted-foreground">근무 지역</p>
                    </CardContent>
                </Card>

                <Card>
                    <CardContent className="p-4 text-center">
                        <div className="flex items-center justify-center mb-2">
                            <Briefcase className="h-5 w-5 text-blue-600" />
                        </div>
                        <div className="text-2xl font-bold">
                            {Object.keys(filterOptions?.jobTypes || {}).length}
                        </div>
                        <p className="text-sm text-muted-foreground">고용 형태</p>
                    </CardContent>
                </Card>

                <Card>
                    <CardContent className="p-4 text-center">
                        <div className="flex items-center justify-center mb-2">
                            <Clock className="h-5 w-5 text-orange-600" />
                        </div>
                        <div className="text-2xl font-bold">
                            매일
                        </div>
                        <p className="text-sm text-muted-foreground">업데이트</p>
                    </CardContent>
                </Card>
            </div>

            {/* 인기 기업 & 지역 */}
            <div className="grid md:grid-cols-2 gap-6">
                <Card>
                    <CardHeader>
                        <CardTitle className="flex items-center text-lg">
                            <TrendingUp className="h-5 w-5 mr-2" />
                            인기 기업
                        </CardTitle>
                        <CardDescription>
                            가장 많은 채용공고를 올린 기업들
                        </CardDescription>
                    </CardHeader>
                    <CardContent>
                        <div className="space-y-2">
                            {topCompanies.map((company, index) => (
                                <div key={company.name} className="flex items-center justify-between">
                                    <div className="flex items-center space-x-2">
                                        <Badge variant="outline" className="w-6 h-6 p-0 justify-center text-xs">
                                            {index + 1}
                                        </Badge>
                                        <span className="font-medium">{company.name}</span>
                                    </div>
                                    <Badge variant="secondary">
                                        {company.count}개
                                    </Badge>
                                </div>
                            ))}
                        </div>
                    </CardContent>
                </Card>

                <Card>
                    <CardHeader>
                        <CardTitle className="flex items-center text-lg">
                            <MapPin className="h-5 w-5 mr-2" />
                            인기 지역
                        </CardTitle>
                        <CardDescription>
                            채용공고가 많은 근무 지역들
                        </CardDescription>
                    </CardHeader>
                    <CardContent>
                        <div className="space-y-2">
                            {topLocations.map((location, index) => (
                                <div key={location.name} className="flex items-center justify-between">
                                    <div className="flex items-center space-x-2">
                                        <Badge variant="outline" className="w-6 h-6 p-0 justify-center text-xs">
                                            {index + 1}
                                        </Badge>
                                        <span className="font-medium">{location.name}</span>
                                    </div>
                                    <Badge variant="secondary">
                                        {location.count}개
                                    </Badge>
                                </div>
                            ))}
                        </div>
                    </CardContent>
                </Card>
            </div>
        </div>
    )
}