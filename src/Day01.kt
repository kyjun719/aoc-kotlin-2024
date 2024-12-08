import kotlin.math.abs

fun main() {
    fun createSeparateList(lines: List<String>): Pair<List<Int>, List<Int>> {
        return lines.map { line ->
            val first = line.substringBefore(" ").toInt()
            val second = line.substringAfterLast(" ").toInt()
            first to second
        }.unzip()
    }

    fun part1(lines: List<String>): Int {
        val (left, right) = createSeparateList(lines)

        return left.sorted().zip(right.sorted()).sumOf {
            abs(it.first - it.second)
        }
    }

    fun part2(lines: List<String>): Int {
        val (left, right) = createSeparateList(lines)

        val frequencyMap: Map<Int, Int> = right.groupingBy { it }.eachCount()

        return left.sumOf { it * frequencyMap.getOrDefault(it, 0) }
    }

    val testInput = readInput("Day01_test")
    val input = readInput("Day01")

    check(part1(testInput) == 11) {
        "expected: 1, result: ${part1(testInput)}"
    }
    // 2000468
    println(part1(input))


    check(part2(testInput) == 31) {
        "expected: 31, result: ${part2(testInput)}"
    }
    // 18567089
    println(part2(input))
}
