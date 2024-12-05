import java.io.File
import java.lang.Math.abs

class Day03 {
    fun readFileAsString(fileName: String): String = File(fileName).readText(Charsets.UTF_8)

    fun main() {
        val input = readFileAsString("input.txt")
        println(firstStar(input))
        println(secondStar(input))
    }

    fun firstStar(input: String): Int =
        Regex("(mul)\\([0-9]+\\,[0-9]+\\)").findAll(input)
            .map { uncorrupted -> uncorrupted.value.split(",").map { it.filter { char -> char.isDigit() }.toInt() } }
            .sumOf { it[0] * it[1] }

    fun secondStar(input: String): Int {
        var enabled = true
        return Regex("(don't\\(\\))?(do\\(\\))?(mul\\([0-9]+\\,[0-9]+\\))?").findAll(input)
            .map { corrupted -> corrupted.value.split(")") }
            .flatten()
            .filter { it != "" }.sumOf { corrupted ->
                when (corrupted) {
                    "don't(" -> {
                        enabled = false
                        0
                    }
                    "do(" -> {
                        enabled = true
                        0
                    }
                    else -> {
                        if (enabled) corrupted.split(",").map { it.filter { char -> char.isDigit() }.toInt() }
                            .reduce { sum, element -> sum * element } else 0
                    }
                }
            }
    }

}