import java.io.File
import java.util.Collections
import kotlin.math.pow

class Day07 {

    fun readFileAsList(fileName: String): List<String> = File(fileName).useLines { it.toList() }

    fun main() {
        val equations = readFileAsList("input.txt").map { equation ->
            equation.split(": ").map { numbers -> numbers.split(" ").map { it.toLong() } }
        }
        //first star
        println(allPossibilities(equations, 2.0))
        //second star
        println(allPossibilities(equations, 3.0))
    }

    private fun allPossibilities(equations: List<List<List<Long>>>, numOperators: Double) =
        equations.sumOf { (test, numbers) ->
            if (checkEquation(test, numbers, numOperators))
                test[0].toLong()
            else
                0
        }


    private fun checkEquation(test: List<Long>, numbers: List<Long>, numOperators: Double): Boolean {
        (0..numOperators.pow(numbers.size - 1.toDouble()).toInt()).forEach {
            val combination = it.toString(numOperators.toInt()).padStart(numbers.size - 1, '0')
            val evaluated = (1..numbers.size - 1).fold(numbers[0]) { acc, i ->
                if (combination[i - 1] == '0') acc + numbers[i]
                else if (combination[i - 1] == '1') acc * numbers[i]
                else (acc.toString() + numbers[i].toString()).toLong()
            }
            if (evaluated == test[0].toLong())
                return true
        }
        return false
    }
}