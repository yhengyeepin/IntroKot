package ttl.introkot.course._1Intro

/**
 * @author whynot
 */

//main method.  Note Array declaration
//public static void main(String [] args)

//fun main(args: Array<String>){
//    println("Here we go args")
//    defaulter("Boo")
//}

fun main(){
    println("Here we go args")
    defaulter("Boo")
}

//public void process(int input)

fun process(input: Int) {
    val result = input * 10
    println("result is $result")    //string interpolation
}

//public int someWork(int a, int b)

fun someWork(a: Int, b: Int) : Int {
    val result = a * b
    return result
}

//Default arguments
fun defaulter(name: String, id: Double = 22.0, age: Int = 10) {
    if(id != 22.0) {
        println("def: $name, $id $age")
    } else {
        println("def: $name, $id $age")
    }
}

fun callDefaulter() {
    defaulter("abc", 220.0)
    defaulter("abc", 220.0, 10)
}

fun callWithNamedArguments() {
    defaulter("abc", age = 20);
}


