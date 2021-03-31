package ttl.introkot.course._1Intro

import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author whynot
 */

//won't compile
//var nullInitialized: String
//won't compile
//var nullInitialized: String = null

var nullInitialized: String? = null

fun useVar(input: String?) {
    if (input != null) {
        //Smart cast
        println("${input.length}")
    }

    input?.length
}

class TestNullNess {
    @Test
    fun main() {
        useVar(nullInitialized)
    }
}