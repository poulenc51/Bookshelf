package com.api.bookshelf.service.authorService

import com.api.bookshelf.domain.model.Author
import com.api.bookshelf.domain.repository.AuthorRepository
import com.api.bookshelf.dto.response.AuthorDto
import com.api.bookshelf.service.AuthorService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

private const val `NOT DELETED`: Boolean = com.api.bookshelf.constants.Constants.Sql.NotDeleted
@SpringBootTest
@ActiveProfiles("test")
class FindAuthorByIdTest {

    @MockBean
    private lateinit var authorRepository: AuthorRepository

    @Autowired
    private lateinit var authorService: AuthorService

    @Test
    fun `findAuthorById returns AuthorDto for existing author`() {

        val authorId = 1
        val expectedAuthor = AuthorDto(id = authorId, name = "Test Author", description = "Description")
        `when`(authorRepository.findById(authorId)).thenReturn(
            Author(
                id = authorId,
                name = "Test Author",
                description = "Description",
                isDeleted = `NOT DELETED`
            )
        )

        val result = authorService.findAuthorById(authorId)

        assertThat(result).isEqualTo(expectedAuthor)
    }

    @Test
    fun `findAuthorById returns null for non-existing author`() {

        val authorId = 1
        `when`(authorRepository.findById(authorId)).thenReturn(null)

        val result = authorService.findAuthorById(authorId)


        assertThat(result).isNull()
    }
}