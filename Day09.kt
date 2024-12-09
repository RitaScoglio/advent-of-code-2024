import java.io.File

class Day09 {

    fun readFileAsString(fileName: String): String = File(fileName).readText(Charsets.UTF_8)

    fun main() {
        val input = readFileAsString("input.txt").split("").filter { it != "" }.chunked(2)
        val diskMap = mutableListOf<Pair<Long, Long>>()
        input.indices.forEach { index ->
            diskMap.add(Pair(input[index][0].toLong(), index.toLong()))
            if (input[index].size > 1) {
                diskMap.add(Pair(input[index][1].toLong(), -1))
            }
        }
        println(getSum(firstStar(diskMap)))
        println(getSum(secondStar(diskMap)))
    }

    private fun getSum(diskMap: MutableList<Pair<Long, Long>>): Long {
        val newDisk = diskMap.map { (size, number) ->
            if (number > 0)
                List(size.toInt()) { number }
            else
                List(size.toInt()) { 0.toLong() }
        }.flatten()
        return newDisk.indices.sumOf { it.toLong() * newDisk[it] }
    }

    private fun min(first: Long, second: Long): Long = if (first < second) first else second

    private fun firstStar(map: MutableList<Pair<Long, Long>>): MutableList<Pair<Long, Long>> {
        var diskMap = map.toMutableList()
        var index = diskMap.size - 1
        while (diskMap.subList(0, index).filter { it.second < 0 }.sumOf { it.first } > 0) {
            if (diskMap[index].second > 0) {
                val exchange = diskMap.indexOfFirst { it.second < 0 && it.first > 0 }
                if (exchange in 0 until index) {
                    val min = min(diskMap[exchange].first, diskMap[index].first)
                    diskMap = (diskMap.subList(0, exchange) +
                            Pair(min, diskMap[index].second) +
                            Pair(diskMap[exchange].first - min, (-1).toLong()) +
                            diskMap.subList(exchange + 1, index) +
                            Pair(diskMap[index].first - min, diskMap[index].second)).toMutableList()
                }
                if (diskMap[index + 1].first > 0)
                    index += 2
            }
            index--
        }
        return diskMap.subList(0, index + 1)
    }

    private fun secondStar(map: MutableList<Pair<Long, Long>>): MutableList<Pair<Long, Long>> {
        var diskMap = map.toMutableList()
        var index = diskMap.size - 1
        while (index > 0) {
            if (diskMap[index].second > 0) {
                val exchange = diskMap.indexOfFirst { it.second < 0 && it.first >= diskMap[index].first }
                if (exchange in 0 until index) {
                    diskMap = (diskMap.subList(0, exchange) +
                            diskMap[index] +
                            Pair(diskMap[exchange].first - diskMap[index].first, (-1).toLong()) +
                            diskMap.subList(exchange + 1, diskMap.size)).toMutableList()
                    diskMap[index + 1] = Pair(diskMap[index + 1].first, -1)
                }
            }
            index--
        }
        return diskMap
    }
}