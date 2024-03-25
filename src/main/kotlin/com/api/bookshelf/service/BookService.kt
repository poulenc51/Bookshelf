package com.api.bookshelf.service

import com.api.bookshelf.dto.response.BookDto
import com.api.bookshelf.dto.response.BookListDto
import com.api.bookshelf.domain.model.Book
import com.api.bookshelf.domain.repository.BookRepository
import org.springframework.stereotype.Service

@Service
class BookService(private val bookRepository: BookRepository) {

    fun findBooksByNotDelete(): BookListDto {
        return BookListDto(
            bookList = bookRepository.findAllByNotDeleted().map { book: Book ->
                BookDto(
                    id = book.id,
                    title = book.title,
                    authorId = book.authorId
                )
            }
        )
    }
}