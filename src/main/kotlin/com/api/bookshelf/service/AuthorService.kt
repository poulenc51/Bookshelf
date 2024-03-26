package com.api.bookshelf.service

import com.api.bookshelf.constants.Constants
import com.api.bookshelf.domain.model.Author
import com.api.bookshelf.domain.repository.AuthorRepository
import com.api.bookshelf.dto.request.AddAuthorDto
import com.api.bookshelf.dto.request.DeleteAuthorDto
import com.api.bookshelf.dto.request.UpdateAuthorDto
import com.api.bookshelf.dto.response.AuthorBooksDto
import com.api.bookshelf.dto.response.AuthorBooksListDto
import com.api.bookshelf.dto.response.AuthorDto
import com.api.bookshelf.dto.response.AuthorListDto
import com.api.bookshelf.dto.response.BookDto
import org.jooq.postgresql.generated.tables.records.AuthorRecord
import org.springframework.stereotype.Service

@Service
class AuthorService(private val authorRepository: AuthorRepository) {

    fun findAuthorById(id: Int): AuthorDto {
        val author = authorRepository.findById(id)
        return AuthorDto(
            id = author.id, name = author.name, description = author.description
        )
    }

    fun findAuthorByKeyword(keyword: String): AuthorListDto {
        return AuthorListDto(authorList = authorRepository.findByKeyword(keyword).map { author: Author ->
            AuthorDto(
                id = author.id, name = author.name, description = author.description
            )
        })
    }

    fun findAuthorsByNotDeleted(): AuthorListDto {
        return AuthorListDto(authorList = authorRepository.findAllByNotDeleted().map { author: Author ->
            AuthorDto(
                id = author.id, name = author.name, description = author.description
            )
        })
    }

    fun findAuthorWithBooksAndNotDeleted(): AuthorBooksListDto {
        return AuthorBooksListDto(
            authorBooksList = authorRepository.findAllAuthorsWithBooks().map { authorWithBooks ->
                AuthorBooksDto(
                    id = authorWithBooks.id,
                    name = authorWithBooks.name,
                    description = authorWithBooks.description,
                    books = authorWithBooks.books.map { book ->
                        BookDto(
                            id = book.id,
                            authorId = authorWithBooks.id,
                            title = book.title,
                            publicationDate = book.publicationDate
                        )
                    }
                )
            }
        )

    }

    fun addAuthor(addAuthorDto: AddAuthorDto) {
        authorRepository.saveBook(
            AuthorRecord(
                name = addAuthorDto.name,
                description = addAuthorDto.description
            )
        )
    }

    fun updateBook(updateAuthorDto: UpdateAuthorDto) {
        authorRepository.saveBook(
            AuthorRecord(
                id = updateAuthorDto.id,
                name = updateAuthorDto.name,
                description = updateAuthorDto.description,
                isDeleted = Constants.Sql.NotDeleted
            )
        )
    }

    fun deleteAuthor(deleteAuthorDto: DeleteAuthorDto) {
        val author = authorRepository.findById(deleteAuthorDto.id)
        if (author != null) {
            authorRepository.saveBook(
                AuthorRecord(
                    id = deleteAuthorDto.id,
                    name = author.name,
                    description = author.description,
                    isDeleted = Constants.Sql.isDeleted
                )
            )
        }
    }
}