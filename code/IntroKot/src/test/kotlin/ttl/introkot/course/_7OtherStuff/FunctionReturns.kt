package ttl.introkot.course._7OtherStuff

import org.junit.jupiter.api.Test

/**
 * @author whynot
 */

/**
 * Can use continue and break with loops like in Java
 */
fun continueAndBreakInLoops() {
    loop@ for (i in 1..100) {
        inner@ for (j in 1..100) {
            if (j + i == 200) break
            if (j + i > 200) continue@inner
        }
    }
}

/**
 * List.forEach is inlined.  So we can do a non local return
 * to the caller of this function, or we can do a
 * return@forEach which will work like a continue.
 */
fun nonLocalReturnFromInlinedFunctions(): Int {
    val x = listOf(1, 2, 5, 20).forEach {
        if (it == 2) {
            println("nlrf found 2")

//            return it           //This returns from the function
            return@forEach        //This is like a continue.  Will return to the forEach.
        } else {
            println("nlrf it: $it")
        }
    }
    return 0
}

class ReturnsTest {
    @Test
    fun testReturns() {
        val returnFromCaller = testVariousReturns("abc")
        println("returnFromCaller: $returnFromCaller")

        val y = nonLocalReturnFromInlinedFunctions()
        println("y: $y")

        //Extended Example for showing crossinlining.  Goes with
        //postInlineFoo and extendedInlineExample.
        val z = extendedInlineExample("hel") { str ->
            if (str.length < 5) {
                //A plain return would return from main.
                //So a return with a value is not allowed.
                //Only a plain return.
                //Also, if postInlineFoo is made noinline, then
                //This return will no longer be allowed.
//          return //2   //Will not compile with the value
                //This will return to the caller of the lambda
                return@extendedInlineExample 2
                //A plain value will do the same thing
                //2
            }
            str.length
        }

        println("z: $z")
    }

    fun testVariousReturns(id: String): Int {

        //Non Inlined lambdas can only have local returns.
        //Local in this case means local to where ever the lambda will
        // be called from, which may be 'foo', or some function that foo calls.
        //See the example below with postFoo.
        val xs = foo(id) { str ->
            if (str.length < 5) {
                return@foo 2   //returns to the caller of the lambda
            }
            str.length
        }
        println("xs: $xs")

        //Inlined functions can have a *Non Local* return.
        //Here a plain return will go back to the caller of 'testVariousReturns'.
        //A return@inlineFoo will go back to the caller of the lambda.  That could
        // be inLineFoo, or it could be some method that inLineFoo calls.
        val inlineLocalResult: Int = inlineFoo(id) { str ->
            if (str.length < 5) {
                println("func str: $str")
//            2000
                return@inlineFoo 2000  //this will return to the caller of the lambda
//            return 2000            //this will return from testVariousReturns
            } else {
                str.length
            }
        }

        println("InlineLocal: $inlineLocalResult")


        //Here again we can't use a plain 'return' because
        //The functions we are calling are not inline.
        val x = bar(id) {
            foo(id) { str ->
                if (str.length < 5) {
//                return 2     //Will not compile
                    return@foo 2
                    //2      //Can also just use the return value
                }
                str.length
            }
        }

        println("x: $x")

        //Same story here.  bar is not inlined, so
        //no non local returns allowed.
        //i.e return 2 will not compile
        val z = bar(id) {
            inlineFoo(id) { str ->
                if (str.length < 5) {
//                return 2        //Will NOT compile
                    return@inlineFoo 2
                }
                str.length
            }
        }

        println("z: $z")
        //Here we *can* use a plain return if we want,
        //because all the functions we are calling *are* inline
        val zz = inlineBar(id) {
            inlineFoo(id) { str ->
                if (str.length < 5) {
                    return 2
//                return@inlineFoo 2
                }
                str.length
            }
        }

        println("zz: $zz")
        return z
    }

    fun foo(id: String, func: (String) -> Int): Int {
        val result = func(id)
//    val result = postFoo(id, func);
        println("foo.result: $result")
        return result
    }

    fun postFoo(id: String, func: (String) -> Int): Int {
        val result = func(id)
        println("postFoo.result: $result")
        return result
    }

    fun bar(id: String, func: () -> Int): Int {
        return func()
    }

    /**
     * inline function.  This will make the 'func' lambda also
     * inlined.
     */
    inline fun inlineFoo(id: String, func: (String) -> Int): Int {
        val localResult = func(id)
        println("inLineFoo.localResult: $localResult")
        return localResult
    }

    /**
     * inline function.  This will make the 'func' lambda also
     * inlined.
     */
    inline fun inlineBar(id: String, func: () -> Int): Int {
        return func()
    }


    /**
     * inline function.  This will make the 'func' lambda also
     * inlined.
     */
    inline fun extendedInlineExample(id: String, func: (String) -> Int): Int {
//    val localResult = func(id)
//    val localResult = postInlineFoo(id, func)

        val localResult = postInlineFoo(id) {
            val lr = func(id)
            println("postInlineFoo.localResult: $lr")
            return@postInlineFoo lr   //This will return to 'postInlineFoo'
//        return lr                //This will return back to the caller of 'extendedInlineExample'
        }
        println("inLineFoo.localResult: $localResult")
        return localResult
    }

    //If this function is not inlined, the argument in the function above will need a 'crossinline'
    inline fun postInlineFoo(id: String, processor: () -> Int): Int {
        val procResult = processor()
        println("procResult.procResult: $procResult")
        return procResult
    }
}
