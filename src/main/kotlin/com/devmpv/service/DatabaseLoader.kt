package com.devmpv.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

import com.devmpv.model.Board
import com.devmpv.repositories.BoardRepository
import com.devmpv.repositories.MessageRepository
import com.devmpv.repositories.ThreadRepository

/**
 * Initial database fill up.
 *
 * @author devmpv
 */
@Component
class DatabaseLoader @Autowired
constructor(threadRepo: ThreadRepository, private val boardRepo: BoardRepository, messageRepository: MessageRepository) : CommandLineRunner {

    @Throws(Exception::class)
    override fun run(vararg strings: String) {
        if (!boardRepo.existsById("b")) {
            this.boardRepo.save(Board("b", "Anything"))
        }
        if (!boardRepo.existsById("po")) {
            this.boardRepo.save(Board("po", "Politics"))
        }
        if (!boardRepo.existsById("dev")) {
            this.boardRepo.save(Board("dev", "Development"))
        }
    }
}