package day4

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ScratchcardsKtTest {

    @Test
    fun solvesDay41() {
        val actual = solveDay41("/day4.txt")
        val expected = 13
        assertEquals(expected, actual)
    }

    @Test
    fun solvesDay42() {
        val actual = solveDay42("/day4.txt")
        val expected = 30
        assertEquals(expected, actual)
    }

    @Test
    fun parsesInput() {
        val actual = parseInput("/day4.txt")
        val expected = listOf(
            Scratchcard(
                winningNumbers = listOf(41, 48, 83, 86, 17),
                numbers = listOf(83, 86, 6, 31, 17, 9, 48, 53)
            ),
            Scratchcard(
                winningNumbers = listOf(13, 32, 20, 16, 61),
                numbers = listOf(61, 30, 68, 82, 17, 32, 24, 19)
            ),
            Scratchcard(
                winningNumbers = listOf(1, 21, 53, 59, 44),
                numbers = listOf(69, 82, 63, 72, 16, 21, 14, 1)
            ),
            Scratchcard(
                winningNumbers = listOf(41, 92, 73, 84, 69),
                numbers = listOf(59, 84, 76, 51, 58, 5, 54, 83)
            ),
            Scratchcard(
                winningNumbers = listOf(87, 83, 26, 28, 32),
                numbers = listOf(88, 30, 70, 12, 93, 22, 82, 36)
            ),
            Scratchcard(
                winningNumbers = listOf(31, 18, 13, 56, 72),
                numbers = listOf(74, 77, 10, 23, 35, 67, 36, 11)
            )
        )
        assertEquals(expected, actual)
    }
}