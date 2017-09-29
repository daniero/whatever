package net.daniero.util

class PeekIterator<T>(private val iterator: Iterator<T>) : Iterator<T> {
    private var started = false
    private var stopped = false
    private var peek: T? = null

    fun peek(): T? {
        if (!started) {
            feed()
        }
        if (stopped) {
            return null
        }

        return peek!!
    }

    override fun hasNext(): Boolean {
        if (started)
            return !stopped
        else
            return iterator.hasNext()
    }

    override fun next(): T {
        if (!started) {
            feed()
        }

        if (stopped) {
            throw NoSuchElementException()
        }

        val tmp = peek
        feed()
        return tmp!!
    }

    private fun feed() {
        started = true

        try {
            peek = iterator.next()
        } catch (e: RuntimeException) {
            stopped = true
        }
    }
}