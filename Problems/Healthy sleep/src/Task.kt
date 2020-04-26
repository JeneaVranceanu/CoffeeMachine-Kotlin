// Posted from EduTools plugin
import java.util.*

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    val minHours = scanner.nextInt()
    val maxHours = scanner.nextInt()
    val actualHours = scanner.nextInt()

    println(
            when {
                actualHours > maxHours -> "Excess"
                actualHours < minHours -> "Deficiency"
                else -> "Normal"
            })
    // write your code here
}