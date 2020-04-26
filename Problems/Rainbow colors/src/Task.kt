import java.util.Scanner

enum class Rainbow(val color: String) {
    RED("red"),
    ORANGE("orange"),
    YELLOW("yellow"),
    GREEN("green"),
    BLUE("blue"),
    INDIGO("indigo"),
    VIOLET("violet");

    companion object {
        fun finByName(name: String): Rainbow? {
            return values().firstOrNull { it.color == name.toLowerCase() }
        }
    }
}

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    println(Rainbow.finByName(input.next()) != null)
}
