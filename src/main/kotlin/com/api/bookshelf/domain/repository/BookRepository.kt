package com.api.bookshelf.domain.repository

import com.api.bookshelf.domain.model.Book
import java.time.LocalDate

interface BookRepository {
    fun findAllByNotDeleted(): List<Book>

    fun addBook(title: String, authorId: Int, publicationDate: LocalDate)
}