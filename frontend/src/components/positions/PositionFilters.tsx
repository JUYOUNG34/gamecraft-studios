'use client'

import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import {
    Search, Filter, Building, MapPin, Users, Briefcase, TrendingUp
} from 'lucide-react'

interface FilterOptionsData {
    companies?: Array<{ name: string; count: number }>
    locations?: Array<{ name: string; count: number }>
    experienceLevels?: Record<string, string>
    jobTypes?: Record<string, string>
}

interface PositionFiltersProps {
    searchTerm: string
    setSearchTerm: (term: string) => void
    companyFilter: string
    setCompanyFilter: (company: string) => void
    locationFilter: string
    setLocationFilter: (location: string) => void
    experienceLevelFilter: string
    setExperienceLevelFilter: (level: string) => void
    jobTypeFilter: string
    setJobTypeFilter: (type: string) => void
    sortBy: string
    setSortBy: (sort: string) => void
    filterOptions?: FilterOptionsData
    onSearch: () => void
    onReset: () => void
}

export function PositionFilters({
                                    searchTerm,
                                    setSearchTerm,
                                    companyFilter,
                                    setCompanyFilter,
                                    locationFilter,
                                    setLocationFilter,
                                    experienceLevelFilter,
                                    setExperienceLevelFilter,
                                    jobTypeFilter,
                                    setJobTypeFilter,
                                    sortBy,
                                    setSortBy,
                                    filterOptions,
                                    onSearch,
                                    onReset
                                }: PositionFiltersProps) {
    return (
        <div className="max-w-4xl mx-auto space-y-4">
            {/* 검색바 */}
            <div className="flex space-x-2">
                <div className="relative flex-1">
                    <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
                    <Input
                        placeholder="회사명, 포지션, 기술스택으로 검색..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        onKeyPress={(e) => e.key === 'Enter' && onSearch()}
                        className="pl-10 h-12"
                    />
                </div>
                <Button onClick={onSearch} size="lg">
                    검색
                </Button>
            </div>

            {/* 필터 */}
            <div className="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-2">
                <Select value={companyFilter} onValueChange={setCompanyFilter}>
                    <SelectTrigger>
                        <Building className="h-4 w-4 mr-2" />
                        <SelectValue placeholder="회사" />
                    </SelectTrigger>
                    <SelectContent>
                        <SelectItem value="">전체 회사</SelectItem>
                        {filterOptions?.companies?.map((company) => (
                            <SelectItem key={company.name} value={company.name}>
                                {company.name} ({company.count})
                            </SelectItem>
                        ))}
                    </SelectContent>
                </Select>

                <Select value={locationFilter} onValueChange={setLocationFilter}>
                    <SelectTrigger>
                        <MapPin className="h-4 w-4 mr-2" />
                        <SelectValue placeholder="지역" />
                    </SelectTrigger>
                    <SelectContent>
                        <SelectItem value="">전체 지역</SelectItem>
                        {filterOptions?.locations?.map((location) => (
                            <SelectItem key={location.name} value={location.name}>
                                {location.name} ({location.count})
                            </SelectItem>
                        ))}
                    </SelectContent>
                </Select>

                <Select value={experienceLevelFilter} onValueChange={setExperienceLevelFilter}>
                    <SelectTrigger>
                        <Users className="h-4 w-4 mr-2" />
                        <SelectValue placeholder="경력" />
                    </SelectTrigger>
                    <SelectContent>
                        <SelectItem value="">전체 경력</SelectItem>
                        {filterOptions?.experienceLevels && Object.entries(filterOptions.experienceLevels).map(([key, value]) => (
                            <SelectItem key={key} value={key}>
                                {value}
                            </SelectItem>
                        ))}
                    </SelectContent>
                </Select>

                <Select value={jobTypeFilter} onValueChange={setJobTypeFilter}>
                    <SelectTrigger>
                        <Briefcase className="h-4 w-4 mr-2" />
                        <SelectValue placeholder="고용형태" />
                    </SelectTrigger>
                    <SelectContent>
                        <SelectItem value="">전체 형태</SelectItem>
                        {filterOptions?.jobTypes && Object.entries(filterOptions.jobTypes).map(([key, value]) => (
                            <SelectItem key={key} value={key}>
                                {value}
                            </SelectItem>
                        ))}
                    </SelectContent>
                </Select>

                <Select value={sortBy} onValueChange={setSortBy}>
                    <SelectTrigger>
                        <TrendingUp className="h-4 w-4 mr-2" />
                        <SelectValue placeholder="정렬" />
                    </SelectTrigger>
                    <SelectContent>
                        <SelectItem value="latest">최신순</SelectItem>
                        <SelectItem value="popular">인기순</SelectItem>
                    </SelectContent>
                </Select>

                <Button
                    variant="outline"
                    onClick={onReset}
                    className="w-full"
                >
                    <Filter className="h-4 w-4 mr-2" />
                    초기화
                </Button>
            </div>
        </div>
    )
}