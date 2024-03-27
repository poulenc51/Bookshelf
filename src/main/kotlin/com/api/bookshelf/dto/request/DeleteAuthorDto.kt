package com.api.bookshelf.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class DeleteAuthorDto (

    @field:NotNull(message = "著者IDの入力は必須です")
    @Min(value = 1, message = "著者IDの値は1以上である必要があります")
    @field:JsonProperty("authorId", required = true)
    val id: Int,
)