package day4

import util.readFileLines
import java.util.*
import kotlin.math.pow

private const val fileName = "/day4.txt"

data class Scratchcard(
    val winningNumbers: List<Int>,
    val numbers: List<Int>
)

fun solveDay41(fileName: String): Int {
    val scratchcards = parseInput(fileName)
    return scratchcards.sumOf { it.calculatePoints() }
}

fun solveDay42(fileName: String): Int {
    val scratchcards = parseInput(fileName)

    val matches = scratchcards.map { it.calculateMatchingNumbers() }

    val scoreMap = scratchcards.indices.associateWith { 1 }.toMutableMap()

    matches.forEachIndexed { index, count ->
        for (i in 1..count) {
            val newScore = scoreMap[index +i]!! + scoreMap[index]!!
            scoreMap[index + i] = newScore
        }
    }
    return scoreMap.values.sum()
}

fun solveDay42Naive(fileName: String): Int {
    val scratchcards = parseInput(fileName)

    var numberOfCards = scratchcards.size

    // id to scratchcard
    val stack = PriorityQueue<Pair<Int, Scratchcard>>(Comparator.comparing { it.first })
    scratchcards
        .mapIndexed { index, scratchcard -> index to scratchcard }
        .forEach { stack.add(it) }

    while (stack.isNotEmpty()) {
        val (id, card) = stack.poll()

        val wonCards = card.calculateMatchingNumbers()

        repeat(wonCards) {
            val newId = id + it + 1
            stack.add(newId to scratchcards[newId])
        }
        numberOfCards += wonCards
    }

    return numberOfCards
}

fun parseInput(fileName: String): List<Scratchcard> {
    return readFileLines(fileName).map { parseScratchcard(it) }
}

private fun parseScratchcard(input: String): Scratchcard {
    val (card, allNumbers) = input.split(":", limit = 2)
    val (winningNumbers, numbers) = allNumbers.split("|", limit = 2)

    return Scratchcard(
        winningNumbers = winningNumbers.trim().split(" ").filter { it.isNotBlank() }.map { it.toInt() },
        numbers = numbers.trim().split(" ").filter { it.isNotBlank() }.map { it.toInt() }
    )
}

private fun Scratchcard.calculatePoints(): Int {
    val matches = numbers.count { winningNumbers.contains(it) }
    return 2.0.pow(matches - 1).toInt()
}

private fun Scratchcard.calculateMatchingNumbers(): Int {
    return numbers.count { winningNumbers.contains(it) }
}

fun main() {
    println(solveDay41(fileName))
    println(solveDay42(fileName))
}