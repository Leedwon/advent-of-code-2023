package day8

import util.readFileLines

private const val fileName = "/day8.txt"

data class Node(
    val name: String,
    val left: String,
    val right: String
)

fun solveDay81(fileName: String): Int {
    val lines = readFileLines(fileName).filter { it.isNotBlank() }
    val instruction = lines.first()
    val mapLines = lines.drop(1)

    val map = parseMap(mapLines)

    return solveDay81(instruction = instruction, nodes = map)
}

fun solveDay82(fileName: String): Long {
    val lines = readFileLines(fileName).filter { it.isNotBlank() }
    val instruction = lines.first()
    val mapLines = lines.drop(1)

    val map = parseMap(mapLines)

    return solveDay82(instruction = instruction, nodes = map)
}

private fun solveDay82(instruction: String, nodes: Map<String, Node>): Long {
    val startingNodes = nodes.keys.filter { it.last() == 'A' }
    val pathLengths = startingNodes.map { calculatePathLength(instruction, nodes, it) }
    return findLcm(pathLengths)
}

private fun calculatePathLength(instruction: String, nodes: Map<String, Node>, startingNode: String): Long {
    var current = startingNode
    var steps = 0L
    var runningIndex = 0

    while (current.last() != 'Z') {
        steps++
        val node = nodes[current]!!
        current = when (instruction[runningIndex]) {
            'L' -> node.left
            'R' -> node.right
            else -> error("Invalid instruction")
        }
        if (runningIndex < instruction.lastIndex) {
            runningIndex++
        } else {
            runningIndex = 0
        }
    }

    return steps
}

private fun solveDay81(instruction: String, nodes: Map<String, Node>): Int {
    var current = "AAA"
    var steps = 0
    var runningIndex = 0

    while (current != "ZZZ") {
        steps++
        val node = nodes[current]!!
        current = when (instruction[runningIndex]) {
            'L' -> node.left
            'R' -> node.right
            else -> error("Invalid instruction")
        }
        if (runningIndex < instruction.lastIndex) {
            runningIndex++
        } else {
            runningIndex = 0
        }
    }

    return steps
}

private fun parseMap(lines: List<String>): Map<String, Node> {
    return lines.associate { parseMapEntry(it) }
}

private fun parseMapEntry(input: String): Pair<String, Node> {
    val (key, value) = input.split(" = ").map { it.trim() }

    val (firstValue, secondValue) = value.split(",").map { it.trim() }

    return key to Node(
        name = key,
        left = firstValue.drop(1), // dropping (
        right = secondValue.dropLast(1) // dropping )
    )
}

private fun findLcm(first: Long, second: Long): Long {
    var larger = if (first > second) first else second
    val maxLcm = first * second
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % first == 0L && lcm % second == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

private fun findLcm(numbers: List<Long>): Long {
    var result = numbers[0]
    for (i in 1..numbers.lastIndex) {
        result = findLcm(result, numbers[i])
    }
    return result
}

fun main() {
    println(solveDay81(fileName))
    println(solveDay82(fileName))
}