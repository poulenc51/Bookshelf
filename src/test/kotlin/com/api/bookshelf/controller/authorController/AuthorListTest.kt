package com.api.bookshelf.controller.authorController

import com.api.bookshelf.controller.AuthorController
import com.api.bookshelf.dto.response.AuthorDto
import com.api.bookshelf.dto.response.AuthorListDto
import com.api.bookshelf.service.AuthorService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.hamcrest.Matchers.equalTo

@WebMvcTest(AuthorController::class)
class AuthorListTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var authorService: AuthorService

    @Test
    fun `authorList returns list of authors`() {
        val expectedAuthors = AuthorListDto(
            listOf(
                AuthorDto(1, "著者1", "説明1"),
                AuthorDto(2, "著者2", "説明2"))
        )
        `when`(authorService.findAuthorsByNotDeleted()).thenReturn(expectedAuthors)

        mockMvc.perform(get("/author/"))
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.authorList[1].id", equalTo(2)))
    }

    @Test
    fun `authorList method in AuthorController calls findAuthorsByNotDeleted method in AuthorService`() {
        val expectedAuthors = AuthorListDto(
            listOf(
                AuthorDto(1, "著者1", "説明1"),
                AuthorDto(2, "著者2", "説明2"))
        )
        `when`(authorService.findAuthorsByNotDeleted()).thenReturn(expectedAuthors)

        mockMvc.perform(get("/author/"))
            .andExpect(status().isOk)

        verify(authorService).findAuthorsByNotDeleted()
    }

    @Test
    fun `authorList method in AuthorController returns expected response when findAuthorsByNotDeleted method in AuthorService is called`() {
        val expectedAuthors = AuthorListDto(
            listOf(
                AuthorDto(1, "著者1", "説明1"),
                AuthorDto(2, "著者2", "説明2"))
        )
        `when`(authorService.findAuthorsByNotDeleted()).thenReturn(expectedAuthors)

        mockMvc.perform(get("/author/"))
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.authorList[0].id", equalTo(1)))
            .andExpect(jsonPath("$.authorList[0].name", equalTo("著者1")))
            .andExpect(jsonPath("$.authorList[0].description", equalTo("説明1")))
            .andExpect(jsonPath("$.authorList[1].id", equalTo(2)))
            .andExpect(jsonPath("$.authorList[1].name", equalTo("著者2")))
            .andExpect(jsonPath("$.authorList[1].description", equalTo("説明2")))
    }
}
