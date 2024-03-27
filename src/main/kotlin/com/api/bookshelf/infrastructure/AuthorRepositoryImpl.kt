package com.api.bookshelf.infrastructure

import com.api.bookshelf.constants.Constants
import com.api.bookshelf.domain.model.Author
import com.api.bookshelf.domain.repository.AuthorRepository
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.postgresql.generated.tables.records.AuthorRecord
import org.jooq.postgresql.generated.tables.references.AUTHOR
import org.springframework.stereotype.Repository

private const val `NOT DELETED`: Boolean = Constants.Sql.NotDeleted

@Repository
class AuthorRepositoryImpl(
    private val dslContext: DSLContext
) : AuthorRepository {
    override fun findById(id: Int): Author? {
        return dslContext
            .select()
            .from(AUTHOR)
            .where(AUTHOR.ID.eq(id))
            .and(AUTHOR.IS_DELETED.eq(`NOT DELETED`))
            .fetchOne()?.map { toModel(it) }
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

    override fun saveAuthor(authorRecord: AuthorRecord) {
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
