data class City(val name: String) {
    var degrees: Int = 0
        get() =
            when {
                field >= -92 && field <= 57 -> field
                name == "Dubai" -> 30
                name == "Moscow" -> 5
                name == "Hanoi" -> 20
                else -> field
            }
}

fun main() {
    val first = readLine()!!.toInt()
    val second = readLine()!!.toInt()
    val third = readLine()!!.toInt()
    val firstCity = City("Dubai")
    firstCity.degrees = first
    val secondCity = City("Moscow")
    secondCity.degrees = second
    val thirdCity = City("Hanoi")
    thirdCity.degrees = third

    val theColdestCity = getTheColdestOneOrNeither(firstCity, secondCity, thirdCity)
    println(theColdestCity?.name ?: "neither")
}

fun getTheColdestOneOrNeither(vararg cities: City): City? {
    var sortedCities = cities.sortedBy { it.degrees }

    val theColdestCity = sortedCities.firstOrNull()
    sortedCities = sortedCities.drop(1)
    val theSecondColdestCity = sortedCities.firstOrNull()

    return if (theColdestCity?.degrees == theSecondColdestCity?.degrees) null else theColdestCity
}
