fun main() {
    var isPart2 = false
    data class BoxPtr(
        val ptrList: List<Pair<Int, Int>>
    )

    fun parseInput(lines: List<String>): Triple<Pair<Int, Int>, List<CharArray>, List<Char>> {
        var robotPtr = Pair(0, 0)
        val board = mutableListOf<CharArray>()

        val separateLineIndex = lines.indexOf("")

        for (i in 0 until separateLineIndex) {
            board.add(lines[i].toCharArray())
            if (lines[i].contains("@")) {
                robotPtr = Pair(i, lines[i].indexOf("@"))
            }
        }

        val dirList = lines.subList(separateLineIndex + 1, lines.size).map {
            it.toCharArray()
        }.fold(listOf<Char>()) { acc, chars ->
            acc + chars.toList()
        }

        return Triple(robotPtr, board.toList(), dirList)
    }

    fun isEmptyTile(board: List<CharArray>, ptr: Pair<Int, Int>): Boolean {
        return board[ptr.first][ptr.second] == '.'
    }

    fun isBoxTile(board: List<CharArray>, ptr: Pair<Int, Int>): Boolean {
        return if (isPart2) {
            listOf('[',']').contains(board[ptr.first][ptr.second])
        } else {
            board[ptr.first][ptr.second] == 'O'
        }
    }

    fun getBoxPtr(board: List<CharArray>, ptr: Pair<Int, Int>): BoxPtr {
        return if (isPart2) {
            if (board[ptr.first][ptr.second] == '[') {
                BoxPtr(listOf(ptr.copy(), Pair(ptr.first, ptr.second + 1)))
            } else {
                BoxPtr(listOf(Pair(ptr.first, ptr.second - 1), ptr.copy()))
            }
        } else {
            BoxPtr(listOf(ptr.copy()))
        }
    }

    fun getMovedBoxList(
        board: List<CharArray>, movedPtr: Pair<Int, Int>,
        dy: Int, dx: Int
    ): List<BoxPtr> {
        val movedBoxList = mutableListOf<BoxPtr>()

        val boxQueue = ArrayDeque<Pair<Int, Int>>()
        boxQueue.add(movedPtr.copy())

        while (boxQueue.isNotEmpty()) {
            val ptr = boxQueue.removeFirst()

            if (isBoxTile(board, ptr)) {
                val boxPtr = getBoxPtr(board, ptr)
                if (!movedBoxList.contains(boxPtr)) {
                    movedBoxList.add(boxPtr)
                    boxPtr.ptrList.forEach { boxP ->
                        boxQueue.add(Pair(boxP.first + dy, boxP.second + dx))
                    }
                }
            }
        }

        if (dx == 1) {
            movedBoxList.sortBy { boxPtr -> -boxPtr.ptrList.maxOf { it.second } }
        } else if (dx == -1) {
            movedBoxList.sortBy { boxPtr -> boxPtr.ptrList.maxOf { it.second } }
        } else if (dy == 1) {
            movedBoxList.sortBy { boxPtr -> -boxPtr.ptrList.maxOf { it.first } }
        } else if (dy == -1) {
            movedBoxList.sortBy { boxPtr -> boxPtr.ptrList.maxOf { it.first } }
        }

        return movedBoxList
    }

    fun conMoved(
        board: List<CharArray>,
        lastBoxPtrList: List<BoxPtr>,
        dy: Int,
        dx: Int
    ): Boolean {
        // 이동할 방향의 마지막 박스들이 움직일수 있는지
        return lastBoxPtrList.map { boxPtr ->
            val ptrMoved = boxPtr.ptrList.all {
                val next = it.plus(Pair(dy, dx))
                board[next.first][next.second] != '#'
            }

            if (ptrMoved) 1 else 0
        }.sum() == lastBoxPtrList.size
    }

    fun moveRobot(
        robotPtr: Pair<Int, Int>,
        board: List<CharArray>,
        dy: Int,
        dx: Int
    ): Pair<List<CharArray>, Pair<Int, Int>> {
        val movedPtr = Pair(robotPtr.first + dy, robotPtr.second + dx)

        var moved = false

        if (isEmptyTile(board, movedPtr)) {
            moved = true
        } else if (isBoxTile(board, movedPtr)) {
            // 각 방향의 움직일 수 있는 박스목록
            val movedBoxList = getMovedBoxList(board, movedPtr, dy, dx)

            // 박스 이동
            if (conMoved(board, movedBoxList, dy, dx)) {
                moved = true

                movedBoxList.forEach { boxPtr ->
                    if (boxPtr.ptrList.size == 1) {
                        val ptr = boxPtr.ptrList.first()
                        board[ptr.first + dy][ptr.second + dx] = 'O'
                        board[ptr.first][ptr.second] = '.'
                    } else {
                        val leftPtr = boxPtr.ptrList.first {
                            board[it.first][it.second] == '['
                        }
                        val rightPtr = boxPtr.ptrList.first {
                            board[it.first][it.second] == ']'
                        }

                        board[leftPtr.first][leftPtr.second] = '.'
                        board[rightPtr.first][rightPtr.second] = '.'

                        board[leftPtr.first + dy][leftPtr.second + dx] = '['
                        board[rightPtr.first + dy][rightPtr.second + dx] = ']'
                    }
                }
            }
        }

        if (moved) {
            board[movedPtr.first][movedPtr.second] = '@'
            board[robotPtr.first][robotPtr.second] = '.'

            return Pair(board, movedPtr)
        } else {
            return Pair(board, robotPtr)
        }
    }

    fun convertBoardPart2(board: List<CharArray>): MutableList<CharArray> {
        return board.map {
            mutableListOf<Char>().apply {
                it.forEach { c ->
                    when (c) {
                        '#' -> addAll(listOf('#','#'))
                        'O' -> addAll(listOf('[',']'))
                        '.' -> addAll(listOf('.','.'))
                        '@' -> addAll(listOf('@','.'))
                    }
                }
            }.toCharArray()
        }.toMutableList()
    }

    fun part1(lines: List<String>): Long {
        isPart2 = false
        var (robotPtr, board, dirList) = parseInput(lines)

        dirList.forEach { dir ->
            when (dir) {
                '<' -> Pair(0, -1)
                '>' -> Pair(0, 1)
                'v' -> Pair(1, 0)
                '^' -> Pair(-1, 0)
                else -> Pair(0, 0)
            }.run {
                moveRobot(robotPtr, board, first, second).let {
                    board = it.first
                    robotPtr = it.second
                }
            }
        }

        var sum = 0L

        board.forEachIndexed { y, chars ->
            chars.forEachIndexed { x, c ->
                if (c == 'O') {
                    sum += (y * 100L + x)
                }
            }
        }

        return sum
    }

    fun part2(lines: List<String>): Long {
        isPart2 = true
        var (robotPtr, board, dirList) = parseInput(lines)
        board = convertBoardPart2(board)
        robotPtr = robotPtr.copy(second = robotPtr.second * 2)

        dirList.forEach { dir ->
            when (dir) {
                '<' -> Pair(0, -1)
                '>' -> Pair(0, 1)
                'v' -> Pair(1, 0)
                '^' -> Pair(-1, 0)
                else -> Pair(0, 0)
            }.run {
                moveRobot(robotPtr, board, first, second).let {
                    board = it.first
                    robotPtr = it.second
                }
            }
        }

        var sum = 0L

        board.forEachIndexed { y, chars ->
            chars.forEachIndexed { x, c ->
                if (c == '[') {
                    sum += (y * 100L + x)
                }
            }
        }

        return sum
    }

    val testInput = readInput("Day15_test")
    val testInputSmall = readInput("Day15_test_small")
    val testPart2InputSmall = readInput("Day15_part2_test_small")
    val input = readInput("Day15")

    check(part1(testInputSmall) == 2028L) {
        "expected: 2028, result: ${part1(testInputSmall)}"
    }
    check(part1(testInput) == 10092L) {
        "expected: 10092, result: ${part1(testInput)}"
    }
    // 1486930
    println(part1(input))

    check(part2(testPart2InputSmall) == 618L) {
        "expected: 618, result: ${part2(testPart2InputSmall)}"
    }
    check(part2(testInput) == 9021L) {
        "expected: 9021, result: ${part2(testInput)}"
    }

    // 1492011
    println(part2(input))
}
