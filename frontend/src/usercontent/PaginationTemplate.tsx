
import { 
    Pagination, 
    PaginationContent, 
    PaginationItem, 
    PaginationPrevious, 
    PaginationLink, 
    PaginationNext 
} from '@/components/ui/pagination'
import { Badge } from '@/components/ui/badge'

const PaginationTemplate = ({ currentPage, totalPages, onPageChange }: { currentPage: number, totalPages: number, onPageChange: (page: number) => void }) => {

    const rangeFromTo = (start: number, end: number) => {
        return Array.from({ length: end - start + 1 }, (_, i) => start + i)
    }

    const handlePageChange = (page: number) => {
        onPageChange(page)
    }

  return (
    <Pagination>
      <PaginationContent>
        <PaginationItem>
            {
                currentPage > 1 ? <PaginationPrevious href="#" onClick={() => handlePageChange(currentPage - 1)} /> : <Badge className="bg-yellow-200" variant="outline">First page</Badge>
            }
        </PaginationItem>
        {
            rangeFromTo(1, totalPages).map((page) => (
                <PaginationItem key={page}>
                    <PaginationLink 
                    href="#" 
                    isActive={page === currentPage}
                    onClick={() => handlePageChange(page)}
                    >{page}</PaginationLink>
                </PaginationItem>
            ))
        }
        <PaginationItem>
            {
                currentPage < totalPages ? <PaginationNext href="#" onClick={() => handlePageChange(currentPage + 1)} /> : <Badge className="bg-yellow-200" variant="outline">Last page</Badge>
            }
        </PaginationItem>
      </PaginationContent>
    </Pagination>
  )
}

export default PaginationTemplate