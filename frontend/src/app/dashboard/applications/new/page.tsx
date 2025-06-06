// frontend/src/app/dashboard/applications/new/page.tsx
'use client'

import { useState, useEffect } from 'react'
import { useRouter } from 'next/navigation'
import { useAuthStore } from '@/store/authStore'
import { useApplications } from '@/hooks/useApplications'
import { useRequireAuth } from '@/hooks/useAuth'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Textarea } from '@/components/ui/textarea'
import { Badge } from '@/components/ui/badge'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Send, Plus, X, Briefcase, Code } from 'lucide-react'
import { CreateApplicationRequest } from '@/lib/api'
import { toast } from 'sonner'

export default function NewApplicationPage() {
    const router = useRouter()
    const { user } = useAuthStore()
    const isAuthenticated = useRequireAuth()
    const { formInfo, createApplication, isCreatingApplication, isLoadingFormInfo } = useApplications()

    const [formData, setFormData] = useState({
        // 기본 정보
        position: '',
        company: '',
        department: '',

        // 개인 정보
        name: user?.nickname || '',
        email: user?.email || '',
        phone: '',
        location: '서울, 대한민국',

        // 경력 정보
        experienceLevel: 'JUNIOR' as 'JUNIOR' | 'MIDDLE' | 'SENIOR' | 'LEAD',
        jobType: 'FULL_TIME' as 'FULL_TIME' | 'CONTRACT' | 'FREELANCE' | 'INTERN',
        currentCompany: '',
        currentPosition: '',
        expectedSalary: '',

        // 포트폴리오
        portfolioUrl: '',
        githubUrl: '',

        // 지원 동기
        coverLetter: '',
        strengths: '',
        projectExperience: '',

        // 추가 정보
        availableStartDate: '',
        workLocation: '서울'
    })

    const [skills, setSkills] = useState<string[]>([
        'React', 'TypeScript', 'Next.js', 'Node.js'
    ])
    const [newSkill, setNewSkill] = useState('')

    // 사용자 정보 업데이트
    useEffect(() => {
        if (user) {
            setFormData(prev => ({
                ...prev,
                name: user.nickname || '',
                email: user.email || ''
            }))
        }
    }, [user])

    // 로그인 상태 확인
    if (!isAuthenticated) {
        return null // useRequireAuth에서 리다이렉트 처리
    }

    const addSkill = () => {
        if (newSkill.trim() && !skills.includes(newSkill.trim())) {
            setSkills([...skills, newSkill.trim()])
            setNewSkill('')
            toast.success(`${newSkill} 스킬이 추가되었습니다!`)
        }
    }

    const removeSkill = (skillToRemove: string) => {
        setSkills(skills.filter(skill => skill !== skillToRemove))
        toast.success(`${skillToRemove} 스킬이 제거되었습니다!`)
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()

        // 폼 검증
        if (!formData.company.trim()) {
            toast.error('회사명을 선택해주세요')
            return
        }

        if (!formData.position.trim()) {
            toast.error('지원 포지션을 선택해주세요')
            return
        }

        if (!formData.coverLetter.trim()) {
            toast.error('지원 동기를 입력해주세요')
            return
        }

        if (formData.coverLetter.length < 50) {
            toast.error('지원 동기는 50자 이상 입력해주세요')
            return
        }

        try {
            const applicationData: CreateApplicationRequest = {
                company: formData.company,
                position: formData.position,
                experienceLevel: formData.experienceLevel,
                jobType: formData.jobType,
                skills: skills,
                coverLetter: formData.coverLetter,
                expectedSalary: formData.expectedSalary || undefined,
                availableStartDate: formData.availableStartDate || undefined,
                workLocation: formData.workLocation || undefined,
            }

            const response = await createApplication(applicationData)

            if (response.success) {
                router.push('/dashboard/applications')
            }
        } catch (error) {
            console.error('지원서 제출 오류:', error)
        }
    }

    if (isLoadingFormInfo) {
        return (
            <div className="container py-8 max-w-4xl">
                <div className="flex items-center justify-center h-64">
                    <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
                    <span className="ml-2">로딩 중...</span>
                </div>
            </div>
        )
    }

    return (
        <div className="container py-8 max-w-4xl">
            <div className="mb-8">
                <h1 className="text-3xl font-bold mb-2">새 지원서 작성</h1>
                <p className="text-muted-foreground">
                    정확하고 자세한 정보를 입력하여 채용 담당자에게 좋은 인상을 남기세요
                </p>
            </div>

            <form onSubmit={handleSubmit} className="space-y-8">
                {/* 지원 정보 */}
                <Card>
                    <CardHeader>
                        <CardTitle className="flex items-center">
                            <Briefcase className="h-5 w-5 mr-2" />
                            지원 정보
                        </CardTitle>
                        <CardDescription>
                            지원하려는 회사와 포지션을 선택해주세요
                        </CardDescription>
                    </CardHeader>
                    <CardContent className="space-y-4">
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div className="space-y-2">
                                <label className="text-sm font-medium">회사명 *</label>
                                <Select
                                    value={formData.company}
                                    onValueChange={(value) => setFormData({...formData, company: value})}
                                >
                                    <SelectTrigger>
                                        <SelectValue placeholder="회사를 선택하세요" />
                                    </SelectTrigger>
                                    <SelectContent>
                                        {formInfo?.companies?.map((company) => (
                                            <SelectItem key={company} value={company}>
                                                {company}
                                            </SelectItem>
                                        )) || [
                                            '카카오게임즈', 'Krafton', 'NCSoft', 'Pearl Abyss',
                                            'Smilegate', 'Nexon', 'Netmarble', 'Com2uS'
                                        ].map((company) => (
                                            <SelectItem key={company} value={company}>
                                                {company}
                                            </SelectItem>
                                        ))}
                                    </SelectContent>
                                </Select>
                            </div>

                            <div className="space-y-2">
                                <label className="text-sm font-medium">지원 포지션 *</label>
                                <Select
                                    value={formData.position}
                                    onValueChange={(value) => setFormData({...formData, position: value})}
                                >
                                    <SelectTrigger>
                                        <SelectValue placeholder="포지션을 선택하세요" />
                                    </SelectTrigger>
                                    <SelectContent>
                                        {formInfo?.positions?.map((position) => (
                                            <SelectItem key={position} value={position}>
                                                {position}
                                            </SelectItem>
                                        )) || [
                                            '웹 플랫폼 개발자', '프론트엔드 개발자', '백엔드 개발자',
                                            '풀스택 개발자', '게임 클라이언트 개발자', '게임 서버 개발자'
                                        ].map((position) => (
                                            <SelectItem key={position} value={position}>
                                                {position}
                                            </SelectItem>
                                        ))}
                                    </SelectContent>
                                </Select>
                            </div>

                            <div className="space-y-2">
                                <label className="text-sm font-medium">경력 수준 *</label>
                                <Select
                                    value={formData.experienceLevel}
                                    onValueChange={(value: 'JUNIOR' | 'MIDDLE' | 'SENIOR' | 'LEAD') =>
                                        setFormData({...formData, experienceLevel: value})
                                    }
                                >
                                    <SelectTrigger>
                                        <SelectValue placeholder="경력을 선택하세요" />
                                    </SelectTrigger>
                                    <SelectContent>
                                        <SelectItem value="JUNIOR">신입</SelectItem>
                                        <SelectItem value="MIDDLE">경력 3-5년</SelectItem>
                                        <SelectItem value="SENIOR">경력 5년 이상</SelectItem>
                                        <SelectItem value="LEAD">팀 리드</SelectItem>
                                    </SelectContent>
                                </Select>
                            </div>

                            <div className="space-y-2">
                                <label className="text-sm font-medium">고용 형태 *</label>
                                <Select
                                    value={formData.jobType}
                                    onValueChange={(value: 'FULL_TIME' | 'CONTRACT' | 'FREELANCE' | 'INTERN') =>
                                        setFormData({...formData, jobType: value})
                                    }
                                >
                                    <SelectTrigger>
                                        <SelectValue placeholder="고용 형태를 선택하세요" />
                                    </SelectTrigger>
                                    <SelectContent>
                                        <SelectItem value="FULL_TIME">정규직</SelectItem>
                                        <SelectItem value="CONTRACT">계약직</SelectItem>
                                        <SelectItem value="FREELANCE">프리랜서</SelectItem>
                                        <SelectItem value="INTERN">인턴</SelectItem>
                                    </SelectContent>
                                </Select>
                            </div>
                        </div>
                    </CardContent>
                </Card>

                {/* 기술 스택 */}
                <Card>
                    <CardHeader>
                        <CardTitle className="flex items-center">
                            <Code className="h-5 w-5 mr-2" />
                            기술 스택 & 포트폴리오
                        </CardTitle>
                    </CardHeader>
                    <CardContent className="space-y-4">
                        {/* 기술 스택 */}
                        <div className="space-y-2">
                            <label className="text-sm font-medium">보유 기술 *</label>
                            <div className="flex space-x-2">
                                <Input
                                    placeholder="기술 추가 (예: React)"
                                    value={newSkill}
                                    onChange={(e) => setNewSkill(e.target.value)}
                                    onKeyPress={(e) => e.key === 'Enter' && (e.preventDefault(), addSkill())}
                                />
                                <Button type="button" onClick={addSkill} size="sm">
                                    <Plus className="h-4 w-4" />
                                </Button>
                            </div>
                            <div className="flex flex-wrap gap-2 mt-2">
                                {skills.map((skill, index) => (
                                    <Badge
                                        key={index}
                                        variant="secondary"
                                        className="text-sm px-3 py-1 flex items-center space-x-1"
                                    >
                                        <span>{skill}</span>
                                        <button
                                            type="button"
                                            onClick={() => removeSkill(skill)}
                                            className="ml-2 hover:text-destructive"
                                        >
                                            <X className="h-3 w-3" />
                                        </button>
                                    </Badge>
                                ))}
                            </div>
                        </div>

                        {/* 포트폴리오 URL */}
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div className="space-y-2">
                                <label className="text-sm font-medium">포트폴리오 URL</label>
                                <Input
                                    value={formData.portfolioUrl}
                                    onChange={(e) => setFormData({...formData, portfolioUrl: e.target.value})}
                                    placeholder="https://your-portfolio.com"
                                />
                            </div>

                            <div className="space-y-2">
                                <label className="text-sm font-medium">GitHub URL</label>
                                <Input
                                    value={formData.githubUrl}
                                    onChange={(e) => setFormData({...formData, githubUrl: e.target.value})}
                                    placeholder="https://github.com/username"
                                />
                            </div>
                        </div>
                    </CardContent>
                </Card>

                {/* 지원 동기 */}
                <Card>
                    <CardHeader>
                        <CardTitle>지원 동기 & 자기소개</CardTitle>
                        <CardDescription>
                            채용 담당자가 가장 중요하게 보는 부분입니다. 구체적이고 진솔하게 작성해주세요.
                        </CardDescription>
                    </CardHeader>
                    <CardContent className="space-y-4">
                        <div className="space-y-2">
                            <label className="text-sm font-medium">지원 동기 *</label>
                            <Textarea
                                value={formData.coverLetter}
                                onChange={(e) => setFormData({...formData, coverLetter: e.target.value})}
                                placeholder="해당 회사에 지원하는 이유와 입사 후 목표를 구체적으로 작성해주세요"
                                rows={6}
                                required
                            />
                            <p className="text-sm text-muted-foreground">
                                {formData.coverLetter.length}/1000자 (최소 50자)
                            </p>
                        </div>
                    </CardContent>
                </Card>

                {/* 추가 정보 */}
                <Card>
                    <CardHeader>
                        <CardTitle>추가 정보</CardTitle>
                    </CardHeader>
                    <CardContent className="space-y-4">
                        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                            <div className="space-y-2">
                                <label className="text-sm font-medium">희망 연봉</label>
                                <Input
                                    value={formData.expectedSalary}
                                    onChange={(e) => setFormData({...formData, expectedSalary: e.target.value})}
                                    placeholder="예: 5000만원, 협의"
                                />
                            </div>

                            <div className="space-y-2">
                                <label className="text-sm font-medium">입사 가능일</label>
                                <Input
                                    type="date"
                                    value={formData.availableStartDate}
                                    onChange={(e) => setFormData({...formData, availableStartDate: e.target.value})}
                                />
                            </div>

                            <div className="space-y-2">
                                <label className="text-sm font-medium">희망 근무지</label>
                                <Input
                                    value={formData.workLocation}
                                    onChange={(e) => setFormData({...formData, workLocation: e.target.value})}
                                    placeholder="서울, 판교, 재택근무 등"
                                />
                            </div>
                        </div>
                    </CardContent>
                </Card>

                {/* 제출 버튼 */}
                <div className="flex justify-between">
                    <Button
                        type="button"
                        variant="outline"
                        onClick={() => router.back()}
                    >
                        취소
                    </Button>

                    <Button
                        type="submit"
                        disabled={isCreatingApplication}
                        className="min-w-[120px]"
                    >
                        {isCreatingApplication ? (
                            <div className="flex items-center">
                                <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
                                제출 중...
                            </div>
                        ) : (
                            <>
                                <Send className="h-4 w-4 mr-2" />
                                지원서 제출
                            </>
                        )}
                    </Button>
                </div>
            </form>
        </div>
    )
}