import java.io.File
import java.util.Collections

class Day06 {

    data class Coors(val row: Int, val col: Int) {
        operator fun plus(other: Coors): Coors {
            return Coors(this.row + other.row, this.col + other.col)
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

    fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    fun main() {
        val input = readFileAsList("input.txt")
        var originalGuard = Coors(0, 0)
        val obstructions = mutableListOf<Coors>()
        (input.indices).forEach { i ->
            (0 until input[i].length).forEach { j ->
                if (input[i][j] == '^')
                    originalGuard = Coors(i, j)
                else if (input[i][j] == '#')
                    obstructions.add(Coors(i, j))
            }
        }

        println(firstStar(input, originalGuard, obstructions, false).size)
        println(secondStar(input, originalGuard, obstructions))
    }

    private fun secondStar(input: List<String>, guard: Coors, obstructions: MutableList<Coors>): Any {
        val possibleObstructions = firstStar(input, guard, obstructions, false)
        var count = 0
        possibleObstructions.forEach { obstruction ->
            obstructions.add(obstruction)
            if (firstStar(input, guard, obstructions, true).isEmpty())
                count++
            obstructions.remove(obstruction)
        }
        return count
    }

    private fun firstStar(
        input: List<String>,
        guard: Coors,
        obstructions: MutableList<Coors>,
        secondStar: Boolean
    ): Set<Coors> {
        var currentDirection = Direction().up
        val guardSteps = mutableListOf(Pair(guard, currentDirection))
        while ((guardSteps.last().first + currentDirection).exists(Coors(input.size, input[0].length))) {
            val newGuard = guardSteps.last().first + currentDirection
            if (newGuard !in obstructions) {
                if (Pair(newGuard, currentDirection) in guardSteps && secondStar) {
                    guardSteps.clear()
                    break
                } else
                    guardSteps.add(Pair(newGuard, currentDirection))
            } else
                when (currentDirection) {
                    Direction().up -> currentDirection = Direction().right
                    Direction().right -> currentDirection = Direction().down
                    Direction().down -> currentDirection = Direction().left
                    Direction().left -> currentDirection = Direction().up
                }
        }
        return guardSteps.map { it.first }.toSet()
    }
}