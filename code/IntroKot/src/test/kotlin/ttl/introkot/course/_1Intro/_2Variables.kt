package ttl.introkot.course._1Intro

/**
 * @author whynot
 */

//compile time constant
const val MAX = 1000

//Type is inferred.
//Compiler chooses primitive or reference based on context
//val => Read Only, *not* necessarily immutable
//var => Read Write
val i = 10

//var changeAble: Int = 10
var changeAble: Int? = null

//var message: String? = null
//lateinit var message: String

fun foo() {
    var j = 10
//    j = changeAble
    changeAble = j
//   i = 20
    changeAble = 35
}


//immutable list
val list: List<Int> = listOf(10, 20, 30)
//mutable
val mutList: MutableList<Int> = mutableListOf(10, 20, 30)

fun doLists() {
//    list.add(50)
    mutList.add(50)
}

fun takeAList(list: List<Int>) {
//    list.add(50)
}

fun doWork() {
    val ml = mutableListOf(10, 20)
    takeAList(ml)
}

