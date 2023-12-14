package day12

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class HotSpringsKtTest {

    @Test
    fun solvesDay121() {
        val fileName = "/day12.txt"

        val actual = solveDay121(fileName)
        val expected = 21L

        assertEquals(expected, actual)
    }

    @Test
    fun solvesDay122() {
        val fileName = "/day12.txt"

        val actual = solveDay122(fileName)
        val expected = 525152L

        assertEquals(expected, actual)
    }
}
