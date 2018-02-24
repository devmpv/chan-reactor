package com.devmpv.repositories

import com.devmpv.model.Attachment
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.io.Serializable
import java.util.*

/**
 * Attachment repository.
 *
 * @author devmpv
 */
interface AttachmentRepository : CrudRepository<Attachment, Serializable> {

    @Query("select count(m) from Attachment a join a.messages m where a.id = ?1")
    fun countMessages(id: Long): Long?

    fun findByMd5(md5: String): Optional<Attachment>

    @Query("select a from Attachment a where a.messages IS EMPTY")
    fun findEmpty(): Set<Attachment>
}
