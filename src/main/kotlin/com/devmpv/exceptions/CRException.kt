package com.devmpv.exceptions

/**
 * Chan-reactor exception
 *
 * @author devmpv
 */
class CRException : RuntimeException {

    constructor(message: String) : super(message) {}

    constructor(message: String, cause: Throwable) : super(message, cause) {}

    companion object {

        private val serialVersionUID = 5748872141056604785L
    }

}
