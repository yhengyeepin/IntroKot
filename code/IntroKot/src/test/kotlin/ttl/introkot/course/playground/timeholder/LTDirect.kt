package ttl.introkot.course.playground.timeholder

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import ttl.introkot.course.playground.timeholder.LocalTimePart.*
import java.time.Duration.ofMinutes
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.coroutines.coroutineContext

/**
 * Utility function to addTime based on the LocalTimePart.  The default
 * amount to add is 1.  We cheat here and use LocalTime to do the
 * actual calculation
 */
fun LocalTime.addTime(timePart: ChronoUnit, amount: Long = 1): LocalTime {
    val result = when (timePart) {
        ChronoUnit.HOURS -> this.plusHours(amount)
        ChronoUnit.MINUTES -> this.plusMinutes(amount)
        ChronoUnit.SECONDS -> this.plusSeconds(amount)
        else -> throw RuntimeException("UnAcceptable time part: $timePart")
    }
    return result
}

operator fun LocalTime.plus(oneOf: ChronoUnit): LocalTime = addTime(oneOf, 1)
operator fun LocalTime.minus(oneOf: ChronoUnit): LocalTime = addTime(oneOf, -1)

////This one is used with ChronoUnitHolder below to allow adding multiple units of a TimePart
operator fun LocalTime.plus(chHolder: ChronoUnitHolder): LocalTime =
        addTime(chHolder.unitPart, chHolder.numTimes)

operator fun LocalTime.minus(chHolder: ChronoUnitHolder): LocalTime =
        addTime(chHolder.unitPart, -chHolder.numTimes)

//Iterable range where we will step in seconds
operator fun LocalTime.rangeTo(other: LocalTime):
        LocalTimeRange {
    return LocalTimeRange(this, other)
}

/**
 * This function is called for the 'until' infix function
 * e.g. t1 until t2.  It's end is 1 stepPart before the end time
 */
infix fun LocalTime.until(other: LocalTime): LocalTimeRange {
    return this.rangeTo(other).apply {
        endInclusive = false
    }
}

/**
 * Set the step size.  The actual value that will be
 * stepped depends on the stepPart setting, set
 * by the 'tp' function below.
 */
infix fun LocalTimeRange.step(stepSize: Long): LocalTimeRange {
    this.step = stepSize
    return this
}

/**
 * To specify the ChronoUnit to use for increment when stepping
 * We return 'this' from here, rather than create a new object.
 * Just because.
 */
infix fun LocalTimeRange.tp(timePart: ChronoUnit): LocalTimeRange {
    stepPart = timePart
    return this
}

/**
 * The trick here is to wrap a second Iterable (Iterator)
 * around the first one, and then return a Pair of (index, LocalTime)
 * for each element.
 */
fun LocalTimeRange.withIndex(): Iterable<Pair<Int, LocalTime>> {
    val origIterator = iterator()
    var index = 0
    val iterable = object : Iterable<Pair<Int, LocalTime>> {
        //Create an other iterator which wraps the first, and
        //returns a Pair(index, element) for each element.
        override fun iterator(): Iterator<Pair<Int, LocalTime>> =
            object : Iterator<Pair<Int, LocalTime>> {
                override fun hasNext(): Boolean {
                    return origIterator.hasNext()
                }

                override fun next(): Pair<Int, LocalTime> {
                    val p = Pair(index, origIterator.next())
                    index += step.toInt()
                    return p
                }
        }
    }

    return iterable;
}

/**
 * Class to hold state for a range of Time.
 * It holds:
 *   start, end: Start and End times of the range
 *   step:  The step to use for iteration
 *   stepPart: The ChronoUnit time field we use for Stepping
 *   endInclusive: it this an inclusive (true) or exclusive range
 *
 */
class LocalTimeRange(val start: LocalTime, val end: LocalTime,
                     var step: Long = 1) : Iterable<LocalTime> {
    var realEnd = end

    //Are we including the end value?
    var endInclusive = true
        set(value) {
            realEnd = end - stepPart
            field = value
        }

    //This is the property we use to decide
    //what ChronoUnit field of the LocalTime we use for stepping
    var stepPart = ChronoUnit.SECONDS
        set(value) {
            //reset the end only if we are not including it
            if (!endInclusive) {
                realEnd = end - value
            }
            field = value
        }

    override fun iterator(): Iterator<LocalTime> =
            object : Iterator<LocalTime> {
                var current = start
                override fun hasNext(): Boolean {
                    return current <= realEnd
                }

                override fun next(): LocalTime {
                    val obj = current
                    current += stepPart * step
                    return obj
                }
            }
}

/**
 * This class is used to work with multiple ChronoUnits.
 * e.g, to add 2 hours we could say
 * val newTime = oldTime + ChronoUnit.HOURS * 2
 * This means we need a multiply operator on ChronoUnit, which
 * needs to return something that can hold both the ChronoUnit and
 * the multiplication value.  That's what this class is for.
 * The multiply operator for LocalTimePart returns a LTRepeat
 * object.  This then gets sent to the plus operator on LocalTimeHolder
 * where we now have both values to work with.
 */
class ChronoUnitHolder(val unitPart: ChronoUnit, val numTimes: Long)

///**
// * This is used to handle invocations like 'YEAR * 2'
// * It simply saves the state in a LTRepeat object, which is then
// * used as mentioned above.
// */
operator fun ChronoUnit.times(numTimes: Long): ChronoUnitHolder = ChronoUnitHolder(this, numTimes)

/**
 * @author whynot
 */

class TestLtDirect {
    @Test
    fun testInGreaterLessThan() {
        val t1 = LocalTime.of(10, 10, 10)
        val t2 = LocalTime.of(10, 10, 20)

        Assertions.assertTrue(t2 > t1)

        val t4 = LocalTime.of(10, 10, 10)
        Assertions.assertTrue(t4 == t1)
        Assertions.assertTrue(t4 < t2)
    }

    //Can make Ranges of LocalTime because it implements Comparable
    @Test
    fun testInNotIn() {
        val t1 = LocalTime.of(10, 10, 10)
        val t2 = LocalTime.of(10, 10, 20)

        val t3 = LocalTime.of(10, 10, 11)
        Assertions.assertTrue(t3 in t1..t2)

        val t4 = LocalTime.of(9, 10, 10)
        Assertions.assertTrue(t4 !in t1..t2)
    }

    //Ranges for iteration are going to require us to
    //provide a rangeTo Extension method for LocalTime
    //that returns an Iterable.  See above.
    @Test
    fun testIteration() {
        val t1 = LocalTime.of(10, 10, 10)
        val t2 = LocalTime.of(10, 10, 20)

        var count = 0
        for (t in t1..t2) {
            println("t: $t")
            count++
        }
        assertEquals(11, count)
    }

    @Test
    fun testPlus() {
        val t1 = LocalTime.of(10, 10, 10)
        val t2 = t1 + ChronoUnit.MINUTES

        Assertions.assertTrue(t2 == LocalTime.of(10, 11, 10))
    }


    @Test
    fun testPlusMinuteChange() {
        val t1 = LocalTime.of(10, 59, 10)
        val t2 = t1 + ChronoUnit.MINUTES

        Assertions.assertTrue(t2 == LocalTime.of(11, 0, 10))
    }

    @Test
    fun testPlusWithMultiple() {
        val t1 = LocalTime.of(10, 59, 10)
        val t2 = t1 + ChronoUnit.MINUTES * 10

        var lt = LocalTime.now()
        var lt2 = lt + ofMinutes(10)

        Assertions.assertTrue(t2 == LocalTime.of(11, 9, 10))
    }

    @Test
    fun testMinus() {
        val t1 = LocalTime.of(10, 10, 10)
        val t2 = t1 - ChronoUnit.SECONDS

        Assertions.assertTrue(t2 == LocalTime.of(10, 10, 9))
    }

    @Test
    fun testMinusChangeMinute() {
        val t1 = LocalTime.of(10, 10, 0)
        val t2 = t1 - ChronoUnit.SECONDS

        Assertions.assertTrue(t2 == LocalTime.of(10, 9, 59))
    }

    @Test
    fun testMinusChangeMinuteMultiple() {
        val t1 = LocalTime.of(10, 10, 5)
        val t2 = t1 - ChronoUnit.SECONDS * 10

        Assertions.assertTrue(t2 == LocalTime.of(10, 9, 55))
        println("t2: $t2")
    }

    @Test
    fun testUntil() {
        val t1 = LocalTime.of(10, 10, 5)
        val t2 = LocalTime.of(10, 10, 20)

        val range = t1 until t2
        Assertions.assertEquals(range.end, LocalTime.of(10, 10, 20))
        Assertions.assertEquals(range.realEnd, LocalTime.of(10, 10, 19))
    }

    @Test
    fun testIterationUntil() {
        val t1 = LocalTime.of(10, 10, 10)
        val t2 = LocalTime.of(10, 10, 20)

        var count = 0
        for (t in t1 until t2) {
            println("t: $t")
            count++
        }
        Assertions.assertEquals(10, count)
    }

    @Test
    fun testIterationStep() {
        val t1 = LocalTime.of(10, 10, 11)
        val t2 = LocalTime.of(10, 10, 20)

        var count = 0
        for (t in t1..t2 step 3) {
            println("t: $t")
            count++
        }
        Assertions.assertEquals(4, count)

        count = 0
        for (t in t1 until t2 step 3) {
            println("t: $t")
            count++
        }
        Assertions.assertEquals(3, count)
    }

    @Test
    fun testLocalTimePart() {
        val t1 = LocalTime.of(10, 10, 11)
        val t2 = LocalTime.of(10, 15, 20)

        var count = 0
        for (t in t1..t2 tp ChronoUnit.MINUTES) {
            println("t: $t")
            count++
        }
        Assertions.assertEquals(6, count)

        count = 0
        for (t in t1 until t2 tp ChronoUnit.MINUTES) {
            println("t: $t")
            count++
        }
        Assertions.assertEquals(5, count)
    }

    @Test
    fun testLocalTimePartWithStep() {
        val t1 = LocalTime.of(10, 10, 11)
        val t2 = LocalTime.of(20, 16, 20)

        var count = 0
        for (t in t1..t2 tp ChronoUnit.HOURS step 2) {
            println("t: $t")
            count++
        }
        Assertions.assertEquals(6, count)

        count = 0
        for (t in t1 until t2 tp ChronoUnit.MINUTES step 2) {
            count++
        }
        Assertions.assertEquals(303, count)
    }

    @Test
    fun testWithIndex() {
        val t1 = LocalTime.of(10, 10, 11)
        val t2 = LocalTime.of(10, 16, 20)

        val r = t1..t2
        var count = 0
        var validIndexes = mutableSetOf(0, 2, 4, 6)
        for ((index, t) in ((t1..t2) tp ChronoUnit.MINUTES step 2).withIndex()) {
            println("$index: $t")
            assertTrue(index in validIndexes)
            validIndexes.remove(index)
            count++
        }
        Assertions.assertEquals(4, count)
        assertEquals(0, validIndexes.size);

        count = 0
        validIndexes = mutableSetOf(0, 3)
        for (t in (t1 until t2 tp ChronoUnit.MINUTES step 3).withIndex()) {
            println("t: $t")
            assertTrue(t.first in validIndexes)
            validIndexes.remove(t.first)
            count++
        }
        Assertions.assertEquals(2, count)
        assertEquals(0, validIndexes.size);
    }
}
