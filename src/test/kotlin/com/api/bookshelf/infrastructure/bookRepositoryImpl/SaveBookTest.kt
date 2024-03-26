package com.api.bookshelf.infrastructure.bookRepositoryImpl

import com.api.bookshelf.infrastructure.BookRepositoryImpl
import org.assertj.core.api.Assertions
import org.jooq.DSLContext
import org.jooq.postgresql.generated.tables.Book
import org.jooq.postgresql.generated.tables.records.BookRecord
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
class SaveBookTest {

    @Autowired
    private lateinit var dslContext: DSLContext

    @Autowired
    private lateinit var bookRepository: BookRepositoryImpl

    fun setupTestData() {
        dslContext.insertInto(Book.BOOK).columns(
            Book.BOOK.ID, Book.BOOK.TITLE, Book.BOOK.AUTHOR_ID, Book.BOOK.PUBLICATION_DATE, Book.BOOK.IS_DELETED
        ).values(1, "Test Book 1", 1, LocalDate.parse("2000-01-01"), `NOT DELETED`)
            .values(2, "Test Book 2", 2, LocalDate.parse("2000-01-01"), `NOT DELETED`)
            .values(3, "Test Book 3", 3, LocalDate.parse("2000-01-01"), `NOT DELETED`)
            .values(4, "テスト書籍 4", 2, LocalDate.parse("2000-01-01"), `NOT DELETED`)
            .values(5, "Test Book 5", 3, LocalDate.parse("2000-01-01"), `IS DELETED`).execute()

        dslContext.execute("SELECT setval('book_id_seq', (SELECT MAX(id) FROM book))")
    }


    @AfterEach
    fun cleanup() {
        dslContext.delete(BOOK).execute()
    }

    @Test
    fun `saveBook should insert new record when id is null`() {
        setupTestData()

        val newBook = BookRecord(null, "New Book", 2, LocalDate.parse("2000-01-01"), `NOT DELETED`)

        bookRepository.saveBook(newBook)

        val insertedRecord = dslContext.selectFrom(BOOK).where(BOOK.ID.eq(6)).fetchOne()

        Assertions.assertThat(insertedRecord).isNotNull
        Assertions.assertThat(insertedRecord!!.id).isEqualTo(6)
        Assertions.assertThat(insertedRecord.title).isEqualTo("New Book")
        Assertions.assertThat(insertedRecord.authorId).isEqualTo(2)
        Assertions.assertThat(insertedRecord.isDeleted).isFalse()
    }

    @Test
    fun `saveBook should update existing record when id is not null`() {
        setupTestData()

        val existingBook = BookRecord(
            1, "Existing Book", 1, LocalDate.parse("2000-01-01"), `NOT DELETED`
        )

        bookRepository.saveBook(existingBook)

        val updatedRecord = dslContext.selectFrom(BOOK).where(BOOK.ID.eq(1)).fetchOne()

        Assertions.assertThat(updatedRecord).isNotNull
        Assertions.assertThat(updatedRecord!!.id).isEqualTo(1)
        Assertions.assertThat(updatedRecord.title).isEqualTo("Existing Book")
        Assertions.assertThat(updatedRecord.isDeleted).isFalse()
    }

    @Test
    fun `saveBook should logic delete existing record when id is not null`() {
        setupTestData()

        val deletingBook = BookRecord(
            1, "Deleting Book", 1, LocalDate.parse("2000-01-01"), `IS DELETED`
        )

        bookRepository.saveBook(deletingBook)

        val deleteRecord = dslContext.selectFrom(BOOK).where(BOOK.ID.eq(1)).fetchOne()

        Assertions.assertThat(deleteRecord!!.isDeleted).isTrue()
    }
}