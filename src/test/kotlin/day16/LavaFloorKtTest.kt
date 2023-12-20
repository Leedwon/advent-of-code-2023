package day16

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LavaFloorKtTest {

    @Test
    fun solvesDay161() {
        val fileName = "/day16.txt"
        val expected = 46
        val actual = solveDay161(fileName)
        assertEquals(expected, actual)
    }

    @Test
    fun solvesDay162() {
        val fileName = "/day16.txt"
        val expected = 51
        val actual = solveDay162(fileName)
        assertEquals(expected, actual)
    }
}
