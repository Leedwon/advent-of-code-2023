package day10

import day10.Direction.*
import util.readFileLines
import kotlin.math.ceil

private const val fileName = "/day10.txt"

typealias Coordinate = Pair<Int, Int>

fun solveDay101(fileName: String): Int {
    val maze = parseInput(fileName)

    val startCoordinate = maze.keys.first { maze[it] == 'S' }

    val loop = calculateLoop(startCoordinate, maze)
    return ceil(loop.size / 2.0f).toInt()
}

fun solveDay102(fileName: String): Int {
    val maze = parseInput(fileName)

    val startCoordinate = maze.keys.first { maze[it] == 'S' }

    val width = maze.keys.maxOf { it.first }
    val height = maze.keys.maxOf { it.second }

    val loop = calculateLoop(startCoordinate, maze)

    var insideCount = 0

    for (coordinate in maze.keys) {
        if (loop.contains(coordinate))
            continue

        var interesections = 0
        var (x, y) = coordinate

        while (x <= width && y <= height) {
            val cords = x to y
            val element2 = maze[cords]

            if (cords in loop && element2 != 'L' && element2 != '7') {
                interesections++
            }

            x++
            y++
        }

        if (interesections % 2 != 0) {
            insideCount++
        }
    }

    return insideCount
}

private fun calculateLoop(start: Coordinate, maze: Map<Coordinate, Char>): List<Coordinate> {
    val loops = ArrayDeque<List<Coordinate>>(listOf(listOf(start)))

    val lookups = listOf(
        0 to 1, // down
        1 to 0, // left
        0 to -1, // up
        -1 to 0 // left
    )

    while (loops.isNotEmpty()) {
        val currentLoop = loops.removeFirst()
        val current = currentLoop.last()

        for (lookup in lookups) {
            val possibleNext = current + lookup

            if (currentLoop.size > 1 && possibleNext == currentLoop[currentLoop.lastIndex - 1]) {
                continue
            }

            if (maze[possibleNext] == 'S' && possibleNext.isValid(current, maze)) {
                return currentLoop
            }

            if (possibleNext in currentLoop) {
                continue
            }

            if (possibleNext.isValid(current, maze)) {
                loops.add(currentLoop + possibleNext)
            }
        }
    }

    error("should early return")
}

// Recursive backtracking, prettier IMO, but causes stackoverflow for real data :(
private fun fillLoop(currentLoop: MutableList<Coordinate>, maze: Map<Coordinate, Char>): Boolean {
    val lookups = listOf(
        0 to 1, // down
        1 to 0, // left
        0 to -1, // up
        -1 to 0 // left
    )

    val current = currentLoop.last()

    // what if the lookup comes back to the S at the start?
    for (lookup in lookups) {
        val possibleNext = current + lookup

        if (maze[possibleNext] == 'S') {
            return true // end of recursive call
        }

        if (possibleNext in currentLoop) {
            continue
        }

        currentLoop.add(possibleNext)

        if (possibleNext.isValid(current = current, maze = maze)) {
            return fillLoop(currentLoop = currentLoop, maze = maze)
        }

        currentLoop.remove(possibleNext)
    }
    return false
}

private fun Coordinate.isValid(current: Coordinate, maze: Map<Coordinate, Char>): Boolean {
    val nextElement = maze[this] ?: return false
    val currentElement = maze[current]!!

    val (currentColumn, currentRow) = current
    val (nextColumn, nextRow) = this

    val goingRight = currentColumn < nextColumn
    val goingLeft = currentColumn > nextColumn
    val goingUp = currentRow > nextRow
    val goingDown = currentRow < nextRow

    val currentDirections = currentElement.directions
    val nextDirections = nextElement.directions

    return when {
        goingDown -> currentDirections.contains(South) && nextDirections.contains(North)
        goingUp -> currentDirections.contains(North) && nextDirections.contains(South)
        goingLeft -> currentDirections.contains(West) && nextDirections.contains(East)
        goingRight -> currentDirections.contains(East) && nextDirections.contains(West)
        else -> error("Going into invalid directions")
    }
}

private fun parseInput(fileName: String): Map<Coordinate, Char> {
    val lines = readFileLines(fileName)

    val map = mutableMapOf<Coordinate, Char>()

    lines.forEachIndexed { row, line ->
        line.forEachIndexed { column, char ->
            map[column to row] = char
        }
    }
    return map
}

private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return this.first + other.first to this.second + other.second
}

private val Char.directions: List<Direction>
    get() {
        return when (this) {
            '|' -> listOf(North, South)
            '-' -> listOf(West, East)
            'L' -> listOf(North, East)
            'J' -> listOf(North, West)
            '7' -> listOf(South, West)
            'F' -> listOf(South, East)
            'S' -> listOf(North, South, West, East)
            '.' -> emptyList()
            else -> error("Invalid directions $this")
        }
    }

enum class Direction {
    South,
    North,
    West,
    East
}

fun main() {
    println(solveDay101(fileName))
    println(solveDay102(fileName))
}
