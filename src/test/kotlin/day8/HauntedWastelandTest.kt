package day8

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class HauntedWastelandTest {

    @Test
    fun solvesDay81() {
        val actual = solveDay81("/day8.txt")
        val expected = 6

        assertEquals(expected, actual)
    }

    @Test
    fun solvesDay82() {
        val actual = solveDay82("/day8-2.txt")
        val expected = 6L

        assertEquals(expected, actual)
    }
}