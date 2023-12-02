package day1

import util.readFileLines

private const val inputFile = "/day1.txt"

fun solveDay11(inputFile: String): Int {
    return readFileLines(inputFile).sumOf { it.getCalibrationValue() }
}

fun solveDay12(inputFile: String): Int {
    return readFileLines(inputFile).sumOf { it.getCalibrationValueWithSpelledDigits() }
}

private fun String.getCalibrationValue(): Int {
    val digits = filter { it.isDigit() }

    return "${digits.first()}${digits.last()}".toInt()
}

private fun String.getCalibrationValueWithSpelledDigits(): Int {
    val spelledDigits = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    val reversedSpelledDigits = spelledDigits.map { it.reversed() }

    var runningValues: List<String> = emptyList()
    var runningReversed: List<String> = emptyList()

    var firstValue: Int? = null
    var secondValue: Int? = null

    for (startIndex in indices) {
        val endIndex = lastIndex - startIndex
        val startChar = get(startIndex)
        val endChar = get(endIndex)

        runningValues = runningValues.map {
            it + startChar
        } + startChar.toString()

        runningReversed = runningReversed.map {
            it + endChar
        } + endChar.toString()

        if (firstValue == null) {
            if (startChar.isDigit()) {
                firstValue = "$startChar".toInt()
            } else {
                val index = spelledDigits.indexOfFirst { runningValues.contains(it) }
                if (index != -1) {
                    firstValue = index + 1
                }
            }
        }

        if (secondValue == null) {
            if (endChar.isDigit()) {
                secondValue = "$endChar".toInt()
            } else {
                val index = reversedSpelledDigits.indexOfFirst { runningReversed.contains(it) }
                if (index != -1) {
                    secondValue = index + 1
                }
            }
        }

        if (firstValue != null && secondValue != null) {
            break
        }
    }

    return "$firstValue$secondValue".toInt()
}


fun main() {
    println(solveDay11(inputFile))
    println(solveDay12(inputFile))
}