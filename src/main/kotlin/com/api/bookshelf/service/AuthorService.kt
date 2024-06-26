package com.api.bookshelf.service

import com.api.bookshelf.constants.Constants
import com.api.bookshelf.domain.model.Author
import com.api.bookshelf.domain.repository.AuthorRepository
import com.api.bookshelf.dto.request.AddAuthorDto
import com.api.bookshelf.dto.request.DeleteAuthorDto
import com.api.bookshelf.dto.request.UpdateAuthorDto
import com.api.bookshelf.dto.response.AuthorDto
import com.api.bookshelf.dto.response.AuthorListDto
import org.jooq.postgresql.generated.tables.records.AuthorRecord
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthorService(private val authorRepository: AuthorRepository) {

    @Transactional(readOnly = true)
    fun findAuthorById(id: Int): AuthorDto? {
        val author = authorRepository.findById(id)
        return if (author != null) {
            AuthorDto(
                id = author.id, name = author.name, description = author.description
            )
        } else {
            null
        }
    }

    @Transactional(readOnly = true)
    fun findAuthorByKeyword(keyword: String): AuthorListDto {
        return AuthorListDto(authorList = authorRepository.findByKeyword(keyword).map { author: Author ->
            AuthorDto(
                id = author.id, name = author.name, description = author.description
            )
        })
    }

    @Transactional(readOnly = true)
    fun findAuthorsByNotDeleted(): AuthorListDto {
        return AuthorListDto(authorList = authorRepository.findAllByNotDeleted().map { author: Author ->
            AuthorDto(
                id = author.id, name = author.name, description = author.description
            )
        })
    }

    @Transactional
    fun addAuthor(addAuthorDto: AddAuthorDto) {
        authorRepository.saveAuthor(
            AuthorRecord(
                name = addAuthorDto.name,
                description = addAuthorDto.description
            )
        )
    }

    @Transactional
    fun updateBook(updateAuthorDto: UpdateAuthorDto) {
        authorRepository.saveAuthor(
            AuthorRecord(
                id = updateAuthorDto.id,
                name = updateAuthorDto.name,
                description = updateAuthorDto.description,
                isDeleted = Constants.Sql.NotDeleted
            )
        )
    }

    @Transactional
    fun deleteAuthor(deleteAuthorDto: DeleteAuthorDto) {
        val author = authorRepository.findById(deleteAuthorDto.id)
        if (author != null) {
            authorRepository.saveAuthor(
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