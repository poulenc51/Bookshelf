package com.api.bookshelf.dto.response

data class AuthorBooksDto(
    val id: Int,
    val name: String,
    val description: String,
    val books: List<BookDto>
)