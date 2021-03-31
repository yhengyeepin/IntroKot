package ttl.introkot.course._7OtherStuff

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

/**
 * @author whynot
 */
class ReturnQuiz {

    fun getLargestValueForThing(czPairs: List<Pair<String, Int>>): Map<String, Int> {
        val map: MutableMap<String, Int> = TreeMap()
        czPairs.forEach { (zone, ci) ->
            println("p: $zone, $ci")
            map[zone] = map.get(zone)?.let { current ->
                if (current > ci) {
                    //TODO -  How many ways to return from the
                    // next line??

                    //Returns directly to caller of the outer function.
//                        return mapOf()

                    //return@forEach: This is like a continue, i.e. we can
                    //skip setting it back into the map.  No return value
                    //allowed.
                    return@forEach

                    //return@let returns to the caller of the let
                    //Like just using the value
//                    return@let current
//                    current
                } else {
                    ci
                }
            } ?: ci
        }

        return map
    }

    @Test
    fun testReturns() {
        val czPairs = listOf("pink" to 100, "orange" to 2, "pink" to 3, "red" to 15, "orange" to 5, "pink" to 56000, "pink" to 1)

        val result = getLargestValueForThing(czPairs)
        println("result: $result")
        assertEquals(56000, result["pink"])
        assertEquals(15, result["red"])
        assertEquals(5, result["orange"])

    }

}