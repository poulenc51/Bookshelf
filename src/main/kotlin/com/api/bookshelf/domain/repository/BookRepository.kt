package com.api.bookshelf.domain.repository

import com.api.bookshelf.domain.model.Book

interface BookRepository {
    fun findAllByNotDeleted(): List<Book>
}