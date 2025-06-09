'use client'

import { useState, useEffect } from 'react'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Loader2, Github, TrendingUp } from 'lucide-react'

interface TechStack {
    name: string
    category: string
    popularityScore: number
    repositoryCount: number
    description?: string
}

interface TechStackSelectorProps {
    selectedStacks: string[]
    onSelectionChange: (stacks: string[]) => void
}

export function TechStackSelector({ selectedStacks, onSelectionChange }: TechStackSelectorProps) {
    const [trendingStacks, setTrendingStacks] = useState<TechStack[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)
    const [lastUpdated, setLastUpdated] = useState<string>('')

    useEffect(() => {
        fetchTrendingStacks()
    }, [])

    const fetchTrendingStacks = async () => {
        try {
            setLoading(true)
            setError(null)

            const response = await fetch('/api/tech-stacks/trending', {
                credentials: 'include'
            })

            const data = await response.json()

            if (data.success) {
                setTrendingStacks(data.flatData || [])
                setLastUpdated(new Date(data.lastUpdated).toLocaleString())
            } else {
                setError(data.message)
                // 실패 시 fallback 데이터 사용
                if (data.fallbackData) {
                    setTrendingStacks(data.fallbackData)
                }
            }
        } catch (err) {
            setError('기술 스택 조회에 실패했습니다')
            console.error('Tech stack fetch error:', err)
        } finally {
            setLoading(false)
        }
    }

    const toggleStack = (stackName: string) => {
        const newSelection = selectedStacks.includes(stackName)
            ? selectedStacks.filter(s => s !== stackName)
            : [...selectedStacks, stackName]

        onSelectionChange(newSelection)
    }

    const getCategoryIcon = (category: string) => {
        switch (category) {
            case 'FRONTEND': return '🎨'
            case 'BACKEND': return '⚙️'
            case 'DATABASE': return '🗄️'
            case 'DEVOPS': return '🚀'
            case 'MOBILE': return '📱'
            case 'GAME': return '🎮'
            default: return '💻'
        }
    }

    const getCategoryColor = (category: string) => {
        switch (category) {
            case 'FRONTEND': return 'bg-blue-100 text-blue-800'
            case 'BACKEND': return 'bg-green-100 text-green-800'
            case 'DATABASE': return 'bg-purple-100 text-purple-800'
            case 'DEVOPS': return 'bg-orange-100 text-orange-800'
            case 'MOBILE': return 'bg-pink-100 text-pink-800'
            case 'GAME': return 'bg-red-100 text-red-800'
            default: return 'bg-gray-100 text-gray-800'
        }
    }

    if (loading) {
        return (
            <Card>
                <CardContent className="p-6">
                    <div className="flex items-center justify-center space-x-2">
                        <Loader2 className="h-5 w-5 animate-spin" />
                        <span>GitHub에서 트렌딩 기술 스택 조회 중...</span>
                    </div>
                </CardContent>
            </Card>
        )
    }

    return (
        <Card>
            <CardHeader>
                <CardTitle className="flex items-center space-x-2">
                    <Github className="h-5 w-5" />
                    <span>🔥 실시간 트렌딩 기술 스택</span>
                    <TrendingUp className="h-4 w-4 text-green-600" />
                </CardTitle>
                <p className="text-sm text-muted-foreground">
                    GitHub ⭐ 수 기반 • 마지막 업데이트: {lastUpdated}
                </p>
                {error && (
                    <p className="text-sm text-red-600">
                        ⚠️ {error} (기본 데이터로 표시)
                    </p>
                )}
            </CardHeader>

            <CardContent className="space-y-4">
                <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-3">
                    {trendingStacks.slice(0, 20).map((stack) => (
                        <Button
                            key={stack.name}
                            variant={selectedStacks.includes(stack.name) ? "default" : "outline"}
                            size="sm"
                            onClick={() => toggleStack(stack.name)}
                            className="relative h-auto p-3 flex flex-col items-start"
                        >
                            <div className="flex items-center space-x-1 mb-1">
                                <span>{getCategoryIcon(stack.category)}</span>
                                <span className="font-medium">{stack.name}</span>
                            </div>

                            <div className="flex items-center space-x-2">
                                <Badge variant="secondary" className={`text-xs ${getCategoryColor(stack.category)}`}>
                                    {stack.category}
                                </Badge>
                                <Badge variant="outline" className="text-xs">
                                    ⭐ {stack.popularityScore > 1000 ? `${Math.round(stack.popularityScore / 1000)}K` : stack.popularityScore}
                                </Badge>
                            </div>

                            {stack.description && (
                                <p className="text-xs text-muted-foreground mt-1 text-left">
                                    {stack.description}
                                </p>
                            )}
                        </Button>
                    ))}
                </div>

                {selectedStacks.length > 0 && (
                    <div className="mt-6 p-4 bg-blue-50 rounded-lg border border-blue-200">
                        <h4 className="font-medium text-blue-900 mb-2">
                            선택된 기술 스택 ({selectedStacks.length}개)
                        </h4>
                        <div className="flex flex-wrap gap-2">
                            {selectedStacks.map(stack => (
                                <Badge key={stack} variant="default" className="cursor-pointer" onClick={() => toggleStack(stack)}>
                                    {stack} ✕
                                </Badge>
                            ))}
                        </div>
                        <p className="text-xs text-blue-700 mt-2">
                            💡 선택한 기술들이 지원서에 자동으로 포함됩니다
                        </p>
                    </div>
                )}

                <div className="mt-4 pt-4 border-t">
                    <Button
                        variant="ghost"
                        size="sm"
                        onClick={fetchTrendingStacks}
                        className="text-sm"
                    >
                        🔄 최신 트렌드 다시 불러오기
                    </Button>
                </div>
            </CardContent>
        </Card>
    )
}