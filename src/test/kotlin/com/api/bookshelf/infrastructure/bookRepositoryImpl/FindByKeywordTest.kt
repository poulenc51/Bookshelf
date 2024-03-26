package com.api.bookshelf.infrastructure.bookRepositoryImpl

import com.api.bookshelf.infrastructure.BookRepositoryImpl
import org.assertj.core.api.Assertions
import org.jooq.DSLContext
import org.jooq.postgresql.generated.tables.Book
import org.jooq.postgresql.generated.tables.references.BOOK
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate

private const val `NOT DELETED`: Boolean = com.api.bookshelf.constants.Constants.Sql.NotDeleted
private const val `IS DELETED`: Boolean = com.api.bookshelf.constants.Constants.Sql.isDeleted
@SpringBootTest
@Import(BookRepositoryImpl::class)
@ActiveProfiles("test")
class FindByKeywordTest {

    @Autowired
    private lateinit var dslContext: DSLContext

    @Autowired
    private lateinit var bookRepository: BookRepositoryImpl

    fun setupTestData() {
        dslContext.insertInto(Book.BOOK)
            .columns(
                Book.BOOK.ID,
                Book.BOOK.TITLE,
                Book.BOOK.AUTHOR_ID,
                Book.BOOK.PUBLICATION_DATE,
                Book.BOOK.IS_DELETED
            )
            .values(1, "Test Book 1", 1, LocalDate.parse("2000-01-01"), `NOT DELETED`)
            .values(2, "Test Book 2", 2, LocalDate.parse("2000-01-01"), `NOT DELETED`)
            .values(3, "Test Book 3", 3, LocalDate.parse("2000-01-01"), `NOT DELETED`)
            .values(4, "テスト書籍 4", 2, LocalDate.parse("2000-01-01"), `NOT DELETED`)
            .values(5, "Test Book 5", 3, LocalDate.parse("2000-01-01"), `IS DELETED`)
            .execute()
    }

    @AfterEach
    fun cleanup() {
        dslContext.delete(BOOK).execute()
    }

    @Test
    fun `findByKeyword should return list of Books when keyword is found`() {
        setupTestData()

        val keyword = "Test"

        val result = bookRepository.findByKeyword(keyword)

        Assertions.assertThat(result).isNotEmpty
        Assertions.assertThat(result).anyMatch{ it.title.contains("Test")}
        Assertions.assertThat(result.size).isEqualTo(3)
    }

    @Test
    fun `findByKeyword should return list of Books when keyword is not found`() {
        setupTestData()

        val keyword = "NonExistingKeyword"

        val result = bookRepository.findByKeyword(keyword)

        Assertions.assertThat(result).isEmpty()
    }
}