fun main() {
    var visited: List<BooleanArray> = listOf()
    var board: List<CharArray> = listOf()

    fun findPoint(y: Int, x: Int, c: Char): List<Pair<Int, Int>> {
        val list = mutableListOf(
            Pair(y,x)
        )

        listOf(
            Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1)
        ).forEach {
            val nextY = y + it.first
            val nextX = x + it.second

            if (nextY in 0..board.size-1 && nextX in 0..board[0].size-1) {
                if (!visited[nextY][nextX] && board[nextY][nextX] == c) {
                    visited[nextY][nextX] = true
                    list.addAll(findPoint(nextY, nextX, c))
                }
            }
        }

        return list
    }

    fun calcPerimeter(list: List<Pair<Int, Int>>): Long {
        var sum = 4L * list.size

        list.forEach { point ->
            listOf(
                Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1)
            ).forEach { diff ->
                if (list.contains(point.plus(diff))) {
                    sum--
                }
            }
        }
        return sum
    }

    fun part1(lines: List<String>): Long {
        var ret = 0L

        board = lines.map { it.toCharArray() }
        visited = List(board.size) { BooleanArray(board[0].size) }

        for (y in board.indices) {
            for (x in board[0].indices) {
                if (!visited[y][x]) {
                    visited[y][x] = true
                    val list = findPoint(y, x, board[y][x])
                    ret += list.size * calcPerimeter(list)
                }
            }
        }


        return ret
    }

    fun calcSide(list: List<Pair<Int, Int>>): Long {
        var sum = 0L

        list.forEach { point ->
            // outside
            /**
             * left top
             * XX
             * XO
             */
            if (!list.contains(point.plus(Pair(0, -1))) && !list.contains(point.plus(Pair(-1, 0)))) {
                sum++
            }

            /**
             * right top
             * XX
             * OX
             */
            if (!list.contains(point.plus(Pair(0, 1))) && !list.contains(point.plus(Pair(-1, 0)))) {
                sum++
            }

            /**
             * left bottom
             * XO
             * XX
             */
            if (!list.contains(point.plus(Pair(0, -1))) && !list.contains(point.plus(Pair(1, 0)))) {
                sum++
            }

            /**
             * right bottom
             * OX
             * XX
             */
            if (!list.contains(point.plus(Pair(0, 1))) && !list.contains(point.plus(Pair(1, 0)))) {
                sum++
            }

            // inside
            /**
             * left top
             * OO
             * OX
             */
            if (list.contains(point.plus(Pair(0, 1))) && list.contains(point.plus(Pair(1, 0))) &&
                !list.contains(point.plus(Pair(1, 1)))) {
                sum++
            }

            /**
             * right top
             * OO
             * XO
             */
            if (list.contains(point.plus(Pair(0, -1))) && list.contains(point.plus(Pair(1, 0))) &&
                !list.contains(point.plus(Pair(1, -1)))) {
                sum++
            }

            /**
             * left bottom
             * OX
             * OO
             */
            if (list.contains(point.plus(Pair(0, 1))) && list.contains(point.plus(Pair(-1, 0))) &&
                !list.contains(point.plus(Pair(-1, 1)))) {
                sum++
            }

            /**
             * right bottom
             * XO
             * OO
             */
            if (list.contains(point.plus(Pair(0, -1))) && list.contains(point.plus(Pair(-1, 0))) &&
                !list.contains(point.plus(Pair(-1, -1)))) {
                sum++
            }
        }

        return sum
    }

    fun part2(lines: List<String>): Long {
        var ret = 0L

        board = lines.map { it.toCharArray() }
        visited = List(board.size) { BooleanArray(board[0].size) }

        for (y in board.indices) {
            for (x in board[0].indices) {
                if (!visited[y][x]) {
                    visited[y][x] = true
                    val list = findPoint(y, x, board[y][x])
                    ret += list.size * calcSide(list)
                }
            }
        }

        return ret
    }

    val testInput = readInput("Day12_test")
    val input = readInput("Day12")


    check(part1(testInput) == 1930L) {
        "expected: 1930, result: ${part1(testInput)}"
    }
    // 1381056
    println(part1(input))

    check(part2(testInput) == 1206L) {
        "expected: 1206, result: ${part2(testInput)}"
    }
    // 834828
    println(part2(input))
}
