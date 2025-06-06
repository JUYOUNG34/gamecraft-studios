// frontend/src/app/dashboard/applications/new/page.tsx (간단 버전)
'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'
import { useAuthStore } from '@/store/authStore'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Textarea } from '@/components/ui/textarea'
import { Badge } from '@/components/ui/badge'
import {  Send, Plus, X, Briefcase, Code, User } from 'lucide-react'
import { toast } from 'sonner'

export default function NewApplicationPage() {
    const router = useRouter()
    const { user } = useAuthStore()

    const [formData, setFormData] = useState({
        // 기본 정보
        position: '웹 플랫폼 개발자',
        company: '카카오게임즈',
        department: '플랫폼개발팀',

        // 개인 정보
        name: user?.nickname || '',
        email: user?.email || '',
        phone: '',
        location: '서울, 대한민국',

        // 경력 정보
        experience: '2-3년',
        currentCompany: '',
        currentPosition: '',
        salary: '협의',

        // 포트폴리오
        portfolioUrl: '',
        githubUrl: '',

        // 지원 동기
        motivation: '',
        strengths: '',
        projectExperience: '',

        // 추가 정보
        availability: '즉시 가능',
        militaryService: '완료'
    })

    const [skills, setSkills] = useState([
        'React', 'TypeScript', 'Next.js', 'Node.js'
    ])
    const [newSkill, setNewSkill] = useState('')
    const [isSubmitting, setIsSubmitting] = useState(false)

    const companies = [
        '카카오게임즈', 'Krafton', 'NCSoft', 'Pearl Abyss',
        'Smilegate', 'Nexon', 'Netmarble', 'Com2uS'
    ]

    const positions = [
        '웹 플랫폼 개발자', '프론트엔드 개발자', '백엔드 개발자',
        '풀스택 개발자', '게임 클라이언트 개발자', '게임 서버 개발자'
    ]

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
        setIsSubmitting(true)

        try {
            // 폼 검증
            if (!formData.motivation.trim()) {
                toast.error('지원 동기를 입력해주세요')
                return
            }

            if (!formData.strengths.trim()) {
                toast.error('강점 및 역량을 입력해주세요')
                return
            }

            // 가상의 API 호출
            await new Promise(resolve => setTimeout(resolve, 2000))

            toast.success('지원서가 성공적으로 제출되었습니다!')
            router.push('/dashboard/applications')

        } catch (error) {
            toast.error('지원서 제출 중 오류가 발생했습니다')
        } finally {
            setIsSubmitting(false)
        }
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
                                <label className="text-sm font-medium">회사명</label>
                                <select
                                    value={formData.company}
                                    onChange={(e) => setFormData({...formData, company: e.target.value})}
                                    className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring disabled:cursor-not-allowed disabled:opacity-50"
                                >
                                    {companies.map((company) => (
                                        <option key={company} value={company}>
                                            {company}
                                        </option>
                                    ))}
                                </select>
                            </div>

                            <div className="space-y-2">
                                <label className="text-sm font-medium">지원 포지션</label>
                                <select
                                    value={formData.position}
                                    onChange={(e) => setFormData({...formData, position: e.target.value})}
                                    className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring disabled:cursor-not-allowed disabled:opacity-50"
                                >
                                    {positions.map((position) => (
                                        <option key={position} value={position}>
                                            {position}
                                        </option>
                                    ))}
                                </select>
                            </div>

                            <div className="space-y-2 md:col-span-2">
                                <label className="text-sm font-medium">희망 부서/팀</label>
                                <Input
                                    value={formData.department}
                                    onChange={(e) => setFormData({...formData, department: e.target.value})}
                                    placeholder="예: 플랫폼개발팀, 웹서비스팀"
                                />
                            </div>
                        </div>
                    </CardContent>
                </Card>

                {/* 개인 정보 */}
                <Card>
                    <CardHeader>
                        <CardTitle className="flex items-center">
                            <User className="h-5 w-5 mr-2" />
                            개인 정보
                        </CardTitle>
                    </CardHeader>
                    <CardContent>
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div className="space-y-2">
                                <label className="text-sm font-medium">이름 *</label>
                                <Input
                                    value={formData.name}
                                    onChange={(e) => setFormData({...formData, name: e.target.value})}
                                    required
                                />
                            </div>

                            <div className="space-y-2">
                                <label className="text-sm font-medium">이메일 *</label>
                                <Input
                                    type="email"
                                    value={formData.email}
                                    onChange={(e) => setFormData({...formData, email: e.target.value})}
                                    required
                                />
                            </div>

                            <div className="space-y-2">
                                <label className="text-sm font-medium">연락처 *</label>
                                <Input
                                    value={formData.phone}
                                    onChange={(e) => setFormData({...formData, phone: e.target.value})}
                                    placeholder="010-1234-5678"
                                    required
                                />
                            </div>

                            <div className="space-y-2">
                                <label className="text-sm font-medium">거주지</label>
                                <Input
                                    value={formData.location}
                                    onChange={(e) => setFormData({...formData, location: e.target.value})}
                                />
                            </div>
                        </div>
                    </CardContent>
                </Card>

                {/* 경력 정보 */}
                <Card>
                    <CardHeader>
                        <CardTitle>경력 정보</CardTitle>
                    </CardHeader>
                    <CardContent>
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div className="space-y-2">
                                <label className="text-sm font-medium">총 경력</label>
                                <select
                                    value={formData.experience}
                                    onChange={(e) => setFormData({...formData, experience: e.target.value})}
                                    className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring disabled:cursor-not-allowed disabled:opacity-50"
                                >
                                    <option value="신입">신입</option>
                                    <option value="1년 미만">1년 미만</option>
                                    <option value="1-2년">1-2년</option>
                                    <option value="2-3년">2-3년</option>
                                    <option value="3-5년">3-5년</option>
                                    <option value="5-10년">5-10년</option>
                                    <option value="10년 이상">10년 이상</option>
                                </select>
                            </div>

                            <div className="space-y-2">
                                <label className="text-sm font-medium">희망 연봉</label>
                                <Input
                                    value={formData.salary}
                                    onChange={(e) => setFormData({...formData, salary: e.target.value})}
                                    placeholder="예: 5000만원, 협의"
                                />
                            </div>

                            <div className="space-y-2">
                                <label className="text-sm font-medium">현재 회사</label>
                                <Input
                                    value={formData.currentCompany}
                                    onChange={(e) => setFormData({...formData, currentCompany: e.target.value})}
                                    placeholder="현재 재직 중인 회사명"
                                />
                            </div>

                            <div className="space-y-2">
                                <label className="text-sm font-medium">현재 직책</label>
                                <Input
                                    value={formData.currentPosition}
                                    onChange={(e) => setFormData({...formData, currentPosition: e.target.value})}
                                    placeholder="현재 직책/포지션"
                                />
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
                            <label className="text-sm font-medium">보유 기술</label>
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
                                value={formData.motivation}
                                onChange={(e) => setFormData({...formData, motivation: e.target.value})}
                                placeholder="카카오게임즈에 지원하는 이유와 입사 후 목표를 구체적으로 작성해주세요"
                                rows={5}
                                required
                            />
                            <p className="text-sm text-muted-foreground">
                                {formData.motivation.length}/1000자
                            </p>
                        </div>

                        <div className="space-y-2">
                            <label className="text-sm font-medium">강점 및 역량 *</label>
                            <Textarea
                                value={formData.strengths}
                                onChange={(e) => setFormData({...formData, strengths: e.target.value})}
                                placeholder="본인의 강점과 게임 개발에 관련된 역량을 구체적으로 설명해주세요"
                                rows={4}
                                required
                            />
                        </div>

                        <div className="space-y-2">
                            <label className="text-sm font-medium">주요 프로젝트 경험</label>
                            <Textarea
                                value={formData.projectExperience}
                                onChange={(e) => setFormData({...formData, projectExperience: e.target.value})}
                                placeholder="가장 인상 깊었던 프로젝트나 성과를 상세히 설명해주세요"
                                rows={4}
                            />
                        </div>
                    </CardContent>
                </Card>

                {/* 추가 정보 */}
                <Card>
                    <CardHeader>
                        <CardTitle>추가 정보</CardTitle>
                    </CardHeader>
                    <CardContent className="space-y-4">
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div className="space-y-2">
                                <label className="text-sm font-medium">입사 가능일</label>
                                <select
                                    value={formData.availability}
                                    onChange={(e) => setFormData({...formData, availability: e.target.value})}
                                    className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring disabled:cursor-not-allowed disabled:opacity-50"
                                >
                                    <option value="즉시 가능">즉시 가능</option>
                                    <option value="1개월 후">1개월 후</option>
                                    <option value="2개월 후">2개월 후</option>
                                    <option value="3개월 후">3개월 후</option>
                                    <option value="협의">협의</option>
                                </select>
                            </div>

                            <div className="space-y-2">
                                <label className="text-sm font-medium">병역 사항</label>
                                <select
                                    value={formData.militaryService}
                                    onChange={(e) => setFormData({...formData, militaryService: e.target.value})}
                                    className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring disabled:cursor-not-allowed disabled:opacity-50"
                                >
                                    <option value="완료">완료</option>
                                    <option value="면제">면제</option>
                                    <option value="미필">미필</option>
                                    <option value="해당없음">해당없음</option>
                                </select>
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
                        disabled={isSubmitting}
                        className="min-w-[120px]"
                    >
                        {isSubmitting ? (
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