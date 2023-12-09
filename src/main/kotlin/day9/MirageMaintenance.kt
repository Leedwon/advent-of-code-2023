package day9

import util.readFileLines

private const val fileName = "/day9.txt"

fun solveDay91(fileName: String): Int {
    return readFileLines(fileName)
        .map { it.split(" ") }
        .map { numbers -> numbers.map { it.toInt() } }
        .sumOf { predictNextValue(it) }
}

fun solveDay92(fileName: String): Int {
    return readFileLines(fileName)
        .map { it.split(" ") }
        .map { numbers -> numbers.map { it.toInt() } }
        .sumOf { predictNextValueBackwards(it) }
}

private fun predictNextValue(numbers: List<Int>): Int {
    val differences = getDifferencesLists(numbers)

    var predictedValue = 0

    differences.reversed().forEach {
        val last = it.last()
        predictedValue += last

    }
    return predictedValue
}

private fun predictNextValueBackwards(numbers: List<Int>): Int {
    val differences = getDifferencesLists(numbers)

    var predictedValue = 0

    differences.reversed().forEach {
        val first = it.first()
        predictedValue = first - predictedValue

    }
    return predictedValue
}

private fun getDifferencesLists(numbers: List<Int>): List<List<Int>> {
    val differences = mutableListOf(numbers)

    var currentDifference = differences[0].calculateDifferences()

    while (currentDifference.any { it != 0 }) {
        differences.add(currentDifference)
        currentDifference = currentDifference.calculateDifferences()
    }

    differences.add(currentDifference)

    return differences
}

private fun List<Int>.calculateDifferences(): List<Int> = zipWithNext().map { it.second - it.first }

fun main() {
    println(solveDay91(fileName))
    println(solveDay92(fileName))
}