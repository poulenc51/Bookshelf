package com.api.bookshelf.infrastructure

import com.api.bookshelf.constants.Constants
import com.api.bookshelf.domain.model.Author
import com.api.bookshelf.domain.model.AuthorBooks
import com.api.bookshelf.domain.model.Book
import com.api.bookshelf.domain.repository.AuthorRepository
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.postgresql.generated.tables.Book.Companion.BOOK
import org.jooq.postgresql.generated.tables.records.AuthorRecord
import org.jooq.postgresql.generated.tables.references.AUTHOR
import org.springframework.stereotype.Repository
import java.time.LocalDate

private const val `NOT DELETED`: Boolean = Constants.Sql.NotDeleted

@Repository
class AuthorRepositoryImpl(
    private val dslContext: DSLContext
) : AuthorRepository {
    override fun findById(id: Int): Author {
        return dslContext
            .select()
            .from(AUTHOR)
            .where(AUTHOR.ID.eq(id))
            .and(AUTHOR.IS_DELETED.eq(`NOT DELETED`))
            .fetchOne()!!.map { toModel(it) }
    }

    override fun findByKeyword(keyword: String): List<Author> {
        return dslContext
            .select()
            .from(AUTHOR)
            .where(AUTHOR.NAME.like("%$keyword%"))
            .and(AUTHOR.IS_DELETED.eq(`NOT DELETED`))
            .fetch().map { toModel(it) }
    }

    override fun findAllByNotDeleted(): List<Author> {
        return dslContext
            .select()
            .from(AUTHOR)
            .where(AUTHOR.IS_DELETED.eq(`NOT DELETED`))
            .fetch().map { toModel(it) }
    }

    override fun findAllAuthorsWithBooks(): List<AuthorBooks> {
        val authorsWithBooksMap = mutableMapOf<Int, AuthorBooks>()
        dslContext
            .select(
                AUTHOR.ID,
                AUTHOR.NAME,
                AUTHOR.DESCRIPTION,
                AUTHOR.IS_DELETED,
                BOOK.ID,
                BOOK.TITLE,
                BOOK.PUBLICATION_DATE,
                BOOK.IS_DELETED
            )
            .from(AUTHOR)
            .innerJoin(BOOK)
            .on(AUTHOR.ID.eq(BOOK.AUTHOR_ID))
            .where(AUTHOR.IS_DELETED.eq(`NOT DELETED`))
            .and(
                BOOK.IS_DELETED.eq(`NOT DELETED`)
            ).forEach { record ->
                val authorId = record.get("id", Int::class.java)
                val book = Book(
                    id = record.get("book_id", Int::class.java),
                    authorId = authorId,
                    title = record.get("title", String::class.java),
                    publicationDate = record.get("publication_date", LocalDate::class.java),
                    isDeleted = record.get("book_is_deleted", Boolean::class.java)
                )
                authorsWithBooksMap.computeIfAbsent(authorId) {
                    AuthorBooks(
                        id = authorId,
                        name = record.get("name", String::class.java),
                        description = record.get("description", String::class.java),
                        isDeleted = record.get("is_deleted", Boolean::class.java),
                        books = mutableListOf()
                    )
                }.books as MutableList<Book> += book
            }

        return authorsWithBooksMap.values.toList()
    }

    override fun saveBook(authorRecord: AuthorRecord) {
        if (authorRecord.id == null) {
            // 新しいレコードの場合はINSERT
            dslContext.insertInto(AUTHOR)
                .set(AUTHOR.NAME, authorRecord.name)
                .set(AUTHOR.DESCRIPTION, authorRecord.description)
                .execute()
        } else {
            // 既存のレコードの場合はUPDATE
            dslContext.update(AUTHOR)
                .set(AUTHOR.NAME, authorRecord.name)
                .set(AUTHOR.DESCRIPTION, authorRecord.description)
                .set(AUTHOR.IS_DELETED, authorRecord.isDeleted)
                .where(AUTHOR.ID.eq(authorRecord.id))
                .execute()
        }
    }

    private fun toModel(record: Record) = Author(
        record.getValue(AUTHOR.ID)!!,
        record.getValue(AUTHOR.NAME)!!,
        record.getValue(AUTHOR.DESCRIPTION)!!,
        record.getValue(AUTHOR.IS_DELETED)!!
    )
}
