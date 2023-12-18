package day15

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LensLibraryKtTest {

    @Test
    fun solvesDay151() {
        val fileName = "/day15.txt"
        val expected = 1320
        val actual = solveDay151(fileName)
        assertEquals(expected, actual)
    }

    @Test
    fun solvesDay152() {
        val fileName = "/day15.txt"
        val expected = 145
        val actual = solveDay152(fileName)
        assertEquals(expected, actual)
    }
}