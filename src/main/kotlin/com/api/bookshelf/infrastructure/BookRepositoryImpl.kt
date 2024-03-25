package com.api.bookshelf.infrastructure

import com.api.bookshelf.constants.Constants
import com.api.bookshelf.domain.model.Book
import com.api.bookshelf.domain.repository.BookRepository
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.postgresql.generated.tables.Book.Companion.BOOK
import org.springframework.stereotype.Repository
import java.time.LocalDate

const val `NOT DELETED`: Boolean = Constants.Sql.NotDeleted

@Repository
class BookRepositoryImpl(
    private val dslContext: DSLContext
) : BookRepository {
    override fun findAllByNotDeleted(): List<Book> {
        return dslContext
            .select()
            .from(BOOK)
            .where(BOOK.IS_DELETED.eq(`NOT DELETED`))
            .fetch().map { toModel(it) }
    }

    override fun addBook(title: String, authorId: Int, publicationDate: LocalDate) {
        dslContext
            .insertInto(BOOK)
            .set(BOOK.TITLE, title)
            .set(BOOK.AUTHOR_ID, authorId)
            .set(BOOK.PUBLICATION_DATE, publicationDate)
            .execute()
    }

    private fun toModel(record: Record) = Book(
        record.getValue(BOOK.ID)!!,
        record.getValue(BOOK.TITLE)!!,
        record.getValue(BOOK.AUTHOR_ID)!!,
        record.getValue(BOOK.PUBLICATION_DATE)!!
    )
}
