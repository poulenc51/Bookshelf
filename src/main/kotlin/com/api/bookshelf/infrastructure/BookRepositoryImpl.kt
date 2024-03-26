package com.api.bookshelf.infrastructure

import com.api.bookshelf.constants.Constants
import com.api.bookshelf.domain.model.Book
import com.api.bookshelf.domain.repository.BookRepository
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.postgresql.generated.tables.Book.Companion.BOOK
import org.jooq.postgresql.generated.tables.records.BookRecord
import org.springframework.stereotype.Repository
import java.time.LocalDate

const val `NOT DELETED`: Boolean = Constants.Sql.NotDeleted

@Repository
class BookRepositoryImpl(
    private val dslContext: DSLContext
) : BookRepository {
    override fun findById(id: Int): Book {
        return dslContext
            .select()
            .from(BOOK)
            .where(BOOK.ID.eq(id))
            .fetchOne()!!.map { toModel(it) }
    }

    override fun findAllByNotDeleted(): List<Book> {
        return dslContext
            .select()
            .from(BOOK)
            .where(BOOK.IS_DELETED.eq(`NOT DELETED`))
            .fetch().map { toModel(it) }
    }

    override fun findAllByAuthorIdAndNotDeleted(id: Int): List<Book> {
        return dslContext
            .select()
            .from(BOOK)
            .where(BOOK.AUTHOR_ID.eq(id))
            .and(BOOK.IS_DELETED.eq(`NOT DELETED`))
            .fetch().map { toModel(it) }
    }

    override fun saveBook(bookRecord: BookRecord) {
        if (bookRecord.id == null) {
            // 新しいレコードの場合はINSERT
            dslContext.insertInto(BOOK)
                .set(BOOK.TITLE, bookRecord.title)
                .set(BOOK.AUTHOR_ID, bookRecord.authorId)
                .set(BOOK.PUBLICATION_DATE, bookRecord.publicationDate)
                .execute()
        } else {
            // 既存のレコードの場合はUPDATE
            dslContext.update(BOOK)
                .set(BOOK.TITLE, bookRecord.title)
                .set(BOOK.AUTHOR_ID, bookRecord.authorId)
                .set(BOOK.PUBLICATION_DATE, bookRecord.publicationDate)
                .set(BOOK.IS_DELETED, bookRecord.isDeleted)
                .where(BOOK.ID.eq(bookRecord.id))
                .execute()
        }
    }

    private fun toModel(record: Record) = Book(
        record.getValue(BOOK.ID)!!,
        record.getValue(BOOK.TITLE)!!,
        record.getValue(BOOK.AUTHOR_ID)!!,
        record.getValue(BOOK.PUBLICATION_DATE)!!,
        record.getValue(BOOK.IS_DELETED)!!
    )
}
