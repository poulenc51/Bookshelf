package com.api.bookshelf.infrastructure.authorRepositoryImpl

import com.api.bookshelf.infrastructure.AuthorRepositoryImpl
import org.assertj.core.api.Assertions.assertThat
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
class FindByKeywordTest {

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
    fun `findByKeyword should return list of Authors when keyword is found`() {
        setupTestData()

        val keyword = "Test"

        val result = authorRepository.findByKeyword(keyword)

        assertThat(result).isNotEmpty
        assertThat(result).anyMatch { it.name.contains("Test") }
        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun `findByKeyword should return list of Authors when keyword is not found`() {
        setupTestData()

        val keyword = "NonExistingKeyword"

        val result = authorRepository.findByKeyword(keyword)

        assertThat(result).isEmpty()
    }

    @Test
    fun `findByKeyword should return the expected list of authors when the keyword is found`() {
        setupTestData()

        val keyword = "Test"
        val expectedAuthors = listOf(
            Author(1, "Test Author 1", "説明1", `NOT DELETED`),
            Author(2, "Test Author 2", "説明2", `NOT DELETED`)
        )

        val result = authorRepository.findByKeyword(keyword)

        assertThat(result).isEqualTo(expectedAuthors)
    }

    @Test
    fun `findByKeyword should return an empty list when the keyword is not found`() {
        setupTestData()

        val keyword = "NonExistingKeyword"

        val result = authorRepository.findByKeyword(keyword)

        assertThat(result).isEmpty()
    }
}
