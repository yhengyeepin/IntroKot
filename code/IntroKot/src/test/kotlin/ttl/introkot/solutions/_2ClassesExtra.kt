package ttl.introkot.solutions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * @author whynot
 */


/*********************************************Task 1*************************************/
//TODO Task 1
// Create a class from scratch that will make testTask1()
// and testTask2() below run successfully.

//Note - This class shows the limits of the data class.
// Here, our first requirement is that we don't want to use
// the comments in equality tests.  So we need to provide our
// own versions of equals and hashCode.  Strike 1
// We have a further requirement that, when copying an object,
// you have to either provide fresh comments, or you get an
// empty string.  The won't work with the default copy method
// of the data class, which wants to copy everything.  So
// we need to provide our own copy method.  Strike 2
// If we do provide a copy method, we can't use the
// data keyword.  Strike 3
class TestProductExtraSolution(val name: String, val price: Double,
                       val quantity: Int, val condition: String, val comments: String = "") {

    //Note - we use nullness as a way to give optionality in this case.
    // We could have maybe used default values if we had good out of band
    // values to use for all the types.  Doubles and Ints are tricky for that.
    // For the comments we can use the empty String since it is part of
    // our requirement for a copy that if you don't send us comments, you
    // get an empty string.
    fun copy(name: String? = null, condition: String? = null, price: Double? = null,
             quantity: Int? = null, comments: String = "") : TestProductExtraSolution{
        val n = name ?: this.name
        val c = condition ?: this.condition
        val p = price ?: this.price
        val q = quantity ?: this.quantity

        return TestProductExtraSolution(n, p, q, c, comments)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TestProductExtraSolution

        if (name != other.name) return false
        if (condition != other.condition) return false
        if (price != other.price) return false
        if (quantity != other.quantity) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + condition.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + quantity
        return result
    }

    //Note - Extra Extra credit
    // To allow destructuring magic, we need to
    // provide componentN functions.
    operator fun component1() = name
    operator fun component2() = price
    operator fun component3() = quantity
    operator fun component4() = condition
    operator fun component5() = comments
}


/*****************************Start Tests******************************************/
class ClassesExtraTests {

    //TODO - Comment out the tests and make them run.
    @Test
    fun testTask1() {
        val product1 = TestProductExtraSolution(name = "Shiny Widget", price = 22.50,
                quantity = 10, condition = "good")

        val product2 = TestProductExtraSolution(name = "Shiny Widget", price = 22.50,
                quantity = 10, condition = "good")

        val b1 = product1 == product2
        assertTrue(b1)
    }

    @Test
    fun testTask2() {
        val product1 = TestProductExtraSolution(name = "Shiny Widget", price = 22.50,
                quantity = 10, condition = "good", comments = "Really really good")

        val product2 = TestProductExtraSolution(name = "Shiny Widget", price = 22.50,
                quantity = 10, condition = "good", comments = "Good deal")

        val b1 = product1 == product2
        assertTrue(b1)
    }

    @Test
    fun testTask3() {
        val product1 = TestProductExtraSolution(name = "Shiny Widget", price = 22.50,
                quantity = 10, condition = "good", comments = "Beautiful widgets")

        val product2 = product1.copy()
        val b1 = product1 == product2

        assertTrue(b1)
    }

    @Test
    fun testTask4() {
        val product1 = TestProductExtraSolution(name = "Shiny Widget", price = 22.50,
                quantity = 10, condition = "good", comments = "Beautiful widgets")

        val product2 = product1.copy(quantity = 20)
        val b1 = product1 == product2

        assertFalse(b1)
    }

    @Test
    fun testTask5() {
        val product1 = TestProductExtraSolution(name = "Shiny Widget", price = 22.50,
                quantity = 10, condition = "good", comments = "Beautiful widgets")

        val product2 = product1.copy(quantity = 50)

        //TODO - This one is slightly tricky
        assertEquals("", product2.comments)

        assertEquals(50, product2.quantity)
    }

    //TODO - Extra Extra credit
    @Test
    fun testTask6() {
        val product1 = TestProductExtraSolution(name = "Shiny Widget", price = 22.50,
                quantity = 10, condition = "good", comments = "Beautiful widgets")

        //Destructuring
        val (name, price) = product1

        assertEquals("Shiny Widget", name)
        assertEquals(22.50, price)
    }

}