package day3

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GearRatiosKtTest {

    @Test
    fun solvesDay31() {
        val actual = solveDay31("/day3.txt")
        val expected = 4361
        assertEquals(expected, actual)
    }

    @Test
    fun solvesDay32() {
        val actual = solveDay32("/day3.txt")
        val expected = 467835
        assertEquals(expected, actual)
    }

    @Test
    fun parsesInput() {
        val actual = parseInput("/day3.txt")
        val expected = Data(
            numbers = listOf(
                Number(
                    value = 467,
                    coordinates = listOf(0 to 0, 1 to 0, 2 to 0)
                ),
                Number(
                    value = 114,
                    coordinates = listOf(5 to 0, 6 to 0, 7 to 0)
                ),
                Number(
                    value = 2,
                    coordinates = listOf(0 to 2)
                ),
                Number(
                    value = 35,
                    coordinates = listOf(2 to 2, 3 to 2)
                ),
                Number(
                    value = 633,
                    coordinates = listOf(6 to 2, 7 to 2, 8 to 2)
                ),
                Number(
                    value = 617,
                    coordinates = listOf(0 to 4, 1 to 4, 2 to 4)
                ),
                Number(
                    value = 58,
                    coordinates = listOf(7 to 5, 8 to 5)
                ),
                Number(
                    value = 592,
                    coordinates = listOf(2 to 6, 3 to 6, 4 to 6)
                ),
                Number(
                    value = 755,
                    coordinates = listOf(6 to 7, 7 to 7, 8 to 7)
                ),
                Number(
                    value = 55,
                    coordinates = listOf(8 to 8, 9 to 8)
                ),
                Number(
                    value = 664,
                    coordinates = listOf(1 to 9, 2 to 9, 3 to 9)
                ),
                Number(
                    value = 598,
                    coordinates = listOf(5 to 9, 6 to 9, 7 to 9)
                )
            ),
            symbols = hashMapOf(
                3 to 1 to '*',
                6 to 3 to '#',
                3 to 4 to '*',
                5 to 5 to '+',
                3 to 8 to '$',
                5 to 8 to '*'
            )
        )
        assertEquals(expected, actual)
    }
}
