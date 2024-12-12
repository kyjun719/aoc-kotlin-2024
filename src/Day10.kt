fun main() {
    fun find(y: Int, x: Int, board: List<IntArray>, visited: List<BooleanArray>, reuse: Boolean = false): Int {
        if (board[y][x] == 9) return 1

        var sum = 0

        listOf(
            Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1)
        ).forEach {
            val nextY = y + it.first
            val nextX = x + it.second

            if (nextY in 0 until board.size && nextX in 0 until board[0].size) {
                if (!visited[nextY][nextX]) {
                    if (board[nextY][nextX] - board[y][x] == 1) {
                        visited[nextY][nextX] = true
                        sum += find(nextY, nextX, board, visited, reuse)
                        if (reuse) {
                            visited[nextY][nextX] = false
                        }
                    }
                }
            }
        }

        return sum
    }

    fun part1(lines: List<String>): Int {
        val board = lines.map { it.map { c -> c.code - 48 }.toIntArray() }

        var sum = 0

        for (y in board.indices) {
            for (x in board[0].indices) {
                if (board[y][x] == 0) {
                    val ret = find(y, x, board, List(board.size) { BooleanArray(board[0].size) } )
                    sum += ret
                }
            }
        }

        return sum
    }

    fun part2(lines: List<String>): Int {
        val board = lines.map { it.map { c -> c.code - 48 }.toIntArray() }

        var sum = 0

        for (y in board.indices) {
            for (x in board[0].indices) {
                if (board[y][x] == 0) {
                    val ret = find(y, x, board, List(board.size) { BooleanArray(board[0].size) },
                        reuse = true
                    )
                    sum += ret
                }
            }
        }

        return sum
    }

    val testInput = readInput("Day10_test")
    val input = readInput("Day10")


    check(part1(testInput) == 36) {
        "expected: 36, result: ${part1(testInput)}"
    }
    // 548
    println(part1(input))

    check(part2(testInput) == 81) {
        "expected: 81, result: ${part2(testInput)}"
    }
    // 1252
    println(part2(input))
}
