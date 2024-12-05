import java.io.File
import java.lang.Math.abs

class Day01 {

    fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    fun main() {
        val input = readFileAsList("input.txt")
            .map { row -> row.split("   ").map { it.toInt() } }
        val left = input.map { it.first() }.sorted()
        val right = input.map { it.last() }.sorted()
        println(firstStar(left, right))
        println(secondStar(left, right))
    }

    fun firstStar(left: List<Int>, right: List<Int>): Int =
        right.indices.sumOf { i -> abs(right[i] - left[i]) }

    fun secondStar(left: List<Int>, right: List<Int>): Int =
        right.sumOf { number -> left.count { it == number } * number }

}