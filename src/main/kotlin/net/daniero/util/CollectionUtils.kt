package net.daniero.util

import java.util.*

fun <E> Stack<E>.pop(n: Int): List<E> {
    val list = ArrayList<E>()

    repeat(n) { list += this.pop() }

    return list.reversed()
}