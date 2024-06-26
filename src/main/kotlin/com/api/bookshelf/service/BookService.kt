package com.api.bookshelf.service

import com.api.bookshelf.constants.Constants
import com.api.bookshelf.domain.model.Book
import com.api.bookshelf.domain.repository.BookRepository
import com.api.bookshelf.dto.request.AddBookDto
import com.api.bookshelf.dto.request.DeleteBookDto
import com.api.bookshelf.dto.request.UpdateBookDto
import com.api.bookshelf.dto.response.BookDto
import com.api.bookshelf.dto.response.BookListDto
import org.jooq.postgresql.generated.tables.records.BookRecord
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class BookService(private val bookRepository: BookRepository) {

    @Transactional(readOnly = true)
    fun findBookById(id: Int): BookDto? {
        val book = bookRepository.findById(id)
        return if (book != null) {
            BookDto(
                id = book.id, title = book.title, authorId = book.authorId, publicationDate = book.publicationDate
            )
        } else {
            null
        }
    }

    @Transactional(readOnly = true)
    fun findBooksByNotDeleted(): BookListDto {
        return BookListDto(bookList = bookRepository.findAllByNotDeleted().map { book: Book ->
            BookDto(
                id = book.id, title = book.title, authorId = book.authorId, publicationDate = book.publicationDate
            )
        })
    }

    @Transactional(readOnly = true)
    fun findBooksByKeyword(keyword: String): BookListDto {
        return BookListDto(bookList = bookRepository.findByKeyword(keyword).map { book: Book ->
            BookDto(
                id = book.id, title = book.title, authorId = book.authorId, publicationDate = book.publicationDate
            )
        })
    }

    @Transactional(readOnly = true)
    fun findBooksByAuthorIdAndNotDeleted(authorId: Int): BookListDto {
        return BookListDto(bookList = bookRepository.findAllByAuthorIdAndNotDeleted(authorId).map { book: Book ->
            BookDto(
                id = book.id, title = book.title, authorId = book.authorId, publicationDate = book.publicationDate
            )
        })
    }

    @Transactional
    fun addBook(addBookDto: AddBookDto) {
        bookRepository.saveBook(
            BookRecord(
                title = addBookDto.title,
                authorId = addBookDto.authorId,
                publicationDate = LocalDate.parse(addBookDto.publicationDate)
            )
        )
    }

    @Transactional
    fun updateBook(updateBookDto: UpdateBookDto) {
        bookRepository.saveBook(
            BookRecord(
                id = updateBookDto.id,
                title = updateBookDto.title,
                authorId = updateBookDto.authorId,
                publicationDate = LocalDate.parse(updateBookDto.publicationDate),
                isDeleted = Constants.Sql.NotDeleted
            )
        )
    }

    @Transactional
    fun deleteBook(deleteBookDto: DeleteBookDto) {
        val book = bookRepository.findById(deleteBookDto.id)
        if (book != null) {
            bookRepository.saveBook(
                BookRecord(
                    id = book.id,
                    title = book.title,
                    authorId = book.authorId,
                    publicationDate = book.publicationDate,
                    isDeleted = Constants.Sql.isDeleted
                )
            )
        }
    }
}