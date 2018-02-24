package com.devmpv

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Application main class.
 *
 * @author devmpv
 */
@SpringBootApplication
class ChanReactor protected constructor() {
    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(ChanReactor::class.java, *args)
        }
    }

}
