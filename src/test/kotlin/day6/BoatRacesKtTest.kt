package day6

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BoatRacesKtTest {

    private val fileName = "/day6.txt"

    @Test
    fun solvesDay61() {
        val actual = solveDay61(fileName)
        val expected = 288
        assertEquals(expected, actual)
    }

    @Test
    fun solvesDay62() {
        val actual = solveDay62(fileName)
        val expected = 71503L
        assertEquals(expected, actual)
    }

    @Test
    fun parsesInput2() {
        val actual = parseInput2(fileName)
        val expected = 71530L to 940200L

        assertEquals(expected, actual)
    }

    @Test
    fun parsesInput() {
        val actual = parseInput(fileName)
        val expected = listOf(7 to 9, 15 to 40, 30 to 200)

        assertEquals(expected, actual)
    }
}