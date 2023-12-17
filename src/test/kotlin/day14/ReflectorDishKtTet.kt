package day14

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ReflectorDishKtTet {

    @Test
    fun solvesDay141() {
        val fileName = "/day14.txt"
        val expected = 136
        val actual = solveDay141(fileName)

        assertEquals(expected, actual)
    }

    @Test
    fun solvesDay142() {
        val fileName = "/day14.txt"
        val expected = 64
        val actual = solveDay142(fileName)

        assertEquals(expected, actual)
    }
}