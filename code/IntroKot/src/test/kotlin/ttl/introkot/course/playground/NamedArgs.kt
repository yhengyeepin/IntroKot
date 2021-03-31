package ttl.introkot.course.playground

/**
 * Demo to show that using function references means you
 * loose the ability to name arguments.
 * @author whynot
 */


fun addProduct(name: String, price: Double) {

}

fun doWork() {
    //Here we can refer to arguments by name.
    //Compiler does the right thing compile time.
    addProduct(price = 22.3, name = "Spindle")
}

fun doOtherWork() {
    //Using a function references makes Arg names not available
    val funRef = ::addProduct
    funRef("Strainer", 33.55)

    takeAFun(funRef)
    takeAFun(::addProduct)

    //Will Not compile
//    funRef(name = "Juicer", price = 33.55)
}

fun takeAFun(block: (String, Double) -> Unit) {
    block("Strainer", 33.55)
}