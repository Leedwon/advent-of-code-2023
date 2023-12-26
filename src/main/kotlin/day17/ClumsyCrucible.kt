package day17

import util.readFileLines
import java.util.*

private const val fileName = "/day17.txt"

// column to row
typealias Coordinate = Pair<Int, Int>

data class Node(
    val coordinate: Coordinate,
    val move: Coordinate,
    val movesInLine: Int
)

data class NodeWithCost(
    val node: Node,
    val cost: Int
)

// we have to reconsider the last 3 moves because it can open other possibilities
fun solveDay171(fileName: String): Int {
    val map = loadMap(fileName)

    return calculateHeatLoss(
        map = map,
        startingNodes = listOf(
            NodeWithCost(node = Node(coordinate = 0 to 0, move = 1 to 0, movesInLine = 0), cost = 0),
            NodeWithCost(node = Node(coordinate = 0 to 0, move = 0 to 1, movesInLine = 0), cost = 0),
        ),
        minInLine = 0,
        maxInLine = 3
    )
}

fun solveDay172(fileName: String): Int {
    val map = loadMap(fileName)

    return calculateHeatLoss(
        map = map,
        startingNodes = listOf(
            NodeWithCost(node = Node(coordinate = 0 to 0, move = 1 to 0, movesInLine = 0), cost = 0),
            NodeWithCost(node = Node(coordinate = 0 to 0, move = 0 to 1, movesInLine = 0), cost = 0),
        ),
        minInLine = 4,
        maxInLine = 10
    )
}

private fun calculateHeatLoss(
    map: Map<Coordinate, Int>,
    startingNodes: List<NodeWithCost>,
    minInLine: Int,
    maxInLine: Int
): Int {
    val costs = mutableMapOf<Node, Int>().withDefault { Int.MAX_VALUE }
    val queue = PriorityQueue<NodeWithCost>(Comparator.comparing { it.cost })

    startingNodes.forEach {
        costs[it.node] = 0
    }

    queue.addAll(startingNodes)

    while (queue.isNotEmpty()) {
        val current = queue.remove()

        current.node.next(minInLine = minInLine, maxInLine = maxInLine)
            .filter { map[it.coordinate] != null }
            .forEach { next ->
                val cost = current.cost + map[next.coordinate]!!
                if (cost < costs.getValue(next)) {
                    costs[next] = cost
                    queue.add(
                        NodeWithCost(
                            node = next,
                            cost = cost
                        )
                    )
                }

            }
    }

    val lastColumn = map.keys.maxOf { it.first }
    val lastRow = map.keys.maxOf { it.second }

    return costs.keys.filter { it.coordinate == lastColumn to lastRow }.mapNotNull { costs[it] }.min()
}

private fun Node.next(
    minInLine: Int,
    maxInLine: Int
): List<Node> {
    return if (movesInLine < minInLine) {
        listOf(copy(coordinate = coordinate + move, movesInLine = movesInLine + 1))
    } else {
        buildList {
            if (movesInLine < maxInLine) {
                add(copy(coordinate = coordinate + move, movesInLine = movesInLine + 1))
            }
            val left = move.second to move.first // (1,0) turns into (0,1)
            val right = -move.second to -move.first // (0, 1) turns int (-1,0)

            add(copy(coordinate = coordinate + left, move = left, movesInLine = 1))
            add(copy(coordinate = coordinate + right, move = right, movesInLine = 1))
        }
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

private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return this.first + other.first to this.second + other.second
}

fun main() {
    println(solveDay171(fileName))
    println(solveDay172(fileName))
}
