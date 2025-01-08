import java.io.File

class Day19 {

    fun readFileAsString(fileName: String): String = File(fileName).readText(Charsets.UTF_8)

    fun main() {
        val input = readFileAsString("input.txt").split("\n\n")
        val towels = input[0].split(", ").sortedByDescending { it.length }
        val designs = input[1].split("\n")
        println(firstStar(towels, designs))
        println(secondStar(towels, designs))
    }

    private fun firstStar(towels: List<String>, designs: List<String>) = designs.filter { getAllDesigns(it, towels, mutableMapOf()) > 0 }.size

    private fun secondStar(towels: List<String>, designs: List<String>) = designs.sumOf { getAllDesigns(it, towels, mutableMapOf()) }

    private fun getAllDesigns(design: String, towels: List<String>, alreadyComputed: MutableMap<String, Long>): Long {
        return if (design == "") 1
        else alreadyComputed.getOrPut(design) {
            towels.sumOf { towel ->
                if (design.startsWith(towel)) {
                    getAllDesigns(design.removePrefix(towel), towels, alreadyComputed)
                } else 0
            }
        }
    }
}