package com.devmpv.model

import javax.persistence.*

/**
 * Thread entity based on [Message]
 *
 * @author devmpv
 */
@Entity
class Thread(
        @ManyToOne
        @JoinColumn(name = "board_id", nullable = false)
        var board: Board? = null,

        title: String,

        text: String
) : Message(title, text) {

    @OneToMany(cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY, mappedBy = "thread", targetEntity = Message::class)
    var messages: MutableSet<Message> = HashSet()

    constructor() : this(null, "", "")
}
