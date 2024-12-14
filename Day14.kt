import java.io.File

class Day14 {

    data class Robot(var p_x: Int, var p_y: Int, val v_x: Int, val v_y: Int) {
        fun move(fieldSize: Coors) {
            p_x = (p_x + v_x).mod(fieldSize.x)
            p_y = (p_y + v_y).mod(fieldSize.y)
        }
    }

    data class Coors(val x: Int, val y: Int)

    fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    val FIELD_SIZE = Coors(101, 103)

    fun main() {
        val robots = readFileAsList("input.txt").map { line ->
            line.filter { it != '=' && it != 'p' && it != 'v' }.split(" ", ",") }
            .map { robot -> Robot(robot[0].toInt(), robot[1].toInt(), robot[2].toInt(), robot[3].toInt()) }
        println(firstStar(robots))
        println(secondStar(robots) + 100)
    }

    fun firstStar(robots: List<Robot>): Int {
        repeat(100) {
            robots.map { robot ->
                robot.move(FIELD_SIZE)
            }
        }
        val quadrants = mutableListOf(0, 0, 0, 0)
        robots.forEach { robot ->
            if (robot.p_x < FIELD_SIZE.x / 2 && robot.p_y < FIELD_SIZE.y / 2)
                quadrants[0]++
            else if (robot.p_x < FIELD_SIZE.x / 2 && robot.p_y > FIELD_SIZE.y / 2)
                quadrants[1]++
            else if (robot.p_x > FIELD_SIZE.x / 2 && robot.p_y < FIELD_SIZE.y / 2)
                quadrants[2]++
            else if (robot.p_x > FIELD_SIZE.x / 2 && robot.p_y > FIELD_SIZE.y / 2)
                quadrants[3]++
        }
        return quadrants.reduce { q, acc -> q * acc }
    }

    fun secondStar(robots: List<Robot>): Int {
        var allUnique = false
        var repetition = 0
        while (!allUnique) {
            val occupiedCoors = mutableSetOf<Coors>()
            robots.map { robot ->
                robot.move(FIELD_SIZE)
                occupiedCoors.add(Coors(robot.p_x, robot.p_y))
            }
            if (occupiedCoors.size == robots.size)
                allUnique = true
            repetition++
        }
        return repetition
    }
}