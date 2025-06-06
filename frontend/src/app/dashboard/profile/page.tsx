// frontend/src/app/dashboard/profile/page.tsx
'use client'

import { useState } from 'react'
import { useAuthStore } from '@/store/authStore'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'
import { Badge } from '@/components/ui/badge'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { Separator } from '@/components/ui/separator'
import { Camera, Plus, X, Save, User, Code, Briefcase, GraduationCap } from 'lucide-react'
import { toast } from 'sonner'

export default function ProfilePage() {
    const { user, updateUser } = useAuthStore()
    const [isEditing, setIsEditing] = useState(false)
    const [formData, setFormData] = useState({
        nickname: user?.nickname || '',
        email: user?.email || '',
        bio: '열정적인 게임 개발자입니다. 사용자 경험을 중시하는 프론트엔드 개발과 안정적인 백엔드 시스템 구축에 관심이 많습니다.',
        location: '서울, 대한민국',
        website: 'https://github.com/JUYOUNG34',
        phone: '010-1234-5678'
    })

    const [skills, setSkills] = useState([
        'React', 'TypeScript', 'Next.js', 'Node.js', 'Express.js',
        'PostgreSQL', 'AWS', 'Git', 'JavaScript', 'Tailwind CSS'
    ])
    const [newSkill, setNewSkill] = useState('')

    const [experiences] = useState([
        {
            company: 'Previous Company',
            position: '프론트엔드 개발자',
            period: '2022.03 - 2024.12',
            description: 'React 기반 웹 애플리케이션 개발 및 유지보수'
        }
    ])

    const [education] = useState([
        {
            school: '한국대학교',
            major: '컴퓨터공학과',
            period: '2018.03 - 2022.02',
            degree: '학사'
        }
    ])

    const handleSave = () => {
        updateUser({
            nickname: formData.nickname,
            email: formData.email
        })

        toast.success('프로필이 성공적으로 업데이트되었습니다!')
        setIsEditing(false)
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

    return (
        <div className="container py-8 max-w-4xl">
            <div className="flex items-center justify-between mb-8">
                <div>
                    <h1 className="text-3xl font-bold">프로필 설정</h1>
                    <p className="text-muted-foreground">
                        나의 정보를 관리하고 채용담당자에게 어필하세요
                    </p>
                </div>

                <Button
                    onClick={isEditing ? handleSave : () => setIsEditing(true)}
                    className="min-w-[100px]"
                >
                    {isEditing ? (
                        <>
                            <Save className="h-4 w-4 mr-2" />
                            저장
                        </>
                    ) : (
                        '편집하기'
                    )}
                </Button>
            </div>

            <div className="space-y-8">
                {/* 기본 정보 */}
                <Card>
                    <CardHeader>
                        <CardTitle className="flex items-center">
                            <User className="h-5 w-5 mr-2" />
                            기본 정보
                        </CardTitle>
                        <CardDescription>
                            기본적인 개인 정보를 입력해주세요
                        </CardDescription>
                    </CardHeader>
                    <CardContent className="space-y-6">
                        {/* 프로필 이미지 */}
                        <div className="flex items-center space-x-4">
                            <Avatar className="h-20 w-20">
                                <AvatarImage src={user?.profileImage} alt={user?.nickname} />
                                <AvatarFallback className="text-xl">
                                    {user?.nickname?.charAt(0).toUpperCase()}
                                </AvatarFallback>
                            </Avatar>
                            <div className="space-y-2">
                                <Button variant="outline" size="sm">
                                    <Camera className="h-4 w-4 mr-2" />
                                    사진 변경
                                </Button>
                                <p className="text-sm text-muted-foreground">
                                    JPG, PNG 파일만 업로드 가능합니다
                                </p>
                            </div>
                        </div>

                        <Separator />

                        {/* 기본 정보 폼 */}
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div className="space-y-2">
                                <Label htmlFor="nickname">닉네임</Label>
                                <Input
                                    id="nickname"
                                    value={formData.nickname}
                                    onChange={(e) => setFormData({...formData, nickname: e.target.value})}
                                    disabled={!isEditing}
                                />
                            </div>

                            <div className="space-y-2">
                                <Label htmlFor="email">이메일</Label>
                                <Input
                                    id="email"
                                    type="email"
                                    value={formData.email}
                                    onChange={(e) => setFormData({...formData, email: e.target.value})}
                                    disabled={!isEditing}
                                />
                            </div>

                            <div className="space-y-2">
                                <Label htmlFor="phone">연락처</Label>
                                <Input
                                    id="phone"
                                    value={formData.phone}
                                    onChange={(e) => setFormData({...formData, phone: e.target.value})}
                                    disabled={!isEditing}
                                />
                            </div>

                            <div className="space-y-2">
                                <Label htmlFor="location">지역</Label>
                                <Input
                                    id="location"
                                    value={formData.location}
                                    onChange={(e) => setFormData({...formData, location: e.target.value})}
                                    disabled={!isEditing}
                                />
                            </div>

                            <div className="space-y-2 md:col-span-2">
                                <Label htmlFor="website">웹사이트/포트폴리오</Label>
                                <Input
                                    id="website"
                                    value={formData.website}
                                    onChange={(e) => setFormData({...formData, website: e.target.value})}
                                    disabled={!isEditing}
                                    placeholder="https://github.com/username"
                                />
                            </div>

                            <div className="space-y-2 md:col-span-2">
                                <Label htmlFor="bio">자기소개</Label>
                                <Textarea
                                    id="bio"
                                    value={formData.bio}
                                    onChange={(e) => setFormData({...formData, bio: e.target.value})}
                                    disabled={!isEditing}
                                    rows={4}
                                    className="resize-none"
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
                            기술 스택
                        </CardTitle>
                        <CardDescription>
                            보유하고 있는 기술들을 추가해주세요
                        </CardDescription>
                    </CardHeader>
                    <CardContent>
                        <div className="space-y-4">
                            {/* 스킬 추가 */}
                            <div className="flex space-x-2">
                                <Input
                                    placeholder="새 기술 입력 (예: React)"
                                    value={newSkill}
                                    onChange={(e) => setNewSkill(e.target.value)}
                                    onKeyPress={(e) => e.key === 'Enter' && addSkill()}
                                />
                                <Button onClick={addSkill} size="sm">
                                    <Plus className="h-4 w-4" />
                                </Button>
                            </div>

                            {/* 스킬 목록 */}
                            <div className="flex flex-wrap gap-2">
                                {skills.map((skill, index) => (
                                    <Badge
                                        key={index}
                                        variant="secondary"
                                        className="text-sm px-3 py-1 flex items-center space-x-1"
                                    >
                                        <span>{skill}</span>
                                        <button
                                            onClick={() => removeSkill(skill)}
                                            className="ml-2 hover:text-destructive"
                                        >
                                            <X className="h-3 w-3" />
                                        </button>
                                    </Badge>
                                ))}
                            </div>
                        </div>
                    </CardContent>
                </Card>

                {/* 경력 */}
                <Card>
                    <CardHeader>
                        <CardTitle className="flex items-center">
                            <Briefcase className="h-5 w-5 mr-2" />
                            경력 사항
                        </CardTitle>
                        <CardDescription>
                            업무 경험을 추가해주세요
                        </CardDescription>
                    </CardHeader>
                    <CardContent>
                        <div className="space-y-4">
                            {experiences.map((exp, index) => (
                                <div key={index} className="border rounded-lg p-4">
                                    <div className="flex justify-between items-start mb-2">
                                        <div>
                                            <h3 className="font-semibold">{exp.position}</h3>
                                            <p className="text-muted-foreground">{exp.company}</p>
                                        </div>
                                        <Badge variant="outline">{exp.period}</Badge>
                                    </div>
                                    <p className="text-sm text-muted-foreground">{exp.description}</p>
                                </div>
                            ))}

                            <Button variant="outline" className="w-full">
                                <Plus className="h-4 w-4 mr-2" />
                                경력 추가
                            </Button>
                        </div>
                    </CardContent>
                </Card>

                {/* 학력 */}
                <Card>
                    <CardHeader>
                        <CardTitle className="flex items-center">
                            <GraduationCap className="h-5 w-5 mr-2" />
                            학력 사항
                        </CardTitle>
                        <CardDescription>
                            교육 배경을 추가해주세요
                        </CardDescription>
                    </CardHeader>
                    <CardContent>
                        <div className="space-y-4">
                            {education.map((edu, index) => (
                                <div key={index} className="border rounded-lg p-4">
                                    <div className="flex justify-between items-start mb-2">
                                        <div>
                                            <h3 className="font-semibold">{edu.school}</h3>
                                            <p className="text-muted-foreground">{edu.major}</p>
                                        </div>
                                        <div className="text-right">
                                            <Badge variant="outline">{edu.period}</Badge>
                                            <p className="text-sm text-muted-foreground mt-1">{edu.degree}</p>
                                        </div>
                                    </div>
                                </div>
                            ))}

                            <Button variant="outline" className="w-full">
                                <Plus className="h-4 w-4 mr-2" />
                                학력 추가
                            </Button>
                        </div>
                    </CardContent>
                </Card>
            </div>
        </div>
    )
}