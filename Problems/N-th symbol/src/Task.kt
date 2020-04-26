// Posted from EduTools plugin
import java.util.*

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)

    val string = scanner.nextLine()
    val number = scanner.nextInt()

    println("Symbol # $number of the string \"$string\" is '${string[number - 1]}'")
    // write your code here
}