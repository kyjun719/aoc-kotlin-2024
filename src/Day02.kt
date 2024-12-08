fun main() {
    fun createDiffList(line: String): List<Int> {
        return line.split(" ").map { it.toInt() }.run {
            mapIndexed { index, i ->
                if (index == 0) {
                    0
                } else {
                    i - get(index - 1)
                }
            }.drop(1)
        }
    }

    fun List<Int>.isElementSafe(): Boolean {
        return (min() >= 1 && max() <= 3) || (max() <= -1 && min() >= -3)
    }

    fun List<Int>.isSafeReport(): Boolean {
        return isElementSafe() && ( all { it > 0 } || all { it < 0 })
    }

    fun part1(lines: List<String>): Int {
        return lines.count { line ->
            createDiffList(line).isSafeReport()
        }
    }

    fun part2(lines: List<String>): Int {
        return lines.filter { line ->
            val intList = createDiffList(line)

            if (intList.isSafeReport()) {
                return@filter true
            }

            var result = false
            var removedIdx = -1
            var needAdditionalCalc = false

            val posCnt = intList.count { it > 0 }
            val negCnt = intList.count { it < 0 }

            // all pos or all neg
            if (posCnt == intList.size || negCnt == intList.size) {
                val burstCnt = intList.count { it > 3 || it < -3 }
                val burstIdx = intList.indexOfFirst { it > 3 || it < -3 }
                if (burstCnt == 1 && (burstIdx == 0 || burstIdx == intList.lastIndex)) {
                    removedIdx = burstIdx
                }
            }

            // all pos except 1
            if (posCnt + 1 == intList.size && negCnt == 1) {
                removedIdx = intList.indexOfFirst { it < 0 }
                needAdditionalCalc = true
            }

            // all neg except 1
            if (negCnt + 1 == intList.size && posCnt == 1) {
                removedIdx = intList.indexOfFirst { it > 0 }
                needAdditionalCalc = true
            }

            // all pos or neg, except 1 zero
            if (intList.count { it == 0 } == 1) {
                removedIdx = intList.indexOfFirst { it == 0 }
            }

            if (removedIdx != -1) {
                result = intList.toMutableList().apply {
                    if (needAdditionalCalc) {
                        if (removedIdx != lastIndex) {
                            set(removedIdx + 1, get(removedIdx + 1) + get(removedIdx))
                        }
                    }
                    removeAt(removedIdx)
                }.isElementSafe()

                if (!result && needAdditionalCalc) {
                    result = intList.toMutableList().apply {
                        if (removedIdx != 0) {
                            set(removedIdx - 1, get(removedIdx - 1) + get(removedIdx))
                        }
                        removeAt(removedIdx)
                    }.isElementSafe()
                }
            }

            return@filter result
        }.count()
    }

    val testInput = readInput("Day02_test")
    val input = readInput("Day02")


    check(part1(testInput) == 2) {
        "expected: 2, result: ${part1(testInput)}"
    }
    // 572
    println(part1(input))


    check(part2(testInput) == 4) {
        "expected: 4, result: ${part2(testInput)}"
    }
    //612
    println(part2(input))
}
