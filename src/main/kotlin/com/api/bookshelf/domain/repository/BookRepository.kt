package com.api.bookshelf.domain.repository

import com.api.bookshelf.domain.model.Book
import org.jooq.postgresql.generated.tables.records.BookRecord
import java.time.LocalDate

interface BookRepository {

    fun findById(id: Int): Book

    fun findByKeyword(keyword: String): List<Book>

    fun findAllByNotDeleted(): List<Book>

    fun findAllByAuthorIdAndNotDeleted(id: Int): List<Book>

    fun saveBook(bookRecord: BookRecord)
}