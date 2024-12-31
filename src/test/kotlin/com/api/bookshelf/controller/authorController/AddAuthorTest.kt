package com.api.bookshelf.controller.authorController

import com.api.bookshelf.controller.AuthorController
import com.api.bookshelf.dto.request.AddAuthorDto
import com.api.bookshelf.service.AuthorService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.server.ResponseStatusException

@WebMvcTest(AuthorController::class)
class AddAuthorTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var authorService: AuthorService

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `addAuthor returns status 200`() {
        val authorJson = "{\"name\":\"テスト　太郎\",\"description\":\"テスト説明\"}"
        val expectedResponse = "post:AddAuthorDto(name=テスト　太郎, description=テスト説明)"

        mockMvc.perform(
            post("/author/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authorJson)
        )
            .andExpect(status().isOk)
            .andExpect(content().string(expectedResponse))
    }

    @Test
    fun `when service throws exception then returns status 400`() {
        val authorJson = "{\"name\":\"テスト 太郎\",\"description\":\"テスト説明\"}"
        val authorDto = AddAuthorDto(name = "テスト 太郎", description = "テスト説明")
        `when`(authorService.addAuthor(authorDto)).thenThrow(
            ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Invalid data"
            )
        )

        mockMvc.perform(
            post("/author/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authorJson)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `when name is blank then returns status 400 and error message`() {
        val invalidAuthorJson = objectMapper.writeValueAsString(AddAuthorDto(name = "", description = "テスト説明"))

        mockMvc.perform(
            post("/author/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidAuthorJson)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().string("名前の入力は必須です"))
    }

    @Test
    fun `when name is too long then returns status 400 and error message`() {
        val invalidAuthorJson =
            objectMapper.writeValueAsString(AddAuthorDto(name = "a".repeat(101), description = "テスト説明"))

        mockMvc.perform(
            post("/author/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidAuthorJson)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().string("名前は100文字以内で入力してください"))
    }

    @Test
    fun `when description name is too long then returns status 400 and error message`() {
        val invalidAuthorJson = objectMapper.writeValueAsString(AddAuthorDto(name = "テスト 太郎", "a".repeat(1001)))

        mockMvc.perform(
            post("/author/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidAuthorJson)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().string("説明は1000文字以内で入力してください"))
    }

    @Test
    fun `addAuthor method in AuthorController calls addAuthor method in AuthorService`() {
        val authorDto = AddAuthorDto(name = "テスト 太郎", description = "テスト説明")
        val authorJson = objectMapper.writeValueAsString(authorDto)

        mockMvc.perform(
            post("/author/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authorJson)
        )
            .andExpect(status().isOk)

        verify(authorService).addAuthor(authorDto)
    }

    @Test
    fun `addAuthor method in AuthorController returns expected response when addAuthor method in AuthorService is called`() {
        val authorDto = AddAuthorDto(name = "テスト 太郎", description = "テスト説明")
        val authorJson = objectMapper.writeValueAsString(authorDto)
        val expectedResponse = "post:AddAuthorDto(name=テスト 太郎, description=テスト説明)"

        mockMvc.perform(
            post("/author/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authorJson)
        )
            .andExpect(status().isOk)
            .andExpect(content().string(expectedResponse))
    }
}
