package day11

import util.readFileLines
import kotlin.math.abs

private const val fileName = "/day11.txt"

typealias Coordinate = Pair<Int, Int>

fun solveDay11(fileName: String, timesBigger: Long): Long {
    val map = parseInput(fileName)

    val columns = map.keys.maxOf { it.first }
    val rows = map.keys.maxOf { it.second }

    // index of empty rows
    val emptyRows = mutableListOf<Int>()
    val emptyColumns = mutableListOf<Int>()
    val galaxies = map.filter { it.value == '#' }.keys

    for (row in 0..rows) {
        val elements = map.filter { (key, _) -> key.second == row }
        if (elements.values.all { it == '.' }) {
            emptyRows.add(row)
        }
    }

    for (column in 0..columns) {
        val elements = map.filter { (key, _) -> key.first == column }
        if (elements.values.all { it == '.' }) {
            emptyColumns.add(column)
        }
    }

    var sum = 0L
    galaxies.forEachIndexed { index, galaxy ->
        for (otherGalaxy in galaxies.drop(index + 1)) {
            sum += calculateDistance(
                first = galaxy,
                second = otherGalaxy,
                emptyRows = emptyRows,
                emptyColumns = emptyColumns,
                timesBigger = timesBigger
            )
        }
    }

    return sum
}

private fun calculateDistance(
    first: Coordinate,
    second: Coordinate,
    emptyRows: List<Int>,
    emptyColumns: List<Int>,
    timesBigger: Long = 1
): Long {
    val (firstX, firstY) = first
    val (secondX, secondY) = second

    val smallerX = if (firstX < secondX) firstX else secondX
    val biggerX = if (firstX < secondX) secondX else firstX

    val smallerY = if (firstY < secondY) firstY else secondY
    val biggerY = if (firstY < secondY) secondY else firstY

    val emptyRowsInBetween = emptyRows.filter { row -> row in (smallerY + 1) until biggerY }.size
    val emptyColumnsInBetween = emptyColumns.filter { column -> column in (smallerX + 1) until biggerX }.size

    val originalDistance = abs(secondX - firstX) + abs(secondY - firstY)
    val additionalSpace = emptyRowsInBetween * timesBigger + emptyColumnsInBetween * timesBigger
    return originalDistance + additionalSpace - emptyColumnsInBetween - emptyRowsInBetween
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

fun main() {
    println(solveDay11(fileName = fileName, timesBigger = 1))
    println(solveDay11(fileName = fileName, timesBigger = 1_000_000))
}