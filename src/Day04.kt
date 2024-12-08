fun main() {
    data class Node(
        val row: Int,
        val col: Int,
        val nowChar: Char,
        val dir: Pair<Int, Int>? = null
    )

    val pointPtr = listOf(
        Pair(-1, -1), Pair(-1, 0), Pair(-1, 1),
        Pair(0, -1), Pair(0, 1),
        Pair(1, -1), Pair(1, 0), Pair(1, 1)
    )

    fun createNextNode(nextChar: Char?, row: Int, col: Int, dir: Pair<Int, Int>, lines: List<String>): Node? {
        val nextRow = row + dir.first
        val nextCol = col + dir.second

        if (nextRow >= 0 && nextCol >= 0 && nextRow < lines.size && nextCol < lines[0].length) {
            if (lines[nextRow][nextCol] == nextChar) {
                return Node(nextRow, nextCol, nextChar, dir)
            }
        }

        return null
    }

    fun find(lines: List<String>, r: Int, c: Int): Int {
        val q = ArrayDeque<Node>()
        q.add(Node(r, c, 'X'))

        var ret = 0

        while (q.isNotEmpty()) {
            val (row, col, nowChar, dir) = q.removeFirst()

            val nextChar = when(nowChar) {
                'X' -> 'M'
                'M' -> 'A'
                'A' -> 'S'
                'S' -> ' '
                else -> null
            }

            if (nextChar == ' ') {
                ret++
                continue
            }

            if (dir == null) {
                pointPtr.forEach {
                    createNextNode(nextChar = nextChar, row = row, col = col, dir = it, lines = lines)?.let { node ->
                        q.add(node)
                    }
                }
            } else {
                createNextNode(nextChar = nextChar, row = row, col = col, dir = dir, lines = lines)?.let { node ->
                    q.add(node)
                }
            }
        }

        return ret
    }

    fun part1(lines: List<String>): Int {
        val row = lines.size
        val col = lines[0].length
        var ret = 0

        for (r in 0 until row) {
            for (c in 0 until col) {
                if (lines[r][c] == 'X') {
                    ret += find(lines, r, c)
                }
            }
        }

        return ret
    }

    fun part2(lines: List<String>): Int {
        val row = lines.size
        val col = lines[0].length
        var ret = 0

        for (r in 0 until row) {
            for (c in 0 until col) {
                if (lines[r][c] == 'A') {
                    if (r - 1 < 0 || c - 1 < 0 || r + 1 >= lines.size || c + 1 >= lines[0].length) {
                        continue
                    }
                    val left = "${lines[r-1][c-1]}${lines[r][c]}${lines[r+1][c+1]}"
                    val right = "${lines[r-1][c+1]}${lines[r][c]}${lines[r+1][c-1]}"
                    if ((left == "MAS" || left == "SAM") && (right == "MAS" || right == "SAM")) {
                        ret++
                    }
                }
            }
        }

        return ret
    }

    val testInput = readInput("Day04_test")
    val input = readInput("Day04")


    check(part1(testInput) == 18) {
        "expected: 18, result: ${part1(testInput)}"
    }
    // 2569
    println(part1(input))

    check(part2(testInput) == 9) {
        "expected: 9, result: ${part2(testInput)}"
    }
    // 1998
    println(part2(input))
}
