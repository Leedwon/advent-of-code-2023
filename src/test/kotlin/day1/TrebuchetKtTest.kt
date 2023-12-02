package day1

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TrebuchetKtTest {

    @Test
    fun solvesSampleDay11() {
        val result = solveDay11("/day1-1.txt")
        val expected = 142

        assertEquals(expected, result)
    }

    @Test
    fun solvesSampleDay12() {
        val result = solveDay12("/day1-2.txt")
        val expected = 369 + 22 + 13 + 81 + 33 + 87 + 77 + 93 + 39 + 99 + 61 + 59 + 86 + 33 + 18 + 63

        assertEquals(expected, result)
    }

    @Test
    fun solvesDay12Actual() {
        val result = solveDay12("/day1-actual.txt")
        val expected = 55093

        assertEquals(expected, result)
    }
}