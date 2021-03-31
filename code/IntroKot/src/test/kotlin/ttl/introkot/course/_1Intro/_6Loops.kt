package ttl.introkot.course._1Intro

import org.junit.jupiter.api.Test

/**
 * @author whynot
 */

fun whileLoops() {
    var condition = true

    while (condition) {
        //do stuff
    }

    do {
        //do stuff
    } while (condition)
}


fun forLoops() {
    println()
    //.. is end inclusive
    //012345678910
    for (i in 0..10) {
        print(i)
    }

    println()
    //until is end exclusive
    //0123456789
    for (i in 0 until 10) {
        print(i)
    }

    println()
    for (i in 9 downTo 1 step 2) {
        print(i)
    }

}

fun collectionIteration() {
    val list = listOf("a", "b", "c")
    for (s in list) {
        println(s)
    }

    for ((index, element) in list.withIndex()) {
        println("$index $element")
    }
}

fun mapIteration() {
    val map = mapOf(1 to "one", 2 to "two", 3 to "three")

    for ((key, value) in map) {
        println("$key = $value")
    }

    for (e in map) {
        println("$e.key = $e.value")
    }
}

class LoopsTest {
    @Test
    fun main() {
        //forLoops()
        collectionIteration()
    }
}
