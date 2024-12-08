fun main() {
    fun createPointMap(lines: List<String>): Map<Char, List<Pair<Int, Int>>> {
        val pointMap = mutableMapOf<Char, MutableList<Pair<Int, Int>>>()

        lines.forEachIndexed { i, line ->
            line.forEachIndexed { j, c ->
                val code = c.code
                if (
                    (code in 48..57) || //0~9
                    (code in 65..90) || //A~Z
                    (code in 97..122)   //a~z
                ) {
                    pointMap.getOrPut(c) {
                        mutableListOf()
                    }.add(Pair(i, j))
                }
            }
        }

        return pointMap
    }

    fun runCoupleInEntries(map: Map<Char, List<Pair<Int, Int>>>, callback: (Pair<Int, Int>, Pair<Int, Int>) -> Unit) {
        map.entries.forEach {
            it.value.forEachIndexed { index, p1 ->
                it.value.slice(index+1 .. it.value.size-1).forEach { p2 ->
                    callback(p1, p2)
                }
            }
        }
    }

    fun checkBound(y: Int, x: Int, ty: Int, tx: Int, callback: ((Int, Int) -> Unit)? = null): Boolean {
        if ((y in 0..ty-1) && (x in 0..tx-1)) {
            callback?.invoke(y, x)
            return true
        }
        return false
    }

    fun part1(lines: List<String>): Int {
        val pointMap = createPointMap(lines)

        val plist = mutableSetOf<Pair<Int, Int>>()

        runCoupleInEntries(pointMap) { p1, p2 ->
            val diff = Pair(p1.first - p2.first, p1.second - p2.second)
            checkBound(p1.first + diff.first, p1.second + diff.second, lines.size, lines[0].length) { y, x ->
                plist.add(Pair(y, x))
            }
            checkBound(p2.first - diff.first, p2.second - diff.second, lines.size, lines[0].length) { y, x ->
                plist.add(Pair(y, x))
            }
        }

        return plist.size
    }

    fun part2(lines: List<String>): Int {
        val pointMap = createPointMap(lines)

        val plist = mutableSetOf<Pair<Int, Int>>()

        runCoupleInEntries(pointMap) { p1, p2 ->
            val diff = Pair(p1.first - p2.first, p1.second - p2.second)

            var ny = p1.first
            var nx = p1.second
            var result = true

            while(result) {
                result = checkBound(ny, nx, lines.size, lines[0].length) { y, x ->
                    plist.add(Pair(y, x))
                }
                ny += diff.first
                nx += diff.second
            }

            ny = p2.first
            nx = p2.second
            result = true
            while(result) {
                result = checkBound(ny, nx, lines.size, lines[0].length) { y, x ->
                    plist.add(Pair(y, x))
                }
                ny -= diff.first
                nx -= diff.second
            }
        }

        return plist.size
    }

    val testInput = readInput("Day08_test")
    val input = readInput("Day08")


    check(part1(testInput) == 14) {
        "expected: 14, result: ${part1(testInput)}"
    }
    // 423
    println(part1(input))

    check(part2(testInput) == 34) {
        "expected: 34, result: ${part2(testInput)}"
    }
    // 1287
    println(part2(input))
}
