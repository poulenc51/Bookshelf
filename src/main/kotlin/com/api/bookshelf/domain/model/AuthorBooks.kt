package com.api.bookshelf.domain.model

data class AuthorBooks(
    val id: Int,
    val name: String,
    val description: String,
    val isDeleted: Boolean,
    val books: List<Book>
)