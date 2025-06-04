// frontend/src/components/layout/Footer.tsx
import Link from 'next/link'
import { Gamepad2, Github, Mail, MapPin } from 'lucide-react'

export function Footer() {
    const currentYear = new Date().getFullYear()

    const footerLinks = {
        product: [
            { name: '채용공고', href: '/positions' },
            { name: '개발자 목록', href: '/developers' },
            { name: '기업 서비스', href: '/enterprise' },
            { name: '요금제', href: '/pricing' }
        ],
        support: [
            { name: '도움말', href: '/help' },
            { name: 'FAQ', href: '/faq' },
            { name: '문의하기', href: '/contact' },
            { name: '상태 페이지', href: '/status' }
        ],
        company: [
            { name: '회사 소개', href: '/about' },
            { name: '블로그', href: '/blog' },
            { name: '채용', href: '/careers' },
            { name: '언론보도', href: '/press' }
        ],
        legal: [
            { name: '서비스 약관', href: '/terms' },
            { name: '개인정보 처리방침', href: '/privacy' },
            { name: '쿠키 정책', href: '/cookies' },
            { name: '운영정책', href: '/policy' }
        ]
    }

    return (
        <footer className="bg-gray-50 border-t">
            <div className="container py-12">
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-6 gap-8">
                    {/* 브랜드 섹션 */}
                    <div className="lg:col-span-2">
                        <Link href="/" className="flex items-center space-x-2 mb-4">
                            <Gamepad2 className="h-8 w-8 text-primary" />
                            <span className="text-xl font-bold bg-gradient-to-r from-primary to-purple-600 bg-clip-text text-transparent">
                GameCraft Studios
              </span>
                        </Link>

                        <p className="text-gray-600 mb-6 leading-relaxed">
                            게임 업계 최고의 인재와 기업을 연결하는 플랫폼입니다. <br/>
                            당신의 게임 개발 여정을 함께 시작하세요.
                        </p>

                        <div className="flex items-center space-x-1 text-sm text-gray-500 mb-2">
                            <MapPin className="h-4 w-4" />
                            <span>서울특별시 동대문구 </span>
                        </div>

                        <div className="flex items-center space-x-1 text-sm text-gray-500 mb-4">
                            <Mail className="h-4 w-4" />
                            <span>juyoung34@soonsgsil.ac.kr</span>
                        </div>

                        <div className="flex space-x-4">
                            <Link
                                href="https://github.com"
                                className="text-gray-400 hover:text-gray-600 transition-colors"
                                target="_blank"
                                rel="noopener noreferrer"
                            >
                                <Github className="h-5 w-5" />
                            </Link>
                        </div>
                    </div>

                    {/* 링크 섹션들 */}
                    <div>
                        <h3 className="font-semibold text-gray-900 mb-4">서비스</h3>
                        <ul className="space-y-2">
                            {footerLinks.product.map((link) => (
                                <li key={link.name}>
                                    <Link
                                        href={link.href}
                                        className="text-sm text-gray-600 hover:text-gray-900 transition-colors"
                                    >
                                        {link.name}
                                    </Link>
                                </li>
                            ))}
                        </ul>
                    </div>

                    <div>
                        <h3 className="font-semibold text-gray-900 mb-4">지원</h3>
                        <ul className="space-y-2">
                            {footerLinks.support.map((link) => (
                                <li key={link.name}>
                                    <Link
                                        href={link.href}
                                        className="text-sm text-gray-600 hover:text-gray-900 transition-colors"
                                    >
                                        {link.name}
                                    </Link>
                                </li>
                            ))}
                        </ul>
                    </div>

                    <div>
                        <h3 className="font-semibold text-gray-900 mb-4">회사</h3>
                        <ul className="space-y-2">
                            {footerLinks.company.map((link) => (
                                <li key={link.name}>
                                    <Link
                                        href={link.href}
                                        className="text-sm text-gray-600 hover:text-gray-900 transition-colors"
                                    >
                                        {link.name}
                                    </Link>
                                </li>
                            ))}
                        </ul>
                    </div>

                    <div>
                        <h3 className="font-semibold text-gray-900 mb-4">약관</h3>
                        <ul className="space-y-2">
                            {footerLinks.legal.map((link) => (
                                <li key={link.name}>
                                    <Link
                                        href={link.href}
                                        className="text-sm text-gray-600 hover:text-gray-900 transition-colors"
                                    >
                                        {link.name}
                                    </Link>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>

                {/* 하단 구분선 및 저작권 */}
                <div className="border-t border-gray-200 mt-8 pt-8">
                    <div className="flex flex-col md:flex-row justify-between items-center space-y-4 md:space-y-0">
                        <p className="text-sm text-gray-500">
                            © {currentYear} GameCraft Studios. All rights reserved.
                        </p>

                        <div className="flex items-center space-x-6">
                            <Link
                                href="/sitemap"
                                className="text-sm text-gray-500 hover:text-gray-700 transition-colors"
                            >
                                사이트맵
                            </Link>
                            <Link
                                href="/accessibility"
                                className="text-sm text-gray-500 hover:text-gray-700 transition-colors"
                            >
                                접근성
                            </Link>
                            <span className="text-sm text-gray-400">
                Made with ❤️ for GameDevs
              </span>
                        </div>
                    </div>
                </div>
            </div>
        </footer>
    )
}