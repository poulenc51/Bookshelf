package com.api.bookshelf.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class DeleteBookDto (

    @field:NotNull(message = "書籍IDの入力は必須です")
    @field:JsonProperty("bookId", required = true)
    val id: Int,
)