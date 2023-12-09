package day9

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MirageMaintenanceTest {

    private val fileName = "/day9.txt"

    @Test
    fun solvesDay91() {
        val actual = solveDay91(fileName)
        val expected = 114

        assertEquals(expected, actual)
    }

    @Test
    fun solvesDay92() {
        val actual = solveDay92(fileName)
        val expected = 2
        assertEquals(expected, actual)
    }
}
