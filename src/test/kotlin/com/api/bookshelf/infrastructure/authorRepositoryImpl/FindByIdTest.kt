package com.api.bookshelf.infrastructure.authorRepositoryImpl

import com.api.bookshelf.domain.model.Author
import com.api.bookshelf.infrastructure.AuthorRepositoryImpl
import org.assertj.core.api.Assertions.assertThat
import org.jooq.DSLContext
import org.jooq.postgresql.generated.tables.references.AUTHOR
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

private const val `NOT DELETED`: Boolean = com.api.bookshelf.constants.Constants.Sql.NotDeleted
private const val `IS DELETED`: Boolean = com.api.bookshelf.constants.Constants.Sql.isDeleted

@SpringBootTest
@Import(AuthorRepositoryImpl::class)
@ActiveProfiles("test")
class FindByIdTest {

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
            .execute()
    }

    @AfterEach
    fun cleanup() {
        dslContext.delete(AUTHOR).execute()
    }

    @Test
    fun `findById should return Author when ID is found and not deleted`() {
        setupTestData()

        val authorId = 1
        val expectedAuthor = Author(1, "Test Author 1", "説明1", `NOT DELETED`)

        val result = authorRepository.findById(authorId)

        assertThat(result).isEqualTo(expectedAuthor)
    }

    @Test
    fun `findById should return null when Author is marked as deleted`() {
        setupTestData()

        val authorId = 3

        val result = authorRepository.findById(authorId)

        assertThat(result).isNull()
    }

    @Test
    fun `findById should return null when ID is not found`() {
        setupTestData()

        val authorId = 999

        val result = authorRepository.findById(authorId)

        assertThat(result).isNull()
    }
}