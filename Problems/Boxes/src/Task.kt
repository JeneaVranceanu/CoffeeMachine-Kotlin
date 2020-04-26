// Posted from EduTools plugin
import java.util.*

data class Box(val x: Int, val y: Int, val z: Int) {

    fun isCapableToHoldInside(otherBox: Box): Boolean {
        return otherBox.x <= x && otherBox.y <= y && otherBox.z <= z
    }
}

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)

    val dimensions1 = scanner.nextLine().split(" ").map { it.toInt() }.sorted()
    val firstBox = Box(dimensions1[0], dimensions1[1], dimensions1[2])

    val dimensions2 = scanner.nextLine().split(" ").map { it.toInt() }.sorted()
    val secondBox = Box(dimensions2[0], dimensions2[1], dimensions2[2])

    val result = when {
        firstBox == secondBox -> "Box 1 = Box 2"
        firstBox.isCapableToHoldInside(secondBox) -> "Box 1 > Box 2"
        secondBox.isCapableToHoldInside(firstBox) -> "Box 1 < Box 2"
        else -> "Incomparable"
    }

    println(result)
}