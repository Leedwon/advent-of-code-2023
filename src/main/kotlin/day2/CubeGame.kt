package day2

import util.readFileLines

private const val inputFile = "/day2.txt"

private val redLimit = 12
private val greenLimit = 13
private val blueLimit = 14

data class Game(
    val id: Int,
    val rounds: List<Round>
)

data class Round(
    val red: Int = 0,
    val green: Int = 0,
    val blue: Int = 0
)

fun solveDay11(inputFile: String): Int {
    return parseCubeGames(inputFile)
        .filter { game -> game.isValid() }
        .sumOf { game -> game.id }
}

fun solveDay12(inputFile: String): Int {
    return parseCubeGames(inputFile).sumOf { it.calculateMinimumCubesPower() }
}

fun parseCubeGames(inputFile: String): List<Game> {
    return readFileLines(inputFile).map { parseCubeGame(it) }
}

private fun parseCubeGame(input: String): Game {
    val (game, roundsInput) = input.split(":", limit = 2)
    return Game(
        id = parseId(game),
        rounds = parseRounds(roundsInput)
    )
}

private fun parseId(input: String): Int {
    val (_, id) = input.split(" ", limit = 2)
    return id.toInt()
}

private fun parseRounds(input: String): List<Round> {
    val rounds = input.trimStart().split(";")
    return rounds.map { parseRound(it) }
}

private fun parseRound(input: String): Round {
    val cubes = input.split(",")
    val cubesMap = cubes.associate {
        val (value, type) = it.trimStart().split(" ", limit = 2)
        type to value.toInt()
    }

    return Round(
        red = cubesMap.getOrDefault("red", 0),
        green = cubesMap.getOrDefault("green", 0),
        blue = cubesMap.getOrDefault("blue", 0),
    )
}

private fun Game.calculateMinimumCubesPower(): Int {
    var maxRed = 0
    var maxGreen = 0
    var maxBlue = 0

    for (round in rounds) {
        if (round.red > maxRed) maxRed = round.red
        if (round.green > maxGreen) maxGreen = round.green
        if (round.blue > maxBlue) maxBlue = round.blue
    }

    return maxRed * maxGreen * maxBlue
}

private fun Game.isValid(): Boolean {
    return rounds.all { round ->
        round.red <= redLimit && round.green <= greenLimit && round.blue <= blueLimit
    }
}

fun main() {
    println(solveDay11(inputFile))
    println(solveDay12(inputFile))
}
