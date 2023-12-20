package day16

import util.readFileLines

private const val fileName = "/day16.txt"

fun solveDay161(fileName: String): Int {
    val map = loadMap(fileName)

    val start = Beam(
        position = 0 to 0,
        direction = Direction.Right
    )

    return calculateEnergizedTiles(map, start)
}

// Solved naively for now, improve with caching
fun solveDay162(fileName: String): Int {
    val map = loadMap(fileName)

    val lastColumn = map.keys.maxOf { it.first }
    val lastRow = map.keys.maxOf { it.second }

    var max = 0

    for (row in 0..lastRow) {
        val startRight = Beam(
            position = 0 to row,
            direction = Direction.Right
        )
        val startLeft = Beam(
            position = lastColumn to row,
            direction = Direction.Left
        )
        val candidateRight = calculateEnergizedTiles(
            map = map,
            start = startRight
        )
        val candidateLeft = calculateEnergizedTiles(
            map = map,
            start = startLeft
        )
        max = maxOf(max, candidateLeft, candidateRight)
    }

    for (column in 0..lastColumn) {
        val startDown = Beam(
            position = column to 0,
            direction = Direction.Down
        )
        val startUp = Beam(
            position = lastColumn to lastRow,
            direction = Direction.Up
        )
        val candidateDown = calculateEnergizedTiles(
            map = map,
            start = startDown
        )
        val candidateLeft = calculateEnergizedTiles(
            map = map,
            start = startUp
        )
        max = maxOf(max, candidateLeft, candidateDown)
    }

    return max
}

// solve this problem recursively with new start when split happens
// cache should be for single element
private fun calculateEnergizedTiles(
    map: Map<Coordinate, Char>,
    start: Beam
): Int {
    val beams = mutableListOf(start)

    val seenBeams = mutableListOf<Beam>()

    while (beams.any { it !in seenBeams }) {
        val beamsToBeRemoved = mutableListOf<Beam>()
        val beamsToBeAdded = mutableListOf<Beam>()

        for (beam in beams) {
            val ch = map[beam.position]
            if (ch == null || beam in seenBeams) {
                beamsToBeRemoved.add(beam)
            } else {
                seenBeams.add(beam.copy())
            }

            when (ch) {
                '.' -> beam.position += beam.direction.position // continue movement
                '-' -> {
                    if (beam.direction.isHorizontal) {
                        beam.position += beam.direction.position // continue movement
                    } else {
                        // split but don't move, they will encounter the same char, but now moving horizontally, so they will be moved
                        beam.direction = Direction.Right
                        beamsToBeAdded.add(Beam(position = beam.position, direction = Direction.Left))
                    }
                }

                '|' -> {
                    if (beam.direction.isVertical) {
                        beam.position += beam.direction.position // continue movement
                    } else {
                        // split but don't move, they will encounter the same char, but now moving vertically, so they will be moved
                        beam.direction = Direction.Up
                        beamsToBeAdded.add(Beam(position = beam.position, direction = Direction.Down))
                    }
                }

                '/' -> {
                    val newDirection = when (beam.direction) {
                        Direction.Left -> Direction.Down
                        Direction.Right -> Direction.Up
                        Direction.Up -> Direction.Right
                        Direction.Down -> Direction.Left
                    }
                    beam.direction = newDirection
                    beam.position += newDirection.position
                }

                '\\' -> {
                    val newDirection = when (beam.direction) {
                        Direction.Left -> Direction.Up
                        Direction.Right -> Direction.Down
                        Direction.Up -> Direction.Left
                        Direction.Down -> Direction.Right
                    }
                    beam.position += newDirection.position
                    beam.direction = newDirection
                }
            }
        }
        beamsToBeRemoved.forEach { beams.remove(it) }
        beamsToBeAdded.forEach { beams.add(it) }
    }

    return seenBeams.map { it.position }.distinct().size
}

data class EnergizedCachedCount(
    val configurations: List<List<Beam>>,
    val energizedCount: Int
)

// column to row
typealias Coordinate = Pair<Int, Int>

data class Beam(
    var position: Coordinate,
    var direction: Direction,
)

enum class Direction {
    Left,
    Right,
    Up,
    Down
}

val Direction.position: Coordinate
    get() = when (this) {
        Direction.Left -> -1 to 0
        Direction.Right -> 1 to 0
        Direction.Up -> 0 to -1
        Direction.Down -> 0 to 1
    }

val Direction.isHorizontal: Boolean
    get() = when (this) {
        Direction.Left,
        Direction.Right -> true

        Direction.Up,
        Direction.Down -> false
    }

val Direction.isVertical: Boolean
    get() = when (this) {
        Direction.Left,
        Direction.Right -> false

        Direction.Up,
        Direction.Down -> true
    }

private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return this.first + other.first to this.second + other.second
}

private fun Map<Coordinate, Char>.prettyPrint(
    beams: List<Beam> = emptyList(),
    seen: List<Coordinate> = emptyList(),
    printSeen: Boolean = true
) {
    val columns = keys.maxOf { it.first }
    val rows = keys.maxOf { it.second }

    for (row in 0..rows) {
        for (column in 0..columns) {
            val coordinate = column to row
            val beam = beams.find { it.position == coordinate }
            if (printSeen && coordinate in seen) {
                print("#")
            } else if (beam != null) {
                print(
                    when (beam.direction) {
                        Direction.Left -> '<'
                        Direction.Right -> '>'
                        Direction.Up -> '^'
                        Direction.Down -> 'v'
                    }
                )
            } else {
                print(this[column to row])
            }
        }
        print("\n")
    }
}

private fun loadMap(fileName: String): Map<Coordinate, Char> {
    val map = mutableMapOf<Coordinate, Char>()
    readFileLines(fileName).forEachIndexed { row, line ->
        line.mapIndexed { column, char ->
            map[column to row] = char
        }
    }
    return map
}

fun main() {
    println(solveDay161(fileName))
    println(solveDay162(fileName))
}
