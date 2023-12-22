package day17

import util.readFileLines
import java.util.*

private const val fileName = "/day17.txt"

// column to row
typealias Coordinate = Pair<Int, Int>

data class Node(
    val coordinate: Coordinate,
    val cost: Int,
    val moves: List<Coordinate> = listOf()
)

// we have to reconsider the last 3 moves because it can open other possibilities
fun solveDay171(fileName: String): Int {
    val map = loadMap(fileName)

    val queue = PriorityQueue<Node>(Comparator.comparing { it.cost })
    map.keys
        .map { Node(coordinate = it, cost = if (it == 0 to 0) 0 else Int.MAX_VALUE) }
        .let { queue.addAll(it) }

    val visitedNodes = mutableListOf<Node>()

    while (queue.isNotEmpty()) {
        val current = queue.remove()

        val neighbours = listOf(
            1 to 0, // right
            -1 to 0, // left
            0 to 1, // bottom
            0 to -1 // top
        )
            .map { it to (it + current.coordinate) }
            .filter { (_, neighbour) -> visitedNodes.none { it.coordinate == neighbour } && map[neighbour] != null }

        for ((move, neighbour) in neighbours) {
            val cost = map[neighbour]!! // null safe cause of filter
            val isMoveAllowed = current.moves.isMoveAllowed(move)
            val newCost = if (isMoveAllowed) current.cost + cost else Int.MAX_VALUE
            val element =
                queue.find { it.coordinate == neighbour } ?: error("Couldn't find element with $neighbour for $current")
            if (newCost < element.cost) {
                println("updating cost for ${element.coordinate} from ${element.cost} to $newCost")
                // remove and add so queue keeps order, plus we inform which move was taken
                queue.remove(element)
                val moves = current.moves + listOf(move)
                queue.add(Node(coordinate = neighbour, cost = newCost, moves = moves))
            }
        }

        visitedNodes.add(current)
    }

    val lastColumn = map.keys.maxOf { it.first }
    val lastRow = map.keys.maxOf { it.second }

    val lastNode = visitedNodes.first { it.coordinate == (lastColumn to lastRow) }

    map.prettyPrint(lastNode.moves)

    return lastNode.cost
}

private fun List<Coordinate>.isMoveAllowed(move: Coordinate): Boolean {
    // maximum last 3 moves can be made in the same direction
    return size < 3 || takeLast(3).any { it != move }
}

fun solveDay172(fileName: String): Int {
    return 0
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

private fun Map<Coordinate, Int>.prettyPrint(
    moves: List<Coordinate>
) {
    val start = Coordinate(0, 0) + moves.first()
    val path = moves.fold(listOf(start)) { acc, coordinate ->
        acc + listOf(acc.last() + coordinate)
    }

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

private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return this.first + other.first to this.second + other.second
}

fun main() {
    println(solveDay171(fileName))
    println(solveDay172(fileName))
}
