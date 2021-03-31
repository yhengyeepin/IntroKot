package ttl.introkot.course._1Intro

import org.junit.jupiter.api.Test

fun testRanges(c: Char) {
    if (c in 'a'..'z' || c in 'A'..'Z') {
        println("isLetter")
    } else if (c in '0'..'9') {
        println("isNumber")
    } else {
        println("None of the above")
    }
}

fun rangeWithWhen(c: Char) = when (c) {
    in '0'..'9' -> "isLetter"
    in 'a'..'z', in 'A'..'Z' -> "isNumber"
    else -> "None of the above"
}

//Ranges work by doing comparisons
//Can make a range of any type that implements
//Comparable
fun stringRanges() {

    val strRange = "Alpha".."Tango"

    val inRange = "Apple" in strRange
    val outOfRange = "Zulu" in strRange

    println("inRange: $inRange, outOfRange: $outOfRange")

}

/**
 * Becoming a Comparable allows you to create Ranges of this
 * this class, and to use it in boundary tests, e.g.
 * cf in ClassForRange(10)..ClassForRange(20)
 *
 * But you can't use it in loops.  That requires a rangeTo function
 * which will return an Iterable.  Show below.
 */
data class ClassForRange(val value: Int) : Comparable<ClassForRange> {

    override fun compareTo(other: ClassForRange): Int {
        return this.value.compareTo(other.value)
    }
}

fun myRangerTest() {
    val mrRange = ClassForRange(10)..ClassForRange(25)

    val mc = ClassForRange(15)
    val inRange = mc in mrRange

    val mc2 = ClassForRange(46)
    val outOfRange = mc2 in mrRange

    println("inRange: $inRange, outOfRange: $outOfRange")
}

/**
 * Create a rangeTo Extension function on ClassForRange which will
 * return an instance of OurIterable, declared below.
 * The 'operator' is necessary to make this work.
 */
infix operator fun ClassForRange.rangeTo(other: ClassForRange) : OurIterable
{
    return OurIterable(this, other)
}

/**
 *OurIterable lets us iterate over ClassForRange instances.
 * Very simple minded implementation with no error checking etc.
 * We are assuming a Step size of 1
 */
class OurIterable(val start: ClassForRange,
                  val end: ClassForRange) : Iterable<ClassForRange> {

    override fun iterator(): Iterator<ClassForRange> = object : Iterator<ClassForRange>{

        var current = start
        override fun hasNext(): Boolean {
            return current <= end
        }

        //Hard coded to step by 1
        override fun next(): ClassForRange {
            val oc = current
            current = ClassForRange(current.value + 1)

            return oc
        }
    }

}

class RangesTest {
    @Test
    fun testRange() {
//    testRanges('%')
//    rangeWithWhen('#')
//    stringRanges()
        myRangerTest()
    }

    @Test
    fun myRangerForLoopTest() {
        val st = ClassForRange(10)..(ClassForRange(14))
        for (i in st) {
            println("i: $i")
        }
    }
}

