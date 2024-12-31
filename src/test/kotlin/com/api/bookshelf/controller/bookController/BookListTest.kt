package com.api.bookshelf.controller.bookController

import com.api.bookshelf.controller.BookController
import com.api.bookshelf.dto.response.BookDto
import com.api.bookshelf.dto.response.BookListDto
import com.api.bookshelf.service.AuthorService
import com.api.bookshelf.service.BookService
import org.hamcrest.Matchers.equalTo
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
import java.time.LocalDate

@WebMvcTest(BookController::class)
class BookListTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var bookService: BookService

    @MockBean
    private lateinit var authorService: AuthorService

    @Test
    fun `bookList returns list of books`() {
        val expectedBooks = BookListDto(
            listOf(
                BookDto(1, "タイトル1", 1, LocalDate.parse("2000-01-01")),
                BookDto(2, "タイトル2", 2, LocalDate.parse("2000-01-01"))
            )
        )
        `when`(bookService.findBooksByNotDeleted()).thenReturn(expectedBooks)

        mockMvc.perform(get("/book/"))
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.bookList[1].id", equalTo(2)))
    }

    @Test
    fun `bookList method in BookController calls findBooksByNotDeleted method in BookService`() {
        val expectedBooks = BookListDto(
            listOf(
                BookDto(1, "タイトル1", 1, LocalDate.parse("2000-01-01")),
                BookDto(2, "タイトル2", 2, LocalDate.parse("2000-01-01"))
            )
        )
        `when`(bookService.findBooksByNotDeleted()).thenReturn(expectedBooks)

        mockMvc.perform(get("/book/"))
            .andExpect(status().isOk)

        verify(bookService).findBooksByNotDeleted()
    }

    @Test
    fun `bookList method in BookController returns expected response when findBooksByNotDeleted method in BookService is called`() {
        val expectedBooks = BookListDto(
            listOf(
                BookDto(1, "タイトル1", 1, LocalDate.parse("2000-01-01")),
                BookDto(2, "タイトル2", 2, LocalDate.parse("2000-01-01"))
            )
        )
        `when`(bookService.findBooksByNotDeleted()).thenReturn(expectedBooks)

        mockMvc.perform(get("/book/"))
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.bookList[0].id", equalTo(1)))
            .andExpect(jsonPath("$.bookList[0].title", equalTo("タイトル1")))
            .andExpect(jsonPath("$.bookList[0].authorId", equalTo(1)))
            .andExpect(jsonPath("$.bookList[0].publicationDate", equalTo("2000-01-01")))
            .andExpect(jsonPath("$.bookList[1].id", equalTo(2)))
            .andExpect(jsonPath("$.bookList[1].title", equalTo("タイトル2")))
            .andExpect(jsonPath("$.bookList[1].authorId", equalTo(2)))
            .andExpect(jsonPath("$.bookList[1].publicationDate", equalTo("2000-01-01")))
    }
}
