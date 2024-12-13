import java.io.File

class Day13 {

    data class Coors(val x: Double, val y: Double)

    fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    fun main() {
        //firstStar
        println(simulatePlay(false))
        //secondStar
        println(simulatePlay(true))
    }

    private fun simulatePlay(secondStar: Boolean): Long {
        val machines = mutableListOf<Triple<Coors, Coors, Coors>>()
        readFileAsList("input.txt").chunked(4).forEach { machine ->
            val buttonA = getNumbers(machine, 0)
            val buttonB = getNumbers(machine, 1)
            val prize = getNumbers(machine, 2)
            machines.add(
                Triple(
                    Coors(buttonA[0], buttonA[1]),
                    Coors(buttonB[0], buttonB[1]),
                    Coors(prize[0].let { if (secondStar) it + 10000000000000 else it },
                        prize[1].let { if (secondStar) it + 10000000000000 else it })
                )
            )
        }
        return machines.map { (buttonA, buttonB, prize) ->
            val det = (buttonA.x * -buttonB.y) + (buttonB.x * buttonA.y)
            val A = ((prize.x * -buttonB.y) + (buttonB.x * prize.y)) / det
            val B = ((buttonA.x * -prize.y) + (prize.x * buttonA.y)) / det
            A * 3 + B * 1
        }.filter { it * 100 % 100 == 0.0 }.sum().toLong()
    }

    private fun getNumbers(machine: List<String>, i: Int) =
        machine[i].split(",").map { substring -> substring.filter { it.isDigit() }.toDouble() }
}