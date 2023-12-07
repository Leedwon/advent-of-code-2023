package day7

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CamelCardsKtTest {

    private val fileName = "/day7.txt"

    @Test
    fun solvesDay71() {
        val actual = solveDay71(fileName)
        val expected = 6440

        assertEquals(expected, actual)
    }

    @Test
    fun solvesDay72() {
        val actual = solveDay72(fileName)
        val expected = 5905

        assertEquals(expected, actual)
    }

    @Test
    fun calculatesRawHandStrength() {
        val hand = "AAAAA"
        val actual = calculateRawHandStrength(hand)
        val expected = 402233L
        assertEquals(expected, actual)
    }

    @Test
    fun parsesInput() {
        val actual = parseInput(fileName)

        val expected = listOf(
            CamelCardsRound(
                hand = "32T3K",
                bid = 765
            ),
            CamelCardsRound(
                hand = "T55J5",
                bid = 684
            ),
            CamelCardsRound(
                hand = "KK677",
                bid = 28
            ),
            CamelCardsRound(
                hand = "KTJJT",
                bid = 220
            ),
            CamelCardsRound(
                hand = "QQQJA",
                bid = 483
            ),
        )

        assertEquals(expected, actual)
    }
}