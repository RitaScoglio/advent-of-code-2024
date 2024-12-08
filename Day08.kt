import java.io.File

class Day08 {

    data class Coors(val row: Int, val col: Int) {
        operator fun plus(other: Coors): Coors {
            return Coors(this.row + other.row, this.col + other.col)
        }

        operator fun minus(other: Coors): Coors {
            return Coors(this.row - other.row, this.col - other.col)
        }

        operator fun times(mul: Int): Coors {
            return Coors(this.row * mul, this.col * mul)
        }

        fun exists(coors: Coors): Boolean {
            return row >= 0 && col >= 0 && row < coors.row && col < coors.col
        }
    }

    fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    fun main() {
        val input = readFileAsList("input.txt").map { row -> row.split("").filter { it != "" } }
        val antennas = mutableMapOf<String, List<Coors>>()
        input.indices.forEach { row ->
            input[row].indices.forEach { col ->
                if (input[row][col] != ".")
                    antennas.merge(input[row][col], listOf(Coors(row, col))) { new, old -> new + old }
            }
        }
        //first star
        println(allAntinodes(antennas, input.size, true))
        //second star
        println(allAntinodes(antennas, input.size, false))
    }

    private fun allAntinodes(antennas: MutableMap<String, List<Coors>>, size: Int, firstStar: Boolean): Int {
        val antinodes = mutableSetOf<Coors>()
        antennas.values.forEach { list ->
            if (list.size > 1) {
                (0 until list.size - 1).forEach { first ->
                    (first + 1 until list.size).forEach { second ->
                        val difference = Coors(list[first].row - list[second].row, list[first].col - list[second].col)
                        if (firstStar) {
                            (list[first] + difference).let { if (it.exists(Coors(size, size))) antinodes.add(it) }
                            (list[second] - difference).let { if (it.exists(Coors(size, size))) antinodes.add(it) }
                        } else {
                            antinodes.addAll(obtainAntinodes(list[first].copy(), difference, size, Coors::plus))
                            antinodes.addAll(obtainAntinodes(list[second].copy(), difference, size, Coors::minus))
                        }
                    }
                }
            }
        }
        return antinodes.size
    }

    private fun obtainAntinodes(
        start: Coors,
        difference: Coors,
        size: Int,
        function: (Coors, Coors) -> Coors
    ): MutableSet<Coors> {
        var antinode = start
        val antinodes = mutableSetOf<Coors>()
        while (antinode.exists(Coors(size, size))) {
            antinodes.add(antinode)
            antinode = function(antinode, difference)
        }
        return antinodes
    }
}