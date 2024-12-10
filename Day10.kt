import java.io.File

class Day10 {

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

    data class Direction(
        val up: Coors = Coors(-1, 0),
        val down: Coors = Coors(1, 0),
        val left: Coors = Coors(0, -1),
        val right: Coors = Coors(0, 1)
    )

    private val DIRECTIONS = listOf(Direction().up, Direction().down, Direction().left, Direction().right)

    private fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    fun main() {
        val trailmap = readFileAsList("input.txt").map { row -> row.split("").filter { it != "" }.map { it.toInt() } }
        var firstStart = 0
        var secondStar = 0
        trailmap.indices.forEach { row ->
            trailmap[row].indices.forEach { col ->
                if (trailmap[row][col] == 0)
                    simulateHiking(trailmap, Coors(row, col)).let {
                        firstStart += it.toSet().size
                        secondStar += it.size
                    }
            }
        }
        println(firstStart)
        println(secondStar)
    }

    private fun simulateHiking(trailmap: List<List<Int>>, trailhead: Coors): MutableList<Coors> {
        val queue = mutableListOf<List<Coors>>()
        val positionsOfNine = mutableListOf<Coors>()
        queue.add(listOf(trailhead))
        while (queue.isNotEmpty()) {
            val path = queue.first()
            queue.removeFirst()
            val last = path.last()
            if (trailmap[last.row][last.col] == 9)
                positionsOfNine.add(last)
            else DIRECTIONS.forEach { direction ->
                if ((last + direction).exists(Coors(trailmap.size, trailmap.size))) {
                    val newPos = last + direction
                    if (trailmap[newPos.row][newPos.col] == trailmap[last.row][last.col] + 1)
                        queue.add(path + newPos)
                }
            }
        }
        return positionsOfNine
    }

}