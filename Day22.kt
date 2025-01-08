import java.io.File

class Day22 {

    fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    fun main() {
        firstStar(readFileAsList("input.txt")).let { first ->
            println(first.sumOf { it.last() })
            println(secondStar(first))
        }
    }

    private fun secondStar(numbers: List<List<Long>>): Int {
        val map = mutableMapOf<List<Int>, List<Int>>()
        numbers.indices.forEach { index ->
            val prices = numbers[index].map { it.toString().last().digitToInt() }
            val sequences = prices.windowed(2).map { it[1] - it[0] }.windowed(4)
            sequences.toSet().forEach { sequence ->
                map.merge(sequence, listOf(prices[sequences.indexOf(sequence) + 4])) { old, new -> old + new }
            }
        }
        return map.values.maxOf { it.sum() }
    }

    private fun firstStar(input: List<String>): List<List<Long>> {
        var numbers = input.map { listOf(it.toLong()) }
        repeat(2000) {
            numbers = numbers.map { list ->
                val first = (list.last()).xor(list.last() * 64).mod(16777216.toLong())
                val second = (first).xor(first / 32).mod(16777216.toLong())
                val third = (second).xor(second * 2048).mod(16777216.toLong())
                list + listOf(third)
            }
        }
        return numbers
    }
}