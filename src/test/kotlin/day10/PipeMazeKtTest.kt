package day10

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PipeMazeKtTest {

    @Test
    fun solvesDay101() {
        val fileName = "/day10.txt"

        val actual = solveDay101(fileName)
        val expected = 8

        assertEquals(expected, actual)
    }

    @Test
    fun solvesDay102() {
        val fileName = "/day10-2.txt"

        val actual = solveDay102(fileName)
        val expected = 10

        assertEquals(expected, actual)
    }

    @Test
    fun solvesDay102Simple() {
        val fileName = "/day10-3.txt"

        val actual = solveDay102(fileName)
        val expected = 4

        assertEquals(expected, actual)
    }
}
