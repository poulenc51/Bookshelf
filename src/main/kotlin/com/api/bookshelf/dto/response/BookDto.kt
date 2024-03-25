package com.api.bookshelf.dto.response

import java.time.LocalDate

data class BookDto (
    val id: Int,
    val title: String,
    val authorId: Int,
    val publicationDate: LocalDate
)