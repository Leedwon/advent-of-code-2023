package day13

import util.readFileLines

private val fileName = "/day13.txt"

// column to row
typealias Coordinate = Pair<Int, Int>

data class Data(
    val map: Map<Coordinate, Char>,
    val rows: Int,
    val columns: Int
)

fun solveDay131(fileName: String): Int {
    val data = parseInput(readFileLines(fileName))

    return data.sumOf { calculate(it) }
}

fun solveDay132(fileName: String): Int {
    val data = parseInput(readFileLines(fileName))

    return data.sumOf { calculate2(it) }
}

private fun calculate(data: Data): Int {

    fun vertical(): Int {
        var runningSum = 0
        for (reflectionRow in 1 until data.rows) {
            // row is a potential reflection point, check reflection for it
            var isReflection = true
            for (row in 0 until reflectionRow) {
                val aboveRow = reflectionRow - row - 1
                val belowRow = reflectionRow + row

                if (aboveRow >= data.rows || belowRow >= data.rows) {
                    continue
                }

                if (data.getRow(aboveRow) != data.getRow(belowRow)) {
                    isReflection = false
                }
            }
            if (isReflection) {
                runningSum += reflectionRow
            }
        }
        return runningSum
    }

    fun horizontal(): Int {
        var runningSum = 0
        for (reflectionColumn in 1 until data.columns) {
            // row is a potential reflection point, check reflection for it
            var isReflection = true
            for (column in 0 until reflectionColumn) {
                val leftColumn = reflectionColumn - column - 1
                val rightColumn = reflectionColumn + column

                if (leftColumn >= data.columns || rightColumn >= data.columns) {
                    continue
                }

                if (data.getColumn(leftColumn) != data.getColumn(rightColumn)) {
                    isReflection = false
                }
            }
            if (isReflection) {
                runningSum += reflectionColumn
            }
        }
        return runningSum
    }

    return horizontal() + vertical() * 100
}

private fun calculate2(data: Data): Int {

    fun vertical(): Int {
        for (reflectionRow in 1 until data.rows) {
            // row is a potential reflection point, check reflection for it
            val notMatchingRows = mutableListOf<Pair<String, String>>()
            for (row in 0 until reflectionRow) {
                val aboveRow = reflectionRow - row - 1
                val belowRow = reflectionRow + row

                if (notMatchingRows.size > 1) {
                    break
                }

                if (aboveRow >= data.rows || belowRow >= data.rows) {
                    continue
                }

                val above = data.getRow(aboveRow)
                val below = data.getRow(belowRow)

                if (above != below) {
                    notMatchingRows.add(above to below)
                }
            }

            if (notMatchingRows.size == 1) {
                val (above, below) = notMatchingRows.first()
                if(above.isEmpty() || below.isEmpty()) {
                    println("wtf")
                }
                if (!above.hasMoreThanCharDifferent(moreThan = 1, other = below)) {
                    return reflectionRow
                }
            }
        }
        return 0
    }

    fun horizontal(): Int {
        for (reflectionColumn in 1 until data.columns) {
            // row is a potential reflection point, check reflection for it
            val notMatchingColumns = mutableListOf<Pair<String, String>>()
            for (column in 0 until reflectionColumn) {
                val leftColumn = reflectionColumn - column - 1
                val rightColumn = reflectionColumn + column

                if (leftColumn >= data.columns || rightColumn >= data.columns) {
                    continue
                }

                val left = data.getColumn(leftColumn)
                val right = data.getColumn(rightColumn)

                if (data.getColumn(leftColumn) != data.getColumn(rightColumn)) {
                    notMatchingColumns.add(left to right)
                }
            }
            if (notMatchingColumns.size == 1) {
                val (left, right) = notMatchingColumns.first()
                if (!left.hasMoreThanCharDifferent(moreThan = 1, other = right)) {
                    return reflectionColumn
                }
            }
        }
        return 0
    }

    val vertical = vertical()
    return if (vertical > 0) {
        vertical * 100
    } else {
        horizontal()
    }
}

private fun String.hasMoreThanCharDifferent(moreThan: Int, other: String): Boolean {
    var count = 0
    if(this.length != other.length) {
        println("thats weird for\nthis=$this\nother=$other")
    }
    for (index in indices) {
        if (this[index] != other[index]) {
            count++
        }
        if (count > moreThan) {
            return true
        }
    }
    return false
}

private fun Data.getRow(row: Int): String {
    return map.filter { (key, _) -> key.second == row }.map { it.value }.joinToString("")
}

private fun Data.getColumn(column: Int): String {
    return map.filter { (key, _) -> key.first == column }.map { it.value }.joinToString("")
}

private fun parseInput(lines: List<String>): List<Data> {
    val result = mutableListOf<Data>()
    val current = mutableListOf<String>()
    for (line in lines) {
        if (line.isNotEmpty()) {
            current.add(line)
        } else {
            result.add(parseMap(current))
            current.clear()
        }
    }
    result.add(parseMap(current))
    return result
}

private fun parseMap(lines: List<String>): Data {
    val rows = lines.size
    val columns = lines.first().length

    val result = mutableMapOf<Coordinate, Char>()

    lines.forEachIndexed { row, str ->
        str.forEachIndexed { column, char ->
            result[column to row] = char
        }
    }

    return Data(
        map = result,
        rows = rows,
        columns = columns
    )
}

fun main() {
    println(solveDay131(fileName))
    println(solveDay132(fileName))
}
