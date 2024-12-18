import java.io.File
import java.util.*
import kotlin.math.pow

class Day18 {

    data class Coors(val row: Int, val col: Int) {
        operator fun plus(other: Coors): Coors {
            return Coors(this.row + other.row, this.col + other.col)
        }

        fun exists(size: Int): Boolean {
            return row >= 0 && col >= 0 && row < size && col < size
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

    val BYTES = 1024
    val SIZE = 71

    fun main() {
        val memorySpace = MutableList(SIZE) { MutableList(SIZE) { "." } }
        val bytes = readFileAsList("input.txt").map { coors -> coors.split(",").map { it.toInt() } }
        var numBytes = 0
        while (numBytes < BYTES) {
            memorySpace[bytes[numBytes][0]][bytes[numBytes][1]] = "#"
            numBytes++
        }
        //first star
        println(simulateSteps(memorySpace))
        do {
            memorySpace[bytes[numBytes][0]][bytes[numBytes][1]] = "#"
            val pathSize = simulateSteps(memorySpace)
            numBytes++
        } while (pathSize != 0)
        //second star
        println(bytes[numBytes - 1].joinToString(","))
    }

    private fun simulateSteps(map: List<List<String>>): Int {
        val coorsSteps = mutableMapOf(Coors(0, 0) to 0)
        val toBeSearched = mutableListOf(Pair(Coors(0, 0), 0))
        val finish = Coors(map.size - 1, map.size - 1)
        var numSteps = 0
        while (toBeSearched.isNotEmpty()) {
            val (coors, steps) = toBeSearched.first()
            toBeSearched.removeFirst()
            if (coors == finish) {
                numSteps = steps
                break
            } else Direction.values().forEach { direction ->
                val newCoors = coors + direction.move
                if (newCoors.exists(SIZE)) {
                    if (map[newCoors.row][newCoors.col] != "#") {
                        if (coorsSteps.getOrDefault(newCoors, Int.MAX_VALUE) >= steps + 1 &&
                            Pair(newCoors, steps + 1) !in toBeSearched) {
                            coorsSteps[newCoors] = steps + 1
                            toBeSearched.add(Pair(newCoors, steps + 1))
                        }
                    }
                }
            }
        }
        return numSteps
    }
}