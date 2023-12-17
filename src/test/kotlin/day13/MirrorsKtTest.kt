package day13

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MirrorsKtTest {

    @Test
    fun solvesDay131() {
        val fileName = "/day13.txt"

        val expected = 405
        val actual = solveDay131(fileName)

        assertEquals(expected, actual)
    }

    @Test
    fun solvesDay132() {
        val fileName = "/day13.txt"

        val expected = 400
        val actual = solveDay132(fileName)

        assertEquals(expected, actual)
    }
}
