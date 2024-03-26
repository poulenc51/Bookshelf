package com.api.bookshelf.domain.repository

import com.api.bookshelf.domain.model.Author
import com.api.bookshelf.domain.model.AuthorBooks
import org.jooq.postgresql.generated.tables.records.AuthorRecord

interface AuthorRepository {

    fun findById(id: Int): Author

    fun findByKeyword(keyword: String): List<Author>

    fun findAllByNotDeleted(): List<Author>

    fun findAllAuthorsWithBooks(): List<AuthorBooks>

    fun saveBook(authorRecord: AuthorRecord)
}