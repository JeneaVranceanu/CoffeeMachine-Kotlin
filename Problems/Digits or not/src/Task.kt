// Posted from EduTools plugin
import java.util.*

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)

    val isDigit1 = scanner.next().first().isDigit()
    val isDigit2 = scanner.next().first().isDigit()
    val isDigit3 = scanner.next().first().isDigit()
    val isDigit4 = scanner.next().first().isDigit()

    print("$isDigit1\\$isDigit2\\$isDigit3\\$isDigit4")
}
