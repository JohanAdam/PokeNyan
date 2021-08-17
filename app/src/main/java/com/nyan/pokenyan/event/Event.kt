package com.nyan.foodie.event

class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set //Allowed external to read but not write.

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}