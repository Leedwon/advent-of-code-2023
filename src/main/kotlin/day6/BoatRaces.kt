package day6

import util.readFileLines

private const val fileName = "/day6.txt"

fun solveDay61(fileName: String): Int {
    return parseInput(fileName)
        .map { calculateNumberOfWaysToBeatDistance(it) }
        .reduce { acc, i -> acc * i }
}

fun solveDay62(fileName: String): Long {
    val (time, distance) = parseInput2(fileName)

    val halfTime = (time / 2)

    var holdTime = halfTime
    fun calculateDistance() = holdTime * (time - holdTime)

    while (calculateDistance() > distance) {
        holdTime /= 2
    }

    // now that we have a holdTime that for sure results in shorter distance we aim to find first holdTime that wins
    while (calculateDistance() < distance) {
        holdTime++
    }

    return time - 2 * holdTime + if (time % 2 == 0L) 1 else 0
}


private fun calculateNumberOfWaysToBeatDistance(timeToDistance: Pair<Int, Int>): Int {
    val (time, distance) = timeToDistance

    val halfTime = time / 2

    var beatCount = 0
    repeat(halfTime) {
        val holdTime =
            it + 1 // no need to check 0 cases as they always loose i.e hold for 0s run for all time = 0 distance
        val raceTime = time - holdTime
        if (holdTime * raceTime > distance) {
            beatCount++
        }
    }

    // minus 1 for even time to extract cases with holdTime = raceTime being calculated twice
    return beatCount * 2 - if (time % 2 == 0) 1 else 0
}

fun parseInput2(fileName: String): Pair<Long, Long> {
    val (times, distances) = readFileLines(fileName)
    val timesValues = times.removePrefix("Time:").trim()
    val distancesValues = distances.removePrefix("Distance:").trim()

    val time = timesValues.trim().replace(" ", "").toLong()
    val distance = distancesValues.trim().replace(" ", "").toLong()
    return time to distance
}

// Returns pairs of time to distance to beat
fun parseInput(fileName: String): List<Pair<Int, Int>> {
    val (times, distances) = readFileLines(fileName)
    val timesValues = times.removePrefix("Time:").trim()
    val distancesValues = distances.removePrefix("Distance:").trim()

    val parsedTimes = timesValues
        .split(" ")
        .filter { it.isNotBlank() }
        .map { it.toInt() }

    val parsedDistances = distancesValues
        .split(" ")
        .filter { it.isNotBlank() }
        .map { it.toInt() }

    return parsedTimes.zip(parsedDistances)
}

fun main() {
    println(solveDay61(fileName))
    println(solveDay62(fileName))
}