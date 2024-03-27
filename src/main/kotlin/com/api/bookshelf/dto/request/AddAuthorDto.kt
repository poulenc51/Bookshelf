package com.api.bookshelf.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class AddAuthorDto(

    @field:Size(max = 100, message = "名前は100文字以内で入力してください")
    @field:NotBlank(message = "名前の入力は必須です")
    @field:JsonProperty("name", required = true)
    val name: String,

    @field:Size(max = 1000, message = "説明は1000文字以内で入力してください")
    @field:JsonProperty("description", required = true)
    val description: String,

    )