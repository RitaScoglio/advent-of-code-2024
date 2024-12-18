import java.io.File
import kotlin.math.pow

class Day17 {

    data class Registers(var A: Long, var B: Long, var C: Long)

    fun readFileAsString(fileName: String): String = File(fileName).readText(Charsets.UTF_8)

    lateinit var registers: Registers

    fun main() {
        val input = readFileAsString("input.txt").split("\n\n")
        input[0].lines().map { line -> line.filter { it.isDigit() }.toInt() }
            .let { registers = Registers(it[0].toLong(), it[1].toLong(), it[2].toLong()) }
        val program = input[1].filter { it.isDigit() }.split("").filter { it != "" }.map { it.toInt() }
        println(firstStar(program, false).joinToString(","))
        println(secondStar(program)) //finished with luck and hard work by modifying function secondStar
    }

    private fun comboOperand(operand: Int): Long {
        return when (operand) {
            0 -> 0
            1 -> 1
            2 -> 2
            3 -> 3
            4 -> registers.A
            5 -> registers.B
            6 -> registers.C
            else -> -1
        }
    }

    private fun firstStar(program: List<Int>, secondStar: Boolean): MutableList<Int> {
        var pointer = 0
        val output = mutableListOf<Int>()
        while (pointer < program.size) {
            val operand = program[pointer + 1]
            when (program[pointer]) {
                0 -> registers.A = registers.A / 2.0.pow(comboOperand(operand).toDouble()).toLong()
                1 -> registers.B = registers.B xor operand.toLong()
                2 -> registers.B = comboOperand(operand).mod(8).toLong()
                3 -> if (registers.A != 0.toLong()) pointer = operand - 2
                4 -> registers.B = registers.B xor registers.C
                5 -> {
                    output.add(comboOperand(operand).mod(8))
                    if (secondStar) {
                        if (output.size > program.size)
                            break
                        else if (output != program.subList(0, output.size))
                            break
                    }
                }
                6 -> registers.B = registers.A / 2.0.pow(comboOperand(operand).toDouble()).toLong()
                7 -> registers.C = registers.A / 2.0.pow(comboOperand(operand).toDouble()).toLong()
            }
            pointer += 2
        }
        return output
    }

    private fun secondStar(program: List<Int>): Long {
        var registerA = 0.toLong()
        do {
            registers.A = registerA
            val output = firstStar(program, true)
            registerA += 256
        } while (output != program)
        return registerA
    }
}