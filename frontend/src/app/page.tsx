import Link from 'next/link'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import { ArrowRight, Code, Users, Zap, Trophy, Star, ChevronRight } from 'lucide-react'

export default function HomePage() {
  const features = [
    {
      icon: Code,
      title: '포트폴리오 관리',
      description: '프로젝트와 기술 스택을 체계적으로 관리하고 showcase하세요'
    },
    {
      icon: Users,
      title: '실시간 매칭',
      description: '게임 회사의 요구사항과 개발자의 스킬을 실시간으로 매칭'
    },
    {
      icon: Zap,
      title: '빠른 지원',
      description: '원클릭 지원으로 번거로운 절차 없이 빠르게 지원 완료'
    },
    {
      icon: Trophy,
      title: '스킬 인증',
      description: '코딩 테스트와 프로젝트 리뷰를 통한 검증된 실력 증명'
    }
  ]

  const companies = [
    '카카오게임즈', 'NCSoft', 'Krafton', 'Pearl Abyss', 'Smilegate', 'Nexon'
  ]

  const jobOpenings = [
    {
      company: '카카오게임즈',
      position: '웹 플랫폼 개발자',
      location: '판교',
      type: '정규직',
      skills: ['React', 'TypeScript', 'Node.js', 'AWS'],
      isHot: true
    },
    {
      company: 'Krafton',
      position: '게임 클라이언트 개발자',
      location: '서울',
      type: '정규직',
      skills: ['Unity', 'C#', 'C++'],
      isHot: false
    },
    {
      company: 'Pearl Abyss',
      position: '백엔드 개발자',
      location: '안양',
      type: '정규직',
      skills: ['Java', 'Spring', 'MSA', 'Redis'],
      isHot: true
    }
  ]

  return (
      <div className="flex flex-col">
        {/* Hero Section */}
        <section className="relative py-20 lg:py-32 bg-gradient-to-br from-primary/5 via-purple-50/30 to-background">
          <div className="container">
            <div className="mx-auto max-w-4xl text-center">
              <Badge variant="secondary" className="mb-6">
                게임 업계 No.1 개발자 플랫폼
              </Badge>

              <h1 className="text-4xl lg:text-6xl font-bold tracking-tight mb-6">
                게임 개발자의 꿈이 현실이 되는 곳
              </h1>

              <div className="text-center max-w-3xl mx-auto mb-8">
                <p className="text-xl text-muted-foreground mb-3">
                  국내 최고의 게임 회사들과 재능있는 개발자들을 연결하는 플랫폼입니다.
                </p>
                <p className="text-lg text-muted-foreground/80">
                  당신의 게임 개발 여정을 함께 시작하세요.
                </p>
              </div>

              <div className="flex flex-col sm:flex-row gap-4 justify-center">
                <Button asChild size="lg" className="text-lg">
                  <Link href="/auth/login">
                    지금 시작하기
                    <ArrowRight className="ml-2 h-5 w-5"/>
                  </Link>
                </Button>

                <Button asChild variant="outline" size="lg" className="text-lg">
                  <Link href="/positions">
                    채용공고 보기
                    <ChevronRight className="ml-2 h-5 w-5"/>
                  </Link>
                </Button>
              </div>
            </div>
          </div>
        </section>

        {/* Features Section */}
        <section className="py-20 bg-background">
          <div className="container">
            <div className="text-center mb-16">
              <h2 className="text-3xl lg:text-4xl font-bold mb-4">
                왜 GameCraft Studios를 선택해야 할까요?
              </h2>
              <p className="text-lg text-muted-foreground max-w-2xl mx-auto">
                게임 개발자들을 위해 특별히 설계된 기능들로 더 나은 커리어를 만들어보세요
              </p>
            </div>

            <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-6">
              {features.map((feature, index) => {
                const Icon = feature.icon
                return (
                    <Card key={index} className="border-2 hover:border-primary/20 transition-colors">
                      <CardHeader>
                        <div className="w-12 h-12 bg-primary/10 rounded-lg flex items-center justify-center mb-4">
                          <Icon className="h-6 w-6 text-primary" />
                        </div>
                        <CardTitle className="text-xl">{feature.title}</CardTitle>
                      </CardHeader>
                      <CardContent>
                        <CardDescription className="text-base">
                          {feature.description}
                        </CardDescription>
                      </CardContent>
                    </Card>
                )
              })}
            </div>
          </div>
        </section>

        {/* Companies Section */}
        <section className="py-20 bg-muted/30">
          <div className="container">
            <div className="text-center mb-16">
              <h2 className="text-3xl lg:text-4xl font-bold mb-4">
                국내 최고의 게임 회사들이 함께합니다
              </h2>
              <p className="text-lg text-muted-foreground">
                검증된 게임 회사들에서 당신을 기다리고 있습니다
              </p>
            </div>

            <div className="flex flex-wrap justify-center items-center gap-8 opacity-60">
              {companies.map((company, index) => (
                  <div
                      key={index}
                      className="text-2xl font-bold text-muted-foreground hover:text-primary transition-colors cursor-pointer"
                  >
                    {company}
                  </div>
              ))}
            </div>
          </div>
        </section>

        {/* Job Openings Preview */}
        <section className="py-20 bg-background">
          <div className="container">
            <div className="text-center mb-16">
              <h2 className="text-3xl lg:text-4xl font-bold mb-4">
                지금 열린 포지션들
              </h2>
              <p className="text-lg text-muted-foreground">
                당신에게 perfect match인 포지션을 찾아보세요
              </p>
            </div>

            <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6 mb-12">
              {jobOpenings.map((job, index) => (
                  <Card key={index} className="hover:shadow-lg transition-shadow">
                    <CardHeader>
                      <div className="flex items-start justify-between">
                        <div>
                          <CardTitle className="text-lg">{job.position}</CardTitle>
                          <CardDescription className="text-base font-medium text-primary">
                            {job.company}
                          </CardDescription>
                        </div>
                        {job.isHot && (
                            <Badge variant="destructive" className="shrink-0">
                              HOT
                            </Badge>
                        )}
                      </div>
                    </CardHeader>
                    <CardContent>
                      <div className="space-y-3">
                        <div className="flex items-center text-sm text-muted-foreground">
                          <span>{job.location} • {job.type}</span>
                        </div>
                        <div className="flex flex-wrap gap-2">
                          {job.skills.map((skill, skillIndex) => (
                              <Badge key={skillIndex} variant="secondary">
                                {skill}
                              </Badge>
                          ))}
                        </div>
                      </div>
                    </CardContent>
                  </Card>
              ))}
            </div>

            <div className="text-center">
              <Button asChild size="lg" variant="outline">
                <Link href="/positions">
                  모든 채용공고 보기
                  <ArrowRight className="ml-2 h-5 w-5" />
                </Link>
              </Button>
            </div>
          </div>
        </section>

        {/* CTA Section */}
        <section className="py-20 bg-primary text-primary-foreground">
          <div className="container">
            <div className="text-center max-w-3xl mx-auto">
              <h2 className="text-3xl lg:text-4xl font-bold mb-6">
                당신의 게임 개발 여정을 시작하세요
              </h2>
              <p className="text-lg mb-8 opacity-90">
                지금 가입하고 국내 최고의 게임 회사들과 연결되어 보세요.
                새로운 기회가 당신을 기다리고 있습니다.
              </p>
              <Button asChild size="lg" variant="secondary" className="text-lg">
                <Link href="/auth/login">
                  무료로 시작하기
                  <ArrowRight className="ml-2 h-5 w-5" />
                </Link>
              </Button>
            </div>
          </div>
        </section>
      </div>
  )
}