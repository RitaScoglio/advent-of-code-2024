import java.io.File
import kotlin.math.pow

class Day11 {

    fun readFileAsString(fileName: String): String
            = File(fileName).readText(Charsets.UTF_8)

    fun main() {
        val stones = readFileAsString("input.txt").split(" ").filter { it != "" }.map{ it.toLong() }.associateWith { 1.toLong() }
        //first star
        println(simulateBlinking(stones, 25))
        //second star
        println(simulateBlinking(stones, 75))
    }

    private fun simulateBlinking(input: Map<Long, Long>, blinks: Int): Long {
        var stones = input.toMutableMap()
        repeat(blinks) {
            val newStones = mutableMapOf<Long, Long>()
            stones.forEach { (stone, occurence) ->
                if (stone == 0.toLong())
                    newStones.merge(1, occurence.toLong()) { new, old -> new + old }
                else if (stone.toString().length % 2 == 0) {
                    val number = stone.toString()
                    newStones.merge(number.take(number.length / 2).toLong(), occurence.toLong()) { new, old -> new + old }
                    newStones.merge(number.takeLast(number.length / 2).toLong(), occurence.toLong()) { new, old -> new + old }
                } else {
                    newStones.merge(stone * 2024, occurence.toLong()) { new, old -> new + old }
                }
            }
            stones = newStones
        }
        return stones.values.sum()
    }
}