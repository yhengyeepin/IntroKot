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

//    if (input != null) {
//        //Smart cast
//        println("${input.length}")
//    }
//    else {
//        throw RuntimeException("input is null")
//    }

    val l1: Int = input?.length ?: 10   //int

    val len2: Int? = input?.length      //Integer

//    val substr = input?.substring(0, 5) ?: throw RuntimeException("...")
//    val len = substr.length
//
////    val len = input?.length  ?: -1
//
//
//    println("len: $len")
//
//
//    val x = null
}

class TestNullNess {
    @Test
    fun main() {
        useVar(nullInitialized)
    }
}