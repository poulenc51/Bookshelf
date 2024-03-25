package com.api.bookshelf.domain.model

import java.time.LocalDate

data class Book(
    val id: Int,
    val title: String,
    val authorId: Int,
    val publicationDate: LocalDate
)