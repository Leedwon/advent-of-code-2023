package day14

import util.readFileLines

private const val fileName = "/day14.txt"

// column to row
typealias Coordinate = Pair<Int, Int>

data class Data(
    val rows: Int,
    val columns: Int,
    val map: Map<Coordinate, Char>
)

fun solveDay141(fileName: String): Int {
    val data = parseInput(fileName)

    return (0 until data.columns).sumOf { calculate(data.map.getColumn(it)) }
}

fun solveDay142(fileName: String): Int {
    val data = parseInput(fileName)

    return calculateDay2(data)
}

// TODO clean it up
private fun calculateDay2(data: Data): Int {

    val boundaries = getBoundaries(data)
    var map = data.map

    fun moveNorth(map: Map<Coordinate, Char>): Map<Coordinate, Char> {
        val copy = mutableMapOf<Coordinate, Char>()
        for (entry in map.entries) {
            copy[entry.key] = entry.value
        }
        for (c in 0 until data.columns) {
            val boundaries = boundaries.columns[c]!!
            val rocks = map.getColumn(c).mapIndexed { index, c -> if (c == 'O') index else null }.filterNotNull()
            for (boundary in boundaries) {
                var rocksCount = rocks.filter { it in boundary }.size
                if (rocksCount == 0) continue // there are no rocks to be moved
                for (row in boundary.first + 1 until boundary.last) {
                    copy[c to row] = if (rocksCount-- > 0) {
                        'O'
                    } else {
                        '.'
                    }
                }
            }
        }
        return copy
    }

    fun moveSouth(map: Map<Coordinate, Char>): Map<Coordinate, Char> {
        val copy = mutableMapOf<Coordinate, Char>()
        for (entry in map.entries) {
            copy[entry.key] = entry.value
        }
        for (c in 0 until data.columns) {
            val boundaries = boundaries.columns[c]!!
            val rocks = map.getColumn(c).mapIndexed { index, c -> if (c == 'O') index else null }.filterNotNull()
            for (boundary in boundaries) {
                var rocksCount = rocks.filter { it in boundary }.size
                if (rocksCount == 0) continue // there are no rocks to be moved
                for (row in boundary.last - 1 downTo boundary.first + 1) {
                    copy[c to row] = if (rocksCount-- > 0) {
                        'O'
                    } else {
                        '.'
                    }
                }
            }
        }
        return copy
    }

    fun moveWest(map: Map<Coordinate, Char>): Map<Coordinate, Char> {
        val copy = mutableMapOf<Coordinate, Char>()
        for (entry in map.entries) {
            copy[entry.key] = entry.value
        }
        for (r in 0 until data.rows) {
            val boundaries = boundaries.rows[r]!!
            val rocks = map.getRow(r).mapIndexed { index, c -> if (c == 'O') index else null }.filterNotNull()
            for (boundary in boundaries) {
                var rocksCount = rocks.filter { it in boundary }.size
                if (rocksCount == 0) continue // there are no rocks to be moved
                for (column in boundary.first + 1 until boundary.last) {
                    copy[column to r] = if (rocksCount-- > 0) {
                        'O'
                    } else {
                        '.'
                    }
                }
            }
        }
        return copy
    }

    fun moveEast(map: Map<Coordinate, Char>): Map<Coordinate, Char> {
        val copy = mutableMapOf<Coordinate, Char>()
        for (entry in map.entries) {
            copy[entry.key] = entry.value
        }
        for (r in 0 until data.rows) {
            val boundaries = boundaries.rows[r]!!
            val rocks = map.getRow(r).mapIndexed { index, c -> if (c == 'O') index else null }.filterNotNull()
            for (boundary in boundaries) {
                var rocksCount = rocks.filter { it in boundary }.size
                if (rocksCount == 0) continue // there are no rocks to be moved
                for (column in boundary.last - 1 downTo boundary.first + 1) {
                    copy[column to r] = if (rocksCount-- > 0) {
                        'O'
                    } else {
                        '.'
                    }
                }
            }
        }
        return copy
    }

    fun cycle(map: Map<Coordinate, Char>): Map<Coordinate, Char> {
        val copy = mutableMapOf<Coordinate, Char>()
        for (entry in map.entries) {
            copy[entry.key] = entry.value
        }
        var newMap = moveNorth(copy)
        newMap = moveWest(newMap)
        newMap = moveSouth(newMap)
        newMap = moveEast(newMap)
        return newMap
    }

    val snapshots = mutableListOf(map)

    val repeat = 1000000000

    for (i in 0 until repeat) {
        map = cycle(map)
        if (map in snapshots) {
            break
        } else {
            snapshots.add(map)
        }
    }

    val repeatedAt = snapshots.indexOf(map)
    val repeatingCycle = snapshots.subList(repeatedAt, snapshots.size)
    val itemsBeforeRepeating = snapshots.size - repeatingCycle.size

    val index = (repeat - itemsBeforeRepeating) % (repeatingCycle.size)
    val finalMap = repeatingCycle[index]

    return finalMap.calculatePoints(data.rows)
}

private fun Map<Coordinate, Char>.calculatePoints(rows: Int): Int {
    var sum = 0
    for (row in 0 until rows) {
        sum += getRow(row).count { it == 'O' } * (rows - row)
    }

    return sum
}

private fun Map<Coordinate, Char>.print(rows: Int, columns: Int) {
    for (row in 0 until rows) {
        for (column in 0 until columns) {
            print(this[column to row])
        }
        print("\n")
    }
}

private fun getBoundaries(data: Data): Boundaries {
    return Boundaries(
        columns = getColumnBoundaries(data),
        rows = getRowBoundaries(data)
    )
}

private fun getRowBoundaries(data: Data): Map<Int, List<IntRange>> {
    val result = mutableMapOf<Int, List<IntRange>>()

    val startBoundary = -1
    val endBoundary = data.rows

    for (r in 0 until data.rows) {
        val middleBoundaries = data.map.getRow(r)
            .mapIndexed { index, char -> if (char == '#') index else null }
            .filterNotNull()
        val boundaries = listOf(startBoundary) + middleBoundaries + listOf(endBoundary)
        val ranges = boundaries.zipWithNext { a, b -> a..b }
        result[r] = ranges
    }

    return result
}

private fun getColumnBoundaries(data: Data): Map<Int, List<IntRange>> {
    val result = mutableMapOf<Int, List<IntRange>>()

    val startBoundary = -1
    val endBoundary = data.columns

    for (c in 0 until data.columns) {
        val middleBoundaries = data.map.getColumn(c)
            .mapIndexed { index, char -> if (char == '#') index else null }
            .filterNotNull()
        val boundaries = listOf(startBoundary) + middleBoundaries + listOf(endBoundary)
        val ranges = boundaries.zipWithNext { a, b -> a..b }
        result[c] = ranges
    }

    return result
}

private data class Boundaries(
    val columns: Map<Int, List<IntRange>>,
    val rows: Map<Int, List<IntRange>>
)

private fun calculate(column: String): Int {
    val startBoundary = -1
    val endBoundary = column.length
    val middleBoundaries = column
        .mapIndexed { index, c ->
            if (c == '#') index else null
        }.filterNotNull()
    val boundaries = listOf(startBoundary) + middleBoundaries + listOf(endBoundary)
    val ranges = boundaries.zipWithNext { a, b -> a..b }
    val rocks = column.mapIndexed { index, c ->
        if (c == 'O') index else null
    }.filterNotNull()


    var sum = 0
    for (range in ranges) {
        val howManyFitIn = rocks.filter { it in range }.size
        sum += (0 until howManyFitIn).sumOf { i -> (column.length) - (range.first + 1) - i }
    }
    return sum
}

private fun Map<Coordinate, Char>.getColumn(column: Int): String {
    return filter { (key, _) -> key.first == column }.map { it.value }.joinToString("")
}

private fun Map<Coordinate, Char>.getRow(row: Int): String {
    return filter { (key, _) -> key.second == row }.map { it.value }.joinToString("")
}

private fun parseInput(fileName: String): Data {
    val map = mutableMapOf<Coordinate, Char>()
    val lines = readFileLines(fileName)
    lines.forEachIndexed { row, line ->
        line.forEachIndexed { column, char ->
            map[column to row] = char
        }
    }
    return Data(
        rows = lines.size,
        columns = lines.first().length,
        map = map
    )
}

fun main() {
    println(solveDay141(fileName))
    println(solveDay142(fileName))
}