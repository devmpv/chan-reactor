package com.devmpv.model

import java.util.stream.Collectors
import javax.persistence.*
import javax.validation.constraints.Size

/**
 * Base entity for messages
 *
 * @author devmpv
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
open class Message(

    var title:String,

    @Column(nullable = false)
    @Size(min = 1, max = 65535)
    var text:String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Long? = null

    @ManyToOne
    @JoinColumn
    var thread:Thread? = null

    @ManyToMany(cascade = arrayOf(CascadeType.ALL), targetEntity = Message::class)
    @JoinTable(name = "message_replies", joinColumns = arrayOf(JoinColumn(name = "ParentId")), inverseJoinColumns = arrayOf(JoinColumn(name = "MessageId")))
    var replies:MutableSet<Message> = HashSet()

    @ManyToMany(cascade = arrayOf(CascadeType.ALL), targetEntity = Message::class)
    @JoinTable(name = "message_replies", joinColumns = arrayOf(JoinColumn(name = "MessageId")), inverseJoinColumns = arrayOf(JoinColumn(name = "ParentId")))
    var replyTo:MutableSet<Message> = HashSet()

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "message_attachment")
    var attachments:MutableSet<Attachment>? = null

    @Column(nullable = false)
    val createdAt:Long? = System.currentTimeMillis()

    @Column(nullable = false)
    var updatedAt:Long? = System.currentTimeMillis()

    @Transient
    var replyIds:Set<Long?> = HashSet()
        get() = replies.stream().map({ it.id }).collect(Collectors.toSet()).toSet()

    constructor() : this("", "")
}
