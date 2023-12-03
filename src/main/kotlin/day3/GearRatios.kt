package day3

import util.readFileLines
import kotlin.math.abs

const val fileName = "/day3.txt"

typealias Coordinate = Pair<Int, Int>

data class Number(
    val coordinates: List<Coordinate>,
    val value: Int
)

data class Data(
    val numbers: List<Number>,
    val symbols: HashMap<Coordinate, Char>
)

fun solveDay31(fileName: String): Int {
    val data = parseInput(fileName)
    return data.numbers
        .filter { it.hasAdjacentSymbol(data.symbols) }
        .sumOf { it.value }
}

fun solveDay32(fileName: String): Int {
    val (numbers, symbols) = parseInput(fileName)

    val gearSymbols = symbols.filter { it.value == '*' }

    var sum = 0
    gearSymbols.forEach { (coordinate, _) ->
        val adjacentNumbers = numbers.filter { number -> number.coordinates.any { it.isAdjacentTo(coordinate) } }
        if (adjacentNumbers.size == 2) {
            sum += adjacentNumbers[0].value * adjacentNumbers[1].value
        }
    }
    return sum
}

fun parseInput(fileName: String): Data {
    val symbols = hashMapOf<Coordinate, Char>()
    val numbers = mutableListOf<Number>()

    readFileLines(fileName).forEachIndexed { y, line ->
        var runningNumber: Int? = null
        var runningCoordinates = emptyList<Coordinate>()
        line.forEachIndexed { x, char ->
            val number = runningNumber
            when {
                char.isDigit() && number == null -> {
                    runningNumber = char.digitToInt()
                    runningCoordinates = runningCoordinates + (x to y)
                }

                char.isDigit() && number != null -> {
                    runningNumber = number * 10 + char.digitToInt()
                    runningCoordinates = runningCoordinates + (x to y)
                }

                !char.isDigit() && number != null -> {
                    numbers.add(
                        Number(
                            value = number,
                            coordinates = runningCoordinates
                        )
                    )
                    runningNumber = null
                    runningCoordinates = emptyList()
                }
            }
            if (char.isSymbol()) {
                symbols[x to y] = char
            }
        }
        if (runningNumber != null) {
            numbers.add(
                Number(
                    value = runningNumber!!,
                    coordinates = runningCoordinates
                )
            )
        }
    }

    return Data(
        numbers = numbers,
        symbols = symbols
    )
}

fun Char.isSymbol() = !isDigit() && this != '.'

private fun Number.hasAdjacentSymbol(symbols: HashMap<Coordinate, Char>): Boolean {
    return coordinates.any { it.hasAdjacentSymbol(symbols) }
}

private fun Coordinate.isAdjacentTo(other: Coordinate): Boolean {
    val (x, y) = this
    val (otherX, otherY) = other

    return abs(x - otherX) <= 1 && abs(y - otherY) <= 1
}

private fun Coordinate.hasAdjacentSymbol(symbols: HashMap<Coordinate, Char>): Boolean {
    val (x, y) = this

    val shifts = listOf(
        0 to 1, // up
        1 to 1, // up-right,
        1 to 0, // right
        1 to -1, // down-right
        0 to -1, // down
        -1 to -1, // down-left
        -1 to 0, // left
        -1 to 1, // up-left
    )
    return shifts.any { shift ->
        symbols[x + shift.first to y + shift.second] != null
    }
}

fun main() {
    println(solveDay31(fileName))
    println(solveDay32(fileName))
}
