package com.api.bookshelf.infrastructure

import com.api.bookshelf.constants.Constants
import com.api.bookshelf.domain.model.Book
import com.api.bookshelf.domain.repository.BookRepository
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.h2.generated.tables.Book.Companion.BOOK
import org.springframework.stereotype.Repository

const val `NOT DELETED`: Boolean = Constants.Sql.NotDeleted

@Repository
class BookRepositoryImpl(
    private val dslContext: DSLContext
) : BookRepository {
    override fun findAllByNotDeleted(): List<Book> {
        return this.dslContext
            .select()
            .from(BOOK)
            .where(BOOK.IS_DELETED.eq(`NOT DELETED`))
            .fetch().map { toModel(it) }
    }

    private fun toModel(record: Record) = Book(
        record.getValue(BOOK.ID)!!,
        record.getValue(BOOK.TITLE)!!,
        record.getValue(BOOK.AUTHOR_ID)!!,
        record.getValue(BOOK.PUBLICATION_DATE)!!
    )
}
