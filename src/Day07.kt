fun main() {
    fun parseLine(line: String): Pair<Long, LongArray> {
        return line.split(" ").run {
            Pair(
                get(0).removeSuffix(":").toLong(),
                slice(1..size-1).map { it.toLong() }.toLongArray()
            )
        }
    }

    fun createNextNumber(answer: Long, now: Long, next: Long, op: String, index: Int): Pair<Long, Int>? {
        when (op) {
            "*" -> now * next
            "+" -> now + next
            "||" -> "$now$next".toLong()
            else -> null
        }?.let {
            if (it in 0..answer) {
                return Pair(it, index)
            }
        }
        return null
    }

    fun part1(lines: List<String>): Long {
        var sum = 0L

        lines.forEach {
            val (answer, arr) = parseLine(it)

            val q = ArrayDeque<Pair<Long, Int>>()
            with(arr) {
                q.add(Pair(get(0), 0))

                while (q.isNotEmpty()) {
                    val data = q.removeFirst()

                    if (data.first == answer && data.second + 1 == size) {
                        sum += answer
                        return@forEach
                    }

                    if (data.second + 1 == size) {
                        continue
                    }

                    createNextNumber(answer, data.first, get(data.second + 1), "+", data.second + 1)?.let { pair ->
                        q.add(pair)
                    }
                    createNextNumber(answer, data.first, get(data.second + 1), "*", data.second + 1)?.let { pair ->
                        q.add(pair)
                    }
                }
            }

        }

        return sum
    }

    fun part2(lines: List<String>): Long {
        var sum = 0L

        lines.forEach {
            val (answer, arr) = parseLine(it)

            val q = ArrayDeque<Pair<Long, Int>>()
            with(arr) {
                q.add(Pair(get(0), 0))

                while (q.isNotEmpty()) {
                    val data = q.removeFirst()

                    if (data.first == answer && data.second + 1 == size) {
                        sum += answer
                        return@forEach
                    }

                    if (data.second + 1 == size) {
                        continue
                    }

                    createNextNumber(answer, data.first, get(data.second + 1), "+", data.second + 1)?.let { pair ->
                        q.add(pair)
                    }
                    createNextNumber(answer, data.first, get(data.second + 1), "*", data.second + 1)?.let { pair ->
                        q.add(pair)
                    }
                    createNextNumber(answer, data.first, get(data.second + 1), "||", data.second + 1)?.let { pair ->
                        q.add(pair)
                    }
                }
            }
        }

        return sum
    }

    val testInput = readInput("Day07_test")
    val input = readInput("Day07")


    check(part1(testInput) == 3749L) {
        "expected: 3749, result: ${part1(testInput)}"
    }
    // 7885693428401
    println(part1(input))

    check(part2(testInput) == 11387L) {
        "expected: 11387, result: ${part2(testInput)}"
    }
    // 348360680516005
    println(part2(input))
}
