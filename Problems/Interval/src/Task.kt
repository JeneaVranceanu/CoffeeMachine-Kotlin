// Posted from EduTools plugin
import java.util.*

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    val value = scanner.nextInt()
    println(if (value in -14..12 || value in 15..16 || value >= 19) "True" else "False")
}