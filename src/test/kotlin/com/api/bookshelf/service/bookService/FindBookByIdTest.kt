package com.api.bookshelf.service.bookService

import com.api.bookshelf.domain.model.Book
import com.api.bookshelf.domain.repository.BookRepository
import com.api.bookshelf.dto.response.BookDto
import com.api.bookshelf.service.BookService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate

private const val `NOT DELETED`: Boolean = com.api.bookshelf.constants.Constants.Sql.NotDeleted

@SpringBootTest
@ActiveProfiles("test")
class FindBookByIdTest {

    @MockBean
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var bookService: BookService

    @Test
    fun `findBookById returns BookDto for existing book`() {

        val bookId = 1
        val expectedBook = BookDto(id = bookId, "Test Title", 1, LocalDate.parse("2000-01-01"))
        Mockito.`when`(bookRepository.findById(bookId)).thenReturn(
            Book(
                id = bookId,
                title = "Test Title",
                authorId = 1,
                publicationDate = LocalDate.parse("2000-01-01"),
                isDeleted = `NOT DELETED`
            )
        )

        val result = bookService.findBookById(bookId)

        Assertions.assertThat(result).isEqualTo(expectedBook)
    }

    @Test
    fun `findBookById returns null for non-existing book`() {

        val bookId = 1
        Mockito.`when`(bookRepository.findById(bookId)).thenReturn(null)

        val result = bookService.findBookById(bookId)

        Assertions.assertThat(result).isNull()
    }
}