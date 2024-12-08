import java.util.regex.Pattern

fun main() {
    fun createMatchList(regexStr: List<Regex>, line: String): List<Pair<Int, String>> {
        val list = mutableListOf<Pair<Int, String>>()

        regexStr.forEach {
            val matcher = Pattern.compile(it.pattern).matcher(line)

            while (matcher.find()) {
                matcher.group()?.let { text ->
                    list.add(matcher.start() to text)
                }
            }
        }

        return list.sortedBy { it.first }
    }

    fun Pair<Int, String>.calc(): Long {
        return second.let { equation ->
            equation.substring(4, equation.length - 1).split(",").map { it.toLong() }.run {
                get(0) * get(1)
            }
        }
    }

    fun part1(lines: List<String>): Long {
        return lines.sumOf { line ->
            createMatchList(
                listOf(Regex("mul\\(\\d{1,3},\\d{1,3}\\)")),
                line
            ).sumOf { it.calc() }
        }
    }

    fun part2(lines: List<String>): Long {
        var isMul = true

        return lines.sumOf { line ->
            createMatchList(
                listOf(
                    Regex("mul\\(\\d{1,3},\\d{1,3}\\)"),
                    Regex("don't\\(\\)"),
                    Regex("do\\(\\)")
                ),
                line
            ).sumOf {
                if (it.second == "don't()") {
                    isMul = false
                }
                if (it.second == "do()") {
                    isMul = true
                }

                if (it.second.startsWith("mul") && isMul) {
                    it.calc()
                } else {
                    0
                }
            }
        }
    }

    val testInput = readInput("Day03_test")
    val input = readInput("Day03")


    check(part1(testInput) == 161L) {
        "expected: 161, result: ${part1(testInput)}"
    }
    // 192767529
    println(part1(input))

    check(part2(testInput) == 48L) {
        "expected: 48, result: ${part2(testInput)}"
    }
    // 104083373
    println(part2(input))
}
