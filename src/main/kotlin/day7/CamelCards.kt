package day7

import day7.HandType.*
import util.readFileLines
import kotlin.math.pow

private const val fileName = "/day7.txt"

fun solveDay71(fileName: String): Int {
    return parseInput(fileName)
        .map { camelCardsRound -> calculateHandStrength(camelCardsRound.hand) to camelCardsRound.bid }
        .sortedBy { it.first }
        .mapIndexed { index, pair -> (index + 1) * pair.second }
        .sum()
}

fun solveDay72(fileName: String): Int {
    return parseInput(fileName)
        .map { camelCardsRound ->
            calculateHandStrength(camelCardsRound.hand, withJokers = true) to camelCardsRound.bid
        }
        .sortedBy { it.first }
        .mapIndexed { index, pair -> (index + 1) * pair.second }
        .sum()
}

data class CamelCardsRound(
    val hand: String,
    val bid: Int
)

fun calculateHandStrength(hand: String, withJokers: Boolean = false): Long {
    val handType = if (withJokers) getHandTypeWithJokers(hand) else getHandType(hand)

    val rawHandStrength = calculateRawHandStrength(hand = hand, withJokers = withJokers)

    return when (handType) {
        FiveOfAKind -> 13.0.pow(10).toLong() + rawHandStrength
        FourOfAKind -> 13.0.pow(9).toLong() + rawHandStrength
        FullHouse -> 13.0.pow(8).toLong() + rawHandStrength
        ThreeOfAKind -> 13.0.pow(7).toLong() + rawHandStrength
        TwoPair -> 13.0.pow(6).toLong() + rawHandStrength
        Pair -> 13.0.pow(5).toLong() + rawHandStrength
        Raw -> rawHandStrength
    }
}

private fun getHandType(hand: String): HandType {
    val buckets = mutableMapOf(
        '2' to 0,
        '3' to 0,
        '4' to 0,
        '5' to 0,
        '6' to 0,
        '7' to 0,
        '8' to 0,
        '9' to 0,
        'T' to 0,
        'J' to 0,
        'Q' to 0,
        'K' to 0,
        'A' to 0
    )

    hand.forEach { card ->
        buckets[card] = buckets[card]!! + 1
    }

    return when {
        buckets.values.any { it == 5 } -> FiveOfAKind
        buckets.values.any { it == 4 } -> FourOfAKind
        buckets.values.any { it == 3 } && buckets.values.any { it == 2 } -> FullHouse
        buckets.values.any { it == 3 } -> ThreeOfAKind
        buckets.values.count { it == 2 } == 2 -> TwoPair
        buckets.values.any { it == 2 } -> Pair
        else -> Raw
    }
}

private fun getHandTypeWithJokers(hand: String): HandType {
    val buckets = mutableMapOf(
        '2' to 0,
        '3' to 0,
        '4' to 0,
        '5' to 0,
        '6' to 0,
        '7' to 0,
        '8' to 0,
        '9' to 0,
        'T' to 0,
        'Q' to 0,
        'K' to 0,
        'A' to 0
    )

    hand.filter { it != 'J' }.forEach { card ->
        buckets[card] = buckets[card]!! + 1
    }

    val jokersCount = hand.count { it == 'J' }

    return when {
        buckets.values.any { it + jokersCount == 5 } -> FiveOfAKind
        buckets.values.any { it + jokersCount == 4 } -> FourOfAKind
        buckets.values.any { it == 3 } && buckets.values.any { it == 2 } -> FullHouse // Raw full house
        buckets.values.count { it == 2 } == 2 && jokersCount == 1 -> FullHouse
        buckets.values.any { it + jokersCount == 3 } -> ThreeOfAKind
        buckets.values.count { it == 2 } == 2 -> TwoPair
        buckets.values.any { it == 2 } -> Pair
        buckets.values.none { it == 2 } && jokersCount == 1 -> Pair
        else -> Raw
    }
}

fun calculateRawHandStrength(hand: String, withJokers: Boolean = false): Long {
    return (
            (if (withJokers) hand[4].valueWithJokers else hand[4].value) * 13.0.pow(0) +
                    (if (withJokers) hand[3].valueWithJokers else hand[4].value) * 13.0.pow(1) +
                    (if (withJokers) hand[2].valueWithJokers else hand[4].value) * 13.0.pow(2) +
                    (if (withJokers) hand[1].valueWithJokers else hand[4].value) * 13.0.pow(3) +
                    (if (withJokers) hand[0].valueWithJokers else hand[4].value) * 13.0.pow(4)
            ).toLong()
}

private val Char.valueWithJokers: Int
    get() = when (this) {
        'J' -> 1
        '2' -> 2
        '3' -> 3
        '4' -> 4
        '5' -> 5
        '6' -> 6
        '7' -> 7
        '8' -> 8
        '9' -> 9
        'T' -> 10
        'Q' -> 11
        'K' -> 12
        'A' -> 13
        else -> error("Unsupported card")
    }

private val Char.value: Int
    get() = when (this) {
        '2' -> 1
        '3' -> 2
        '4' -> 3
        '5' -> 4
        '6' -> 5
        '7' -> 6
        '8' -> 7
        '9' -> 8
        'T' -> 9
        'J' -> 10
        'Q' -> 11
        'K' -> 12
        'A' -> 13
        else -> error("Unsupported card")
    }

fun parseInput(fileName: String): List<CamelCardsRound> {
    return readFileLines(fileName).map { parseRound(it) }
}

enum class HandType {
    FiveOfAKind,
    FourOfAKind,
    FullHouse,
    ThreeOfAKind,
    TwoPair,
    Pair,
    Raw
}

private fun parseRound(input: String): CamelCardsRound {
    val (hand, bid) = input.split(" ")
    return CamelCardsRound(
        hand = hand,
        bid = bid.toInt()
    )
}

fun main() {
    println(solveDay71(fileName))
    println(solveDay72(fileName))
}