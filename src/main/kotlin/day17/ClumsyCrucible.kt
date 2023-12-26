package day17

import util.readFileLines
import java.util.*

private const val fileName = "/day17.txt"

// column to row
typealias Coordinate = Pair<Int, Int>

data class Node(
    val coordinate: Coordinate,
    val cost: Int,
    val path: List<Coordinate> = listOf(),
    val moves: List<Coordinate> = listOf()
)

// we have to reconsider the last 3 moves because it can open other possibilities
fun solveDay171(fileName: String): Int {
    val map = loadMap(fileName)

    val queue = PriorityQueue<Node>(Comparator.comparing { it.cost })

    for (coordinate in map.keys) {
        // all combinations of >>> >>v etc
        for (m in 0..127) {
            var runningCoordinate = coordinate
            val moves = m
                .toMoves()
                .takeWhile {
                    runningCoordinate += it
                    map[runningCoordinate] != null
                }
            if (moves.isNotEmpty() && moves.isEndingValid() && moves.areValid()) {
                queue.add(
                    Node(
                        coordinate = coordinate,
                        cost = if (coordinate == 0 to 0) 0 else MAX_COST,
                        moves = moves
                    )
                )
            }
        }
    }

    val visitedNodes = mutableListOf<Node>()

    var moves = 0
    while (queue.isNotEmpty()) {
//        if(moves++ % 100 == 0) {
//            map.debugPrint(visitedNodes + queue)
//        }
        val current = queue.remove()
        var runningCost = current.cost
        var runningCoordinate = current.coordinate
        var path = current.path

        for (move in current.moves) {
//            if(runningCoordinate.first == 12 && runningCoordinate.second > 11) {
//                map.prettyPrint(path)
//                println("stop $runningCoordinate")
//            }
            runningCoordinate += move
            path = path + listOf(move)
            val isMoveAllowed = path.isEndingValid()
            val cost = map[runningCoordinate]
            if (cost == null || !isMoveAllowed) break
            runningCost += cost
            val elements = queue.filter { it.coordinate == runningCoordinate }
            for (element in elements) {
                if (element.cost > runningCost) {
                    // remove and add so queue keeps order, plus we inform which move was taken
                    queue.remove(element)
                    queue.add(
                        Node(
                            coordinate = element.coordinate,
                            cost = runningCost,
                            path = path,
                            moves = element.moves
                        )
                    )
                }
            }
        }
        visitedNodes.add(current)
    }

    val lastColumn = map.keys.maxOf { it.first }
    val lastRow = map.keys.maxOf { it.second }

    val lastNode = visitedNodes.filter { it.coordinate == (lastColumn to lastRow) }.minBy { it.cost }

    map.prettyPrint(lastNode.path)
    println("----------------")

    return lastNode.cost
}

private fun List<Coordinate>.isMoveAllowed(move: Coordinate): Boolean {
    // maximum last 3 moves can be made in the same direction
    return size < 2 || takeLast(2).any { it != move }
}

// max 3 consecutive
private fun List<Coordinate>.isEndingValid(): Boolean {
    return size < 4 || takeLast(4).any { it != last() }
}

fun solveDay172(fileName: String): Int {
    return 0
}

private fun Int.toMoves(): List<Coordinate> {
    val first = this.and(0b00000011).toMove()
    val second = this.and(0b00001100).shr(2).toMove()
    val third = this.and(0b00110000).shr(4).toMove()
    val forth = this.and(0b11000000).shr(6).toMove()

    return listOf(first, second, third, forth)
}

private fun List<Coordinate>.areValid() : Boolean {
    return zipWithNext { a, b ->
        when(a) {
            1 to 0 -> b != -1 to 0
            -1 to 0 -> b != 1 to 0
            0 to 1 -> b != 0 to -1
            0 to -1 -> b != 0 to 1
            else -> error("error")
        }
    }
        .all { it }
}

private fun Int.toMove(): Coordinate {
    // 00 -> >, 01 -> <, 10 -> ^, 11 -> v
    return when (this) {
        0b00 -> 1 to 0 // right
        0b01 -> -1 to 0 // left
        0b10 -> 0 to -1 // up
        0b11 -> 0 to 1 // down
        else -> error("Invalid move $this")
    }
}

private fun loadMap(fileName: String): Map<Coordinate, Int> {
    val lines = readFileLines(fileName)
    val map = mutableMapOf<Coordinate, Int>()

    lines.forEachIndexed { row, line ->
        line.forEachIndexed { column, char ->
            map[column to row] = char.digitToInt()
        }
    }

    return map
}

const val MAX_COST =
    10_000 // the costs in data are not that big, and we don't use Int.MAX_VALUE as sometimes max costs will be added

private fun Map<Coordinate, Int>.prettyPrint(
    moves: List<Coordinate>
) {
    val start = Coordinate(0, 0) + moves.first()
    val path = moves.drop(1).fold(listOf(start)) { acc, coordinate ->
        acc + listOf(acc.last() + coordinate)
    }

    println("path = ")
    path.chunked(10).forEach { println(it) }

    val columns = keys.maxOf { it.first }
    val rows = keys.maxOf { it.second }

    for (row in 0..rows) {
        for (column in 0..columns) {
            when (val coordinate = column to row) {
                in path -> {
                    val index = path.indexOf(coordinate)
                    when (moves.getOrNull(index)) {
                        1 to 0 -> print(">")
                        -1 to 0 -> print("<")
                        0 to 1 -> print("v")
                        0 to -1 -> print("^")
                        null -> print(".")
                    }
                }

                (0 to 0) -> {
                    print("s")
                }

                else -> {
                    print("${get(coordinate)}")
                }
            }
        }
        print("\n")
    }
}

private fun Map<Coordinate, Int>.debugPrint(
    nodes: List<Node>
) {
    val columns = keys.maxOf { it.first }
    val rows = keys.maxOf { it.second }

    for (row in 0..rows) {
        for (column in 0..columns) {
            val node = nodes.first { it.coordinate == column to row }
            if (node.cost == MAX_COST) {
                print("x,")
            } else {
                print("${node.cost},")
            }

        }
        print("\n")
    }
    println("--------------------------------------------------")
}

private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return this.first + other.first to this.second + other.second
}

fun main() {
    println(solveDay171(fileName))
    println(solveDay172(fileName))
}
