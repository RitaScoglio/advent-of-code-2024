import java.io.File
import java.util.PriorityQueue

class Day16 {

    data class Coors(val row: Int, val col: Int) {
        operator fun plus(other: Coors): Coors {
            return Coors(this.row + other.row, this.col + other.col)
        }
    }

    enum class Direction {
        NORTH {
            override val move = Coors(-1, 0)
        },
        SOUTH {
            override val move = Coors(1, 0)
        },
        WEST {
            override val move = Coors(0, -1)
        },
        EAST {
            override val move = Coors(0, 1)
        };

        abstract val move: Coors
    }

    fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    fun main() {
        val map = readFileAsList("input.txt").map { line -> line.split("").filter { it != "" }.toMutableList() }
        val (firstStar, secondStar) = simulateReindeerRun(map)
        println(firstStar)
        println(secondStar)
    }

    private fun simulateReindeerRun(map: List<List<String>>): Pair<Long, Int> {
        val smallestPaths = mutableListOf<List<Coors>>()
        var smallestScore = Long.MAX_VALUE
        val scores = mutableMapOf<Pair<Coors, Direction>, Long>()
        val queue = PriorityQueue<Pair<Long, List<Pair<Coors, Direction>>>>(compareBy { it.first })
        queue.add(Pair(0.toLong(), listOf(Pair(Coors(map.size - 2, 1), Direction.EAST))))
        while (queue.isNotEmpty()) {
            val (score, path) = queue.poll()
            val (lastPosition, lastDirection) = path.last()
            if (map[lastPosition.row][lastPosition.col] == "E") {
                if (score < smallestScore) {
                    smallestScore = score
                    smallestPaths.clear()
                    smallestPaths.add(path.map { it.first })
                } else if (score == smallestScore)
                    smallestPaths.add(path.map { it.first })
            } else Direction.values().forEach { direction ->
                val newPosition = lastPosition + direction.move
                if (map[newPosition.row][newPosition.col] != "#") {
                    val newScore = score.let { if (direction == lastDirection) it + 1 else it + 1001 }
                    if (scores.getOrDefault(Pair(newPosition, direction), Long.MAX_VALUE) >= newScore) {
                        scores[Pair(newPosition, direction)] = newScore
                        queue.add(Pair(newScore, path + Pair(newPosition, direction)))
                    }
                }
            }
            queue.removeAll { it.first > smallestScore }
        }
        return Pair(smallestScore, smallestPaths.flatten().toSet().size)
    }
}