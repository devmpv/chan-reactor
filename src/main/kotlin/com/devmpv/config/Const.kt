package com.devmpv.config

/**
 * Commonly used constants
 *
 * @author devmpv
 */
class Const private constructor() {

    /**
     * Setting for thumbnail generation.
     *
     * @author devmpv
     */
    object Thumbs {

        /**
         * Thumbnail width
         */
        val WIDTH = 200

        /**
         * Thumbnail height
         */
        val HEIGHT = 200
    }

    companion object {

        /**
         * Message title field
         */
        val TITLE = "title"

        /**
         * Message text field
         */
        val TEXT = "text"

        /**
         * Message board field
         */
        val BOARD = "board"

        /**
         * Message thread field
         */
        val THREAD = "thread"
    }
}
