import java.io.File
import java.lang.Math.abs
import java.util.*

class Day20 {

    data class Coors(val row: Int, val col: Int) {
        operator fun plus(other: Coors): Coors {
            return Coors(this.row + other.row, this.col + other.col)
        }

        operator fun minus(coors: Coors): Int {
            return abs(row - coors.row) + abs(col - coors.col)
        }
    }

    enum class Direction {
        UP {
            override val move = Coors(-1, 0)
        },
        DOWN {
            override val move = Coors(1, 0)
        },
        LEFT {
            override val move = Coors(0, -1)
        },
        RIGHT {
            override val move = Coors(0, 1)
        };

        abstract val move: Coors
    }

    fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    fun main() {
        val input = readFileAsList("input.txt")
        lateinit var start: Coors
        lateinit var end: Coors
        input.indices.forEach { row ->
            input[row].indices.forEach { col ->
                if (input[row][col] == 'S')
                    start = Coors(row, col)
                else if (input[row][col] == 'E')
                    end = Coors(row, col)
            }
        }
        val path = findPath(input, start, end)
        //first star
        println(getShorcuts(path, 2))
        //second star
        println(getShorcuts(path, 20))
    }

    private fun getShorcuts(path: List<Coors>, maxDistance: Int): Int =
        (0 until path.size - 1).sumOf { i ->
            (i + maxDistance until path.size).sumOf { j ->
                (path[i] - path[j]).let { if (it in (2..maxDistance) && abs(i - j) - it >= 100) 1 else 0 }.toInt()
            }
        }

    private fun findPath(input: List<String>, start: Coors, end: Coors): List<Coors> {
        val visited = mutableListOf<Coors>()
        val queue = mutableListOf(Pair(listOf(start), 0.toLong()))
        while (queue.isNotEmpty()) {
            val (path, score) = queue.first()
            queue.removeFirst()
            val position = path.last()
            if (position == end)
                return path
            if (position !in visited) {
                visited.add(position)
                Direction.values().forEach { direction ->
                    val newCoors = position + direction.move
                    if (newCoors !in visited && input[newCoors.row][newCoors.col] != '#')
                        queue.add(Pair(path + listOf(newCoors), score + 1))
                }
            }
        }
        return listOf()
    }
}