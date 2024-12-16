import java.io.File

class Day15 {

    data class Coors(val row: Int, val col: Int) {
        operator fun plus(other: Coors): Coors {
            return Coors(this.row + other.row, this.col + other.col)
        }
    }

    data class Direction(val move: Char) {
        var direction: Coors = when (move) {
            '^' -> Coors(-1, 0)
            'v' -> Coors(1, 0)
            '<' -> Coors(0, -1)
            else -> Coors(0, 1)
        }

    }

    fun readFileAsString(fileName: String): String = File(fileName).readText(Charsets.UTF_8)

    fun main() {
        val (mapString, movements) = readFileAsString("input.txt").split("\n\n")
        //first star
        println(simulateRobot(mapString, movements, false))
        //second star
        println(simulateRobot(mapString, movements, true))
    }

    private fun simulateRobot(mapString: String, movements: String, secondStar: Boolean): Int {
        val map = mutableMapOf<Coors, String>()
        var robotCoors = Coors(0, 0)
        mapString.split("\n").let { list ->
            list.indices.forEach { row ->
                val line = if (secondStar) list[row].split("").joinToString("") {
                    when (it) {
                        "." -> ".."
                        "#" -> "##"
                        "O" -> "[]"
                        "@" -> "@."
                        else -> ""
                    }
                } else list[row]
                line.indices.forEach { col ->
                    if (line[col] == '@')
                        robotCoors = Coors(row, col)
                    map[Coors(row, col)] = line[col].toString()
                }
            }
        }
        movements.replace("\n", "").forEach { move ->
            val direction = Direction(move).direction
            var coors = mutableListOf(robotCoors)
            val boxesAhead = mutableListOf<MutableList<Coors>>()
            do {
                boxesAhead.add(coors)
                coors = coors.map { it + direction }.toMutableList()
                if ((move == '^' || move == 'v') && secondStar) {
                    coors.filter { map[it] == "]" }
                        .forEach { if (it + Coors(0, -1) !in coors) coors.add(it + Coors(0, -1)) }
                    coors.filter { map[it] == "[" }
                        .forEach { if (it + Coors(0, 1) !in coors) coors.add(it + Coors(0, 1)) }
                }
                coors = coors.filter { map[it]!! != "." }.toMutableList()
            } while (coors.isNotEmpty() && !coors.map { map[it] }.any { it == "#" })
            if (coors.isEmpty()) {
                boxesAhead.reversed().forEach { list ->
                    list.forEach { coors ->
                        map[coors + direction] = map[coors]!!
                        map[coors] = "."
                    }
                }
                map[robotCoors] = "."
                robotCoors += direction
            }
        }
        return map.filter { if (secondStar) it.value == "[" else it.value == "O" }.keys.sumOf { coors -> coors.row * 100 + coors.col }
    }
}