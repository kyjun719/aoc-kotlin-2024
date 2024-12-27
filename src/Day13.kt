
fun main() {
    data class Equation(
        // ax + bx = x
        // ay + by = y

        var ax: Int = 0,
        var ay: Int = 0,
        var bx: Int = 0,
        var by: Int = 0,
        var x: Long = 0,
        var y: Long = 0
    ) {
        val l: Long
            get() = (ax*by - bx*ay).toLong()

        val au: Long
            get() = -y*bx + x*by

        val bu: Long
            get() = y*ax - x*ay
    }

    fun calcNum(equation: Equation): Long {
        if (equation.au % equation.l != 0L || equation.bu % equation.l != 0L) {
            return 0
        }

        return (equation.au/equation.l)*3 + (equation.bu/equation.l)
    }

    fun parseFirstValueFromEquation(line: String, numberStart: Char): Pair<Int, Int> {
        return Pair(
            line.substring(line.indexOfFirst { it == numberStart }+1, line.indexOf(',')).toInt(),
            line.substring(line.indexOfLast { it == numberStart }+1, line.length).toInt()
        )
    }

    fun parseLines(lines: List<String>): List<Equation> {
        val list = mutableListOf<Equation>()

        var e = Equation()
        lines.forEach { line ->
            if (line.isEmpty()) {
                e = Equation()
            } else if (line.contains("A")) {
                parseFirstValueFromEquation(line, '+').run {
                    e.ax = first
                    e.ay = second
                }
            } else if (line.contains("B")) {
                parseFirstValueFromEquation(line, '+').run {
                    e.bx = first
                    e.by = second
                }
            } else {
                parseFirstValueFromEquation(line, '=').run {
                    e.x = first.toLong()
                    e.y = second.toLong()
                }
                list.add(e)
            }
        }

        return list
    }

    fun part1(lines: List<String>): Long {
        return parseLines(lines).sumOf {
            calcNum(it)
        }
    }

    fun part2(lines: List<String>): Long {
        return parseLines(lines).sumOf {
            calcNum(it.apply {
                x += 10000000000000
                y += 10000000000000
            })
        }
    }

    val testInput = readInput("Day13_test")
    val input = readInput("Day13")


    check(part1(testInput) == 480L) {
        "expected: 480L, result: ${part1(testInput)}"
    }
    // 39290
    println(part1(input))

    check(part2(testInput) == 875318608908L) {
        "expected: 875318608908, result: ${part2(testInput)}"
    }
    // 73458657399094
    println(part2(input))
}
