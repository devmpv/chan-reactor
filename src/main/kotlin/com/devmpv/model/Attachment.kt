package com.devmpv.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.persistence.*

/**
 * Entity to store attachment information
 *
 * @author devmpv
 */
@Entity
@Table(indexes = arrayOf(Index(columnList = "md5", unique = true)))
class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    @JsonIgnore
    var md5: String? = null

    var name: String? = null

    @ManyToMany(mappedBy = "attachments")
    val messages: Set<Message> = HashSet()
}
