package com.api.bookshelf.dto.request

import com.api.bookshelf.service.AuthorService
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.beans.factory.annotation.Autowired

data class UpdateAuthorDto(

    @field:NotNull(message = "著者IDの入力は必須です")
    @Min(value = 1, message = "著者IDの値は1以上である必要があります")
    @field:JsonProperty("authorId", required = true)
    val id: Int,

    @field:Size(max = 100, message = "名前は100文字以内で入力してください")
    @field:NotBlank(message = "名前の入力は必須です")
    @field:JsonProperty("name", required = true)
    val name: String,

    @field:NotNull(message = "説明の入力にnullは許可してません")
    @field:JsonProperty("description", required = true)
    val description: String,

)