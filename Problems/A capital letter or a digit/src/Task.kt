// Posted from EduTools plugin
import java.util.Scanner

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    val ch1 = scanner.next().first()
    println(ch1 in '\u0031'..'\u0039' || ch1.isLetter() && ch1.isUpperCase())
}