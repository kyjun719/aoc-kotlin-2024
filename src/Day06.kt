
sealed class Direction(val row: Int, val col: Int) {
    data object UP: Direction(-1, 0)
    data object RIGHT: Direction(0, 1)
    data object DOWN: Direction(1, 0)
    data object LEFT: Direction(0, -1)
}

fun main() {
    fun getNextDir(dir: Direction): Direction {
        return when(dir) {
            Direction.UP -> Direction.RIGHT
            Direction.RIGHT -> Direction.DOWN
            Direction.DOWN -> Direction.LEFT
            Direction.LEFT -> Direction.UP
        }
    }

    fun findStartPoint(lines: List<String>): Pair<Int, Int> {
        for (i in lines.indices) {
            if (lines[i].contains("^")) {
                return Pair(i, lines[i].indexOf("^"))
            }
        }

        return Pair(0, 0)
    }

    fun calcEscapeCount(r: Int, c: Int, board: List<CharArray>): Int {
        var moveCnt = 1
        var dir: Direction = Direction.UP
        var row = r
        var col = c
        val turnPointList = mutableListOf<Triple<Int, Int, Direction>>()
        while (true) {
            val nextRow = row + dir.row
            val nextCol = col + dir.col
            if (nextRow < 0 || nextRow >= board.size || nextCol < 0 || nextCol >= board[0].size) {
                break
            }

            if (board[nextRow][nextCol] == '#') {
                dir = getNextDir(dir)

                val tri = Triple(nextRow, nextCol, dir)
                if (turnPointList.contains(tri)) {
                    moveCnt = -1
                    break
                }

                turnPointList.add(Triple(nextRow, nextCol, dir))
            } else {
                if (board[nextRow][nextCol] == '.') {
                    moveCnt++
                    board[nextRow][nextCol] = 'X'
                }
                row = nextRow
                col = nextCol
            }
        }

        return moveCnt
    }

    fun part1(lines: List<String>): Int {
        val (row, col) = findStartPoint(lines)

        val board = lines.map { it.toCharArray() }
        board[row][col] = 'X'

        return calcEscapeCount(row, col, board)
    }

    fun part2(lines: List<String>): Int {
        val (row, col) = findStartPoint(lines)

        val board = lines.map { it.toCharArray() }
        var nc = 0

        val calculatedBoard = board.toList().map { it.copyOf() }
        calcEscapeCount(row, col, calculatedBoard)

        for (i in lines.indices) {
            for (j in lines[0].indices) {
                if (calculatedBoard[i][j] == 'X') {
                    val boardCopy = board.toList().map { it.copyOf() }
                    boardCopy[i][j] = '#'

                    if (calcEscapeCount(row, col, boardCopy) == -1) nc++
                }
            }
        }

        return nc
    }

    val testInput = readInput("Day06_test")
    val input = readInput("Day06")


    check(part1(testInput) == 41) {
        "expected: 41, result: ${part1(testInput)}"
    }
    // 5534
    println(part1(input))

    check(part2(testInput) == 6) {
        "expected: 6, result: ${part2(testInput)}"
    }
    // 2262
    println(part2(input))
}
