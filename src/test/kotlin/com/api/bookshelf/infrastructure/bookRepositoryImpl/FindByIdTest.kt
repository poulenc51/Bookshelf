package com.api.bookshelf.infrastructure.bookRepositoryImpl

import com.api.bookshelf.domain.model.Book
import com.api.bookshelf.infrastructure.BookRepositoryImpl
import org.assertj.core.api.Assertions
import org.jooq.DSLContext
import org.jooq.postgresql.generated.tables.Book.Companion.BOOK
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate

private const val `NOT DELETED`: Boolean = com.api.bookshelf.constants.Constants.Sql.NotDeleted
private const val `IS DELETED`: Boolean = com.api.bookshelf.constants.Constants.Sql.isDeleted

@SpringBootTest
@ActiveProfiles("test")
class FindByIdTest {

    @Autowired
    private lateinit var dslContext: DSLContext

    @Autowired
    private lateinit var bookRepository: BookRepositoryImpl

    fun setupTestData() {
        dslContext.insertInto(BOOK)
            .columns(BOOK.ID, BOOK.TITLE, BOOK.AUTHOR_ID, BOOK.PUBLICATION_DATE, BOOK.IS_DELETED)
            .values(1, "Test Book 1", 1, LocalDate.parse("2000-01-01"), `NOT DELETED`)
            .values(2, "Test Book 2", 2, LocalDate.parse("2000-01-01"), `NOT DELETED`)
            .values(3, "Test Book 3", 3, LocalDate.parse("2000-01-01"), `NOT DELETED`)
            .values(4, "Test Book 4", 2, LocalDate.parse("2000-01-01"), `NOT DELETED`)
            .values(5, "Test Book 5", 3, LocalDate.parse("2000-01-01"), `IS DELETED`)
            .execute()
    }

    @AfterEach
    fun cleanup() {
        dslContext.delete(BOOK).execute()
    }

    @Test
    fun `findById should return Book when ID is found and not deleted`() {
        setupTestData()

        val bookId = 1
        val expectedBook = Book(1, "Test Book 1", 1, LocalDate.parse("2000-01-01"), `NOT DELETED`)

        val result = bookRepository.findById(bookId)

        Assertions.assertThat(result).isEqualTo(expectedBook)
    }

    @Test
    fun `findById should return null when Book is marked as deleted`() {
        setupTestData()

        val bookId = 5

        val result = bookRepository.findById(bookId)

        Assertions.assertThat(result).isNull()
    }

    @Test
    fun `findById should return null when ID is not found`() {
        setupTestData()

        val bookId = 999

        val result = bookRepository.findById(bookId)

        Assertions.assertThat(result).isNull()
    }
}