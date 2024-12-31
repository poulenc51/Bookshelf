package com.api.bookshelf.infrastructure.authorRepositoryImpl

import com.api.bookshelf.domain.model.Author
import com.api.bookshelf.infrastructure.AuthorRepositoryImpl
import org.assertj.core.api.Assertions
import org.jooq.DSLContext
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
class FindAllByNotDeletedTest {

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
    }

    @AfterEach
    fun cleanup() {
        dslContext.delete(AUTHOR).execute()
    }

    @Test
    fun `findAllByNotDeleted should return list of Authors`() {
        setupTestData()

        val result = authorRepository.findAllByNotDeleted()

        Assertions.assertThat(result).isNotEmpty
        Assertions.assertThat(result.size).isEqualTo(3)
    }

    @Test
    fun `findAllByNotDeleted should return the expected list of authors`() {
        setupTestData()

        val expectedAuthors = listOf(
            Author(1, "Test Author 1", "説明1", `NOT DELETED`),
            Author(2, "Test Author 2", "説明2", `NOT DELETED`),
            Author(4, "テスト 著者 4", "説明3", `NOT DELETED`)
        )

        val result = authorRepository.findAllByNotDeleted()

        Assertions.assertThat(result).isEqualTo(expectedAuthors)
    }
}
