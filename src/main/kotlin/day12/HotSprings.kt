package day12

import util.readFileLines

private const val fileName = "/day12.txt"

fun solveDay121(fileName: String): Long {
    val springsData = readFileLines(fileName)
        .map { line ->
            val (springs, data) = line.split(" ")
            springs to data.split(",").map { it.toInt() }
        }

    val cache = mutableMapOf<CacheKey, Long>()
    return springsData.sumOf { (spring, data) ->
        calculate(spring, data, cache)
    }
}

fun solveDay122(fileName: String): Long {
    val springsData = readFileLines(fileName)
        .map { line ->
            val (springs, data) = line.split(" ")
            val extendedSprings = List(5) { springs }.joinToString("?")
            val extendedData = List(5) { data }.joinToString(",")
            extendedSprings to extendedData.split(",").map { it.toInt() }
        }

    val cache = mutableMapOf<CacheKey, Long>()
    return springsData.sumOf { (spring, data) ->
        calculate(spring, data, cache)
    }
}

data class CacheKey(
    val record: String,
    val groups: List<Int>
)

private fun calculate(record: String, groups: List<Int>, cache: MutableMap<CacheKey, Long>): Long {
    if (groups.isEmpty()) {
        return if ("#" !in record) {
            1 // If groups are empty then we expected no more # for valid string
        } else {
            0
        }
    }

    if (record.isEmpty()) {
        return 0 // if record is empty and there are more groups string is not valid
    }

    val nextChar = record[0]
    val nextGroup = groups[0]

    val fromCache = cache[CacheKey(record = record, groups = groups)]
    if (fromCache != null) {
        return fromCache
    }

    fun dot(): Long {
        return calculate(record = record.drop(1), groups = groups, cache = cache)
    }

    // this is always called for initial # of the group
    fun pound(): Long {
        val canFit = record.length >= nextGroup
        if (!canFit) return 0

        val currentGroup = record.take(nextGroup).replace('?', '#')

        // Invalid group i.e "###.#" for 5
        if (currentGroup != "#".repeat(nextGroup)) {
            return 0
        }

        // This is the last part of the string
        if (record.length == nextGroup) {
            // If this is last group we are good
            return if (groups.size == 1) {
                1
            } else {
                0
            }
        }

        val nextChar = record[nextGroup]
        if (nextChar == '#') return 0 // too big group, returning

        // nextChar now must be ?, or . so we can skip it, and it will either start new group or skip another dot
        return calculate(record = record.drop(nextGroup + 1), groups = groups.drop(1), cache = cache)
    }

    return when (nextChar) {
        '.' -> dot()
        '#' -> pound()
        '?' -> dot() + pound()
        else -> error("Invalid char")
    }.also {
        cache[CacheKey(record = record, groups = groups)] = it
    }
}

private fun generateAllStrings(
    possibleStrings: MutableList<String>,
    runningString: StringBuilder,
    unknownIndices: List<Int>,
    index: Int,
) {
    if (index > unknownIndices.lastIndex) {
        possibleStrings.add(runningString.toString())
        return
    }

    runningString[unknownIndices[index]] = '#'
    generateAllStrings(
        possibleStrings = possibleStrings,
        runningString = runningString,
        unknownIndices = unknownIndices,
        index = index + 1
    )

    runningString[unknownIndices[index]] = '.'
    generateAllStrings(
        possibleStrings = possibleStrings,
        runningString = runningString,
        unknownIndices = unknownIndices,
        index = index + 1
    )
}

private fun calculateNumberOfPossibilities(spring: String, springData: List<Int>): Int {
    val unknownIndices = spring.mapIndexed { index, c -> index to c }.filter { it.second == '?' }.map { it.first }
    val combinations = mutableListOf<String>()

    generateAllStrings(
        possibleStrings = combinations,
        runningString = StringBuilder(spring),
        unknownIndices = unknownIndices,
        index = 0
    )

    return combinations.count { it.isValidSpring(springData) }
}

private fun String.isValidSpring(springData: List<Int>): Boolean {
    val groups = mutableListOf<Int>()

    var currentGroup = 0

    for (ch in this) {
        when (ch) {
            '#' -> currentGroup++
            '.' -> if (currentGroup > 0) {
                groups.add(currentGroup)
                currentGroup = 0
            }

            else -> error("Invalid char $ch in spring")
        }
    }

    if (currentGroup > 0) {
        groups.add(currentGroup)
    }

    return groups == springData
}

fun main() {
    println(solveDay121(fileName))
    println(solveDay122(fileName))
}
