'use client'

import { Button } from '@/components/ui/button'

interface PositionPaginationProps {
    currentPage: number
    totalPages: number
    onPageChange: (page: number) => void
}

export function PositionPagination({
                                       currentPage,
                                       totalPages,
                                       onPageChange
                                   }: PositionPaginationProps) {
    if (totalPages <= 1) {
        return null
    }

    const getVisiblePages = () => {
        const delta = 2 // 현재 페이지 앞뒤로 보여줄 페이지 수
        const start = Math.max(0, currentPage - delta)
        const end = Math.min(totalPages - 1, currentPage + delta)

        const pages = []
        for (let i = start; i <= end; i++) {
            pages.push(i)
        }
        return pages
    }

    const visiblePages = getVisiblePages()

    return (
        <div className="flex justify-center mt-8">
            <div className="flex items-center space-x-2">
                {/* 이전 버튼 */}
                <Button
                    variant="outline"
                    onClick={() => onPageChange(currentPage - 1)}
                    disabled={currentPage === 0}
                >
                    이전
                </Button>

                {/* 첫 페이지 */}
                {visiblePages[0] > 0 && (
                    <>
                        <Button
                            variant="outline"
                            size="sm"
                            onClick={() => onPageChange(0)}
                        >
                            1
                        </Button>
                        {visiblePages[0] > 1 && (
                            <span className="px-2 text-muted-foreground">...</span>
                        )}
                    </>
                )}

                {/* 보이는 페이지들 */}
                <div className="flex space-x-1">
                    {visiblePages.map((pageNum) => (
                        <Button
                            key={pageNum}
                            variant={currentPage === pageNum ? "default" : "outline"}
                            size="sm"
                            onClick={() => onPageChange(pageNum)}
                        >
                            {pageNum + 1}
                        </Button>
                    ))}
                </div>

                {/* 마지막 페이지 */}
                {visiblePages[visiblePages.length - 1] < totalPages - 1 && (
                    <>
                        {visiblePages[visiblePages.length - 1] < totalPages - 2 && (
                            <span className="px-2 text-muted-foreground">...</span>
                        )}
                        <Button
                            variant="outline"
                            size="sm"
                            onClick={() => onPageChange(totalPages - 1)}
                        >
                            {totalPages}
                        </Button>
                    </>
                )}

                {/* 다음 버튼 */}
                <Button
                    variant="outline"
                    onClick={() => onPageChange(currentPage + 1)}
                    disabled={currentPage === totalPages - 1}
                >
                    다음
                </Button>
            </div>
        </div>
    )
}