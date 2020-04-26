// Posted from EduTools plugin
import java.util.*

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    val sides = arrayOf(scanner.nextInt(), scanner.nextInt(), scanner.nextInt())
            .sorted()

    println(if (sides[0] + sides[1] > sides[2]) "YES" else "NO")
}