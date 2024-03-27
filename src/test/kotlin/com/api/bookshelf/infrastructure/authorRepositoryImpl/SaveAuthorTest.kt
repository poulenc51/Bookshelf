package com.api.bookshelf.infrastructure.authorRepositoryImpl

import com.api.bookshelf.infrastructure.AuthorRepositoryImpl
import org.assertj.core.api.Assertions.assertThat
import org.jooq.DSLContext
import org.jooq.postgresql.generated.tables.records.AuthorRecord
import org.jooq.postgresql.generated.tables.references.AUTHOR
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

private const val `NOT DELETED`: Boolean = com.api.bookshelf.constants.Constants.Sql.NotDeleted
private const val `IS DELETED`: Boolean = com.api.bookshelf.constants.Constants.Sql.isDeleted

@SpringBootTest
@ActiveProfiles("test")
class SaveAuthorTest {

    @Autowired
    private lateinit var dslContext: DSLContext

    @Autowired
    private lateinit var authorRepository: AuthorRepositoryImpl

    fun setupTestData() {
        dslContext.insertInto(AUTHOR)
            .columns(AUTHOR.ID, AUTHOR.NAME, AUTHOR.DESCRIPTION, AUTHOR.IS_DELETED)
            .values(1, "Test Author 1", "説明1", `NOT DELETED`)
            .values(2, "Test Author 2", "説明2", `NOT DELETED`)
            .values(3, "Test Author 3", "説明3", `IS DELETED`)
            .values(4, "テスト 著者 4", "説明3", `NOT DELETED`)
            .execute()
        dslContext.execute("SELECT setval('author_id_seq', (SELECT MAX(id) FROM author))")
    }

    @AfterEach
    fun cleanup() {
        dslContext.delete(AUTHOR).execute()
    }

    @Test
    fun `saveBook should insert new record when id is null`() {
        setupTestData()

        val newAuthor = AuthorRecord(null, "New Author", "New Description")

        authorRepository.saveAuthor(newAuthor)

        val insertedRecord = dslContext.selectFrom(AUTHOR)
            .where(AUTHOR.ID.eq(5))
            .fetchOne()

        assertThat(insertedRecord).isNotNull
        assertThat(insertedRecord!!.id).isEqualTo(5)
        assertThat(insertedRecord.name).isEqualTo("New Author")
        assertThat(insertedRecord.description).isEqualTo("New Description")
        assertThat(insertedRecord.isDeleted).isFalse()
    }

    @Test
    fun `saveBook should update existing record when id is not null`() {
        setupTestData()

        val existingAuthor = AuthorRecord(1, "Existing Author", "Updated Description", `NOT DELETED`)

        authorRepository.saveAuthor(existingAuthor)

        val updatedRecord = dslContext.selectFrom(AUTHOR)
            .where(AUTHOR.ID.eq(1))
            .fetchOne()

        assertThat(updatedRecord).isNotNull
        assertThat(updatedRecord!!.id).isEqualTo(1)
        assertThat(updatedRecord.name).isEqualTo("Existing Author")
        assertThat(updatedRecord.description).isEqualTo("Updated Description")
        assertThat(updatedRecord.isDeleted).isFalse()
    }

    @Test
    fun `saveBook should logic delete existing record when id is not null`() {
        setupTestData()

        val deletingAuthor = AuthorRecord(1, "Deleting Author", "Deleting Description", `IS DELETED`)

        authorRepository.saveAuthor(deletingAuthor)

        val deleteRecord = dslContext.selectFrom(AUTHOR)
            .where(AUTHOR.ID.eq(1))
            .fetchOne()

        assertThat(deleteRecord!!.isDeleted).isTrue()
    }
}