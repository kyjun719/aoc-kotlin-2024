
enum class Dir(val y: Int, val x: Int) {
    L(0, -1), R(0, 1),
    U(-1, 0), D(1, 0)
}

data class MoveInfo(
    val y: Int, val x: Int,
    val dir: Dir, val score: Int,
    val pointList: List<Pair<Int, Int>> = listOf()
)

fun main() {
    fun findStartPoint(lines: List<String>): Pair<Int, Int> {
        var sy = 0
        var sx = 0
        lines.forEachIndexed { index, s ->
            if (s.contains('S')) {
                sy = index
                sx = s.indexOf('S')
            }
        }

        return Pair(sy, sx)
    }

    fun findLowestPath(lines: List<String>): List<MoveInfo> {
        val (sy, sx) = findStartPoint(lines)

        var score = Int.MAX_VALUE

        val q = ArrayDeque<MoveInfo>()
        q.add(MoveInfo(sy, sx, Dir.R, 0, listOf(Pair(sy, sx))))

        val minValList = List(lines.size) { IntArray(lines[0].length) { Int.MAX_VALUE } }

        val moveInfoList = mutableListOf<MoveInfo>()

        while (q.isNotEmpty()) {
            val now = q.removeFirst()

            if (now.score > score) continue

            if (minValList[now.y][now.x] < now.score - 1000) {
                continue
            }

            minValList[now.y][now.x] = now.score

            if (lines[now.y][now.x] == 'E') {
                if (score > now.score) {
                    moveInfoList.clear()
                }

                score = minOf(score, now.score)
                if (score == now.score) {
                    moveInfoList.add(now)
                }
                continue
            }

            if (lines[now.y + now.dir.y][now.x + now.dir.x] != '#') {
                q.add(now.copy(y = now.y + now.dir.y, x = now.x + now.dir.x, score = now.score + 1,
                    pointList = now.pointList.toMutableList().apply { add(Pair(now.y + now.dir.y, now.x + now.dir.x)) }))
            }

            if (now.dir == Dir.L || now.dir == Dir.R) {
                listOf(Dir.U, Dir.D)
            } else {
                listOf(Dir.L, Dir.R)
            }.forEach {
                if (lines[now.y + it.y][now.x + it.x] != '#') {
                    q.add(MoveInfo(
                        y = now.y + it.y, x = now.x + it.x, dir = it, score = now.score + 1001,
                        pointList = now.pointList.toMutableList().apply { add(Pair(now.y + it.y, now.x + it.x)) }
                    ))
                }
            }
        }

        return moveInfoList
    }

    fun part1(lines: List<String>): Int {
        return findLowestPath(lines).first().score
    }

    fun part2(lines: List<String>): Int {
        return findLowestPath(lines).fold(mutableSetOf<Pair<Int, Int>>()) { acc, moveInfo ->
            acc.addAll(moveInfo.pointList)
            acc
        }.size
    }

    val testInput = readInput("Day16_test")
    val input = readInput("Day16")

    check(part1(testInput) == 7036) {
        "expected: 7036, result: ${part1(testInput)}"
    }
    // 104516
    println(part1(input))


    check(part2(testInput) == 45) {
        "expected: 45, result: ${part2(testInput)}"
    }
    // 545
    println(part2(input))
}
