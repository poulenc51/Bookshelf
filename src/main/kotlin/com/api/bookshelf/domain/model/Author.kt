package com.api.bookshelf.domain.model

data class Author(
    val id: Int,
    val name: String,
    val description: String,
    val isDeleted: Boolean
)