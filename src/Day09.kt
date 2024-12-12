import kotlin.math.min

data class Segment(
    val numSize: Int,
    val numList: MutableList<Int>,
    var movedSize: Int = 0,
    val movedList: MutableList<Int> = mutableListOf()
) {
    fun convertToIntList(): List<Int> {
        return mutableListOf<Int>().apply {
            if (numList.isEmpty()) {
                addAll(List(numSize) { -1 })
            } else {
                addAll(numList)
            }
            addAll(movedList)
            addAll(List(movedSize) { -1 })
        }
    }
}

fun main() {
    fun createSegList(line: String): List<Segment> {
        return mutableListOf<Segment>().apply {
            line.forEachIndexed { index, c ->
                val count = c.code - 48
                if (index % 2 == 0) {
                    add(
                        Segment(numSize = count, numList = MutableList(count) { index/2 })
                    )
                } else {
                    get(index / 2).movedSize = count
                }
            }
        }
    }

    fun calcSum(segList: List<Segment>): Long {
        return segList.map { it.convertToIntList().toMutableList() }.reduce { acc, ints ->
            acc.addAll(ints)
            acc
        }.mapIndexed { index, i ->
            if (i == -1) 0L else index*(i.toLong())
        }.sum()
    }

    fun part1(lines: List<String>): Long {
        val segList = createSegList(lines[0])

        while (true) {
            val firstIdx = segList.indexOfFirst { it.movedSize > 0 }
            val lastIdx = segList.indexOfLast { it.numList.isNotEmpty() }
            if (firstIdx >= lastIdx) break

            val sliceSize = min(segList[firstIdx].movedSize, segList[lastIdx].numList.size)

            for (i in 0 until sliceSize) {
                segList[firstIdx].movedList.add(segList[lastIdx].numList.removeLast())
            }
            segList[firstIdx].movedSize -= sliceSize
        }

        return calcSum(segList)
    }

    fun part2(lines: List<String>): Long {
        val segList = createSegList(lines[0])

        var lastIdx = segList.lastIndex
        while (lastIdx > 0) {
            val firstIdx = segList.indexOfFirst {
                it.movedSize > 0 && it.movedSize >= segList[lastIdx].numSize
            }

            if (firstIdx == -1 || firstIdx >= lastIdx) {
                lastIdx--
                continue
            }

            val sliceSize = segList[lastIdx].numSize

            for (i in 0 until sliceSize) {
                segList[firstIdx].movedList.add(segList[lastIdx].numList.removeLast())
            }
            segList[firstIdx].movedSize -= sliceSize

            lastIdx--
        }

        return calcSum(segList)
    }

    val testInput = readInput("Day09_test")
    val input = readInput("Day09")


    check(part1(testInput) == 1928L) {
        "expected: 1928, result: ${part1(testInput)}"
    }
    // 6463499258318
    println(part1(input))

    check(part2(testInput) == 2858L) {
        "expected: 2858, result: ${part2(testInput)}"
    }
    // 6493634986625
    println(part2(input))
}
