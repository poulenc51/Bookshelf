package com.api.bookshelf.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UpdateBookDto (

    @field:NotNull(message = "書籍IDの入力は必須です")
    @field:JsonProperty("bookId", required = true)
    val id: Int,

    @field:Size(max = 100, message = "タイトルは100文字以内で入力してください")
    @field:NotBlank(message = "タイトルの入力は必須です")
    @field:JsonProperty("title", required = true)
    val title: String,

    @field:NotNull(message = "著者IDの入力は必須です")
    @field:JsonProperty("authorId", required = true)
    val authorId: Int,

    @field:Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "出版日は'yyyy-mm-dd'形式で入力してください")
    @field:NotBlank(message = "出版日の入力は必須です")
    @field:JsonProperty("publicationDate", required = true)
    val publicationDate: String
)