import java.util.*
import kotlin.math.max

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    var max = 0
    var isEndOfSequence = false
    do {
        val number = scanner.nextInt()
        isEndOfSequence = number == 0
        max = max(max, number)
    } while (!isEndOfSequence)

    println(max)
}
