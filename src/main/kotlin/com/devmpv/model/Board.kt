package com.devmpv.model

import javax.persistence.*

/**
 * Entity containing Information about board.
 *
 * @author devmpv
 */
@Entity
class Board(
        @Id
        val id: String = "",

        var title: String = ""
) {
    @OneToMany(cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY, mappedBy = "board")
    var threads: MutableSet<Thread>? = null

    constructor() : this("")
}
