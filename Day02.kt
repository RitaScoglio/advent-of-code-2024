import java.io.File
import java.lang.Math.abs

class Day02 {
    fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    fun main() {
        val input = readFileAsList("input.txt")
            .map { row -> row.split(" ").map { it.toInt() } }
        println(firstStar(input))
        println(secondStar(input))
    }

    fun checkCorrectMissOne(row: List<Int>): Boolean = row.indices.map { index ->
        val changed = row.toMutableList()
        changed.removeAt(index)
        checkCorrect(changed)
    }.contains(true)

    fun checkCorrect(row: List<Int>): Boolean = (row.sorted() == row || row.sortedDescending() == row)
            && row.windowed(2, 1).map { abs(it[0] - it[1]) in (1..3) }.all { it == true }

    fun firstStar(input: List<List<Int>>): Int = input.map { checkCorrect(it) }.count { it }

    fun secondStar(input: List<List<Int>>): Int = input.map { row -> checkCorrectMissOne(row) }.count { it }

}