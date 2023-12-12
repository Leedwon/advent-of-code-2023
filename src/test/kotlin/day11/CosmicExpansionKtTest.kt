package day11

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CosmicExpansionKtTest {

    @Test
    fun solvesDay111() {
        val fileName = "/day11.txt"

        val actual = solveDay11(fileName = fileName, timesBigger = 1)
        val expected = 374L

        assertEquals(expected, actual)
    }

    @Test
    fun solvesDay112() {
        val fileName = "/day11.txt"

        val actual = solveDay11(fileName = fileName, timesBigger = 10)
        val expected = 1030L

        assertEquals(expected, actual)
    }
}
