package day17

import kotlin.test.Test
import kotlin.test.assertEquals

class ClumsyCrucibleKtTest {

    @Test
    fun solvesDay171() {
        val fileName = "/day17.txt"
        val expected = 102
        val actual = solveDay171(fileName)
        assertEquals(expected, actual)
    }
}
