import java.util.*
import kotlin.math.max
import kotlin.math.min

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    val intArray = IntArray(scanner.nextInt())
    var index = 0
    var number = 1
    while (index < intArray.size) {
        repeat(min(intArray.size - index, number)) {
            intArray[index] = number
            index++
        }
        number++
    }
    println(intArray.joinToString(separator = " "))
}