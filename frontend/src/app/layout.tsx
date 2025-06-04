import type { Metadata } from 'next'
import { Inter } from 'next/font/google'
import './globals.css'
import { Providers } from '@/components/layout/Providers'
import { Header } from '@/components/layout/Header'
import { Footer } from '@/components/layout/Footer'

const inter = Inter({ subsets: ['latin'] })

export const metadata: Metadata = {
    title: 'GameCraft Studios | Where Gaming Talent Meets Opportunity',
    description: '게임 업계 최고의 인재들이 모이는 플랫폼',
    keywords: ['게임개발', '취업', '포트폴리오', '카카오게임즈'],
    authors: [{ name: 'GameCraft Studios' }],
    openGraph: {
        title: 'GameCraft Studios',
        description: '게임 업계 최고의 인재들이 모이는 플랫폼',
        type: 'website',
        locale: 'ko_KR',
    },
}

export default function RootLayout({
                                       children,
                                   }: {
    children: React.ReactNode
}) {
    return (
        <html lang="ko" suppressHydrationWarning>
        <body className={inter.className}>
        <Providers>
            <div className="min-h-screen bg-background flex flex-col">
                <Header />
                <main className="flex-1">
                    {children}
                </main>
                <Footer />
            </div>
        </Providers>
        </body>
        </html>
    )
}