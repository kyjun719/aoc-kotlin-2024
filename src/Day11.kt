fun main() {
    var cacheMap: MutableMap<Pair<Int, Long>, Long> = mutableMapOf()

    fun changeStone(totalDepth: Int, depth: Int, v: Long): Long {
        if (totalDepth == depth) return 1

        cacheMap[Pair(depth, v)]?.let {
            return it
        }

        var ret = 0L
        if (v == 0L) {
            ret = changeStone(totalDepth, depth+1, 1L)
        } else if (v.toString().length % 2 == 0) {
            val length = v.toString().length

            ret = changeStone(totalDepth, depth+1, v.toString().slice(0..length/2-1).toLong())
            ret += changeStone(totalDepth, depth+1, v.toString().slice(length/2..length-1).toLong())
        } else {
            ret = changeStone(totalDepth, depth+1, v*2024)
        }

        cacheMap[Pair(depth, v)] = ret

        return ret
    }

    fun part1(lines: List<String>): Long {
        cacheMap = mutableMapOf()

        return lines[0].split(" ").map { it.toLong() }.sumOf {
            changeStone(25, 0, it)
        }
    }

    fun part2(lines: List<String>): Long {
        cacheMap = mutableMapOf()

        return lines[0].split(" ").map { it.toLong() }.sumOf {
            changeStone(75, 0, it)
        }
    }

    val testInput = readInput("Day11_test")
    val input = readInput("Day11")


    check(part1(testInput) == 55312L) {
        "expected: 55312, result: ${part1(testInput)}"
    }
    // 182081
    println(part1(input))

    check(part2(testInput) == 65601038650482L) {
        "expected: 65601038650482, result: ${part2(testInput)}"
    }
    // 216318908621637
    println(part2(input))
}
