fun main() {
    data class Node(val value: Int) {
        val forwardList: MutableSet<Int> = mutableSetOf()
    }

    fun createNodeMap(ruleLines: List<String>): Map<Int, Node> {
        val nodeMap = mutableMapOf<Int, Node>()
        ruleLines.forEach { ruleLine ->
            val (forward, backward) = ruleLine.split("|").map(String::toInt)
            nodeMap.getOrPut(forward) { Node(forward) }

            val backwardNode = nodeMap.getOrPut(backward) { Node(backward) }

            backwardNode.forwardList.add(forward)
        }

        return nodeMap
    }

    fun isCorrectList(list: List<Int>, nodeMap: Map<Int, Node>): Boolean {
        for (i in list.size - 1 downTo 1) {
            val beforeNode = nodeMap[list[i]]
            if (beforeNode?.forwardList?.contains(list[i - 1]) == false) {
                return false
            }
        }
        return true
    }

    fun part1(lines: List<String>): Int {
        val inputDividerIdx = lines.indexOf("")
        val nodeMap = createNodeMap(lines.slice(0..inputDividerIdx - 1))
        val correctList = mutableListOf<List<Int>>()

        lines.slice(inputDividerIdx+1 .. lines.size - 1).forEach { line ->
            val list = line.split(",").map { it.toInt() }

            if (isCorrectList(list, nodeMap)) {
                correctList.add(list)
            }
        }

        return correctList.sumOf { it[it.size / 2] }
    }

    fun getZeroCountIndex(list: List<Int>, nodeMap: Map<Int, Node>): Int {
        val cnt = IntArray(list.size)
        list.forEach { n ->
            nodeMap[n]?.let { node ->
                list.forEachIndexed { index, i ->
                    if (i == n) return@forEachIndexed

                    if (node.forwardList.contains(i)) {
                        cnt[index]++
                    }
                }
            }
        }

        return cnt.indexOfLast { it == 0 }
    }

    fun part2(lines: List<String>): Int {
        val inputDividerIdx = lines.indexOf("")
        val nodeMap = createNodeMap(lines.slice(0..inputDividerIdx - 1))
        val incorrectList = mutableListOf<List<Int>>()

        lines.slice(inputDividerIdx+1 .. lines.size-1).forEach { line ->
            val list = line.split(",").map { it.toInt() }

            if (!isCorrectList(list, nodeMap)) {
                incorrectList.add(list)
            }
        }

        return incorrectList.sumOf { list ->
            var result = 0
            val mutableList = list.toMutableList()
            for (i in 0 until (list.size/2 + 1)) {
                val last = getZeroCountIndex(mutableList, nodeMap)
                result = mutableList[last]
                mutableList.removeAt(last)
            }

            result
        }
    }

    val testInput = readInput("Day05_test")
    val input = readInput("Day05")


    check(part1(testInput) == 143) {
        "expected: 143, result: ${part1(testInput)}"
    }
    // 6498
    println(part1(input))

    check(part2(testInput) == 123) {
        "expected: 123, result: ${part2(testInput)}"
    }
    //5017
    println(part2(input))
}
