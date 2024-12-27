import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    data class Robot(
        var x: Int,
        var y: Int,
        val vx: Int,
        val vy: Int
    ) {
        fun move(width: Int, height: Int) {
            x = ((x + vx)%width + width) % width
            y = ((y + vy)%height + height) % height
        }
    }

    fun createRobotList(lines: List<String>): List<Robot> {
        return lines.map { line ->
            val p = line.split(" ")[0].let { pStr ->
                pStr.substring(pStr.indexOf('=') + 1).split(",").run {
                    Pair(get(0).toInt(), get(1).toInt())
                }
            }

            val v = line.split(" ")[1].let { pStr ->
                pStr.substring(pStr.indexOf('=') + 1).split(",").run {
                    Pair(get(0).toInt(), get(1).toInt())
                }
            }

            Robot(p.first, p.second, v.first, v.second)
        }
    }

    fun part1(lines: List<String>, width: Int = 101, height: Int = 103): Int {
        val xCenterList = mutableListOf<Int>().apply {
            if (width % 2 == 0) {
                add(width/2 + 1)
            }
            add(width/2)
        }

        val yCenterList = mutableListOf<Int>().apply {
            if (height % 2 == 0) {
                add(height/2 + 1)
            }
            add(height/2)
        }

        val robotList = createRobotList(lines).toMutableList()

        for (i in 0 until 100) {
            robotList.forEach {
                it.move(width, height)
            }
        }

        robotList.removeAll {
            xCenterList.contains(it.x) || yCenterList.contains(it.y)
        }

        return robotList.map { robot ->
            when {
                xCenterList.all { robot.x > it } && yCenterList.all { robot.y < it } -> 1
                xCenterList.all { robot.x < it } && yCenterList.all { robot.y < it } -> 2
                xCenterList.all { robot.x < it } && yCenterList.all { robot.y > it } -> 3
                else -> 4
            }
        }.groupBy { it }.map { it.value.size }.reduce { acc, i -> acc*i }
    }

    fun printBoard(robotList: List<Robot>) {
        val board = List(103) { IntArray(101) }

        robotList.forEach {
            board[it.y][it.x]++
        }
        board.joinToString(separator = "\n") {
            it.joinToString(separator = "").replace('0', '.')
        }.println()

        println("==========================================")
    }

    fun part2(lines: List<String>, width: Int = 101, height: Int = 103): Int {
        val robotList = createRobotList(lines)

        var deviation = Double.MAX_VALUE
        for (i in 1 until width*height) {
            robotList.forEach {
                it.move(width, height)
            }

            val xc = robotList.sumOf { it.x }.toDouble() / robotList.size
            val yc = robotList.sumOf { it.y }.toDouble() / robotList.size

            val tmpDev = sqrt(robotList.sumOf { (it.x - xc + it.y - yc).pow(2) } / robotList.size)
            if (deviation > tmpDev) {
                deviation = tmpDev
                println("count:: $i")
                printBoard(robotList)
            }
        }

        return 0
    }

    val testInput = readInput("Day14_test")
    val input = readInput("Day14")

    check(part1(testInput, width = 11, height = 7) == 12) {
        "expected: 12, result: ${part1(testInput, width = 11, height = 7)}"
    }
    // 225810288
    println(part1(input))

    // 6752
    println(part2(input))
}
