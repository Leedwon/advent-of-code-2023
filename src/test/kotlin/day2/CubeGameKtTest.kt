package day2

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CubeGameKtTest {

    @Test
    fun solvesDay11() {
        val actual = solveDay11("/day2.txt")
        val expected = 8
        assertEquals(expected, actual)
    }

    @Test
    fun solvesDay12() {
        val actual = solveDay12("/day2.txt")
        val expected = 2286
        assertEquals(expected, actual)
    }

    @Test
    fun parsesGameInput() {
        val actual = parseCubeGames("/day2.txt")

        val expected = listOf(
            Game(
                id = 1,
                rounds = listOf(
                    Round(blue = 3, red = 4),
                    Round(red = 1, green = 2, blue = 6),
                    Round(green = 2)
                )
            ),
            Game(
                id = 2,
                rounds = listOf(
                    Round(blue = 1, green = 2),
                    Round(green = 3, blue = 4, red = 1),
                    Round(green = 1, blue = 1)
                )
            ),
            Game(
                id = 3,
                rounds = listOf(
                    Round(green = 8, blue = 6, red = 20),
                    Round(blue = 5, red = 4, green = 13),
                    Round(green = 5, red = 1)
                )
            ),
            Game(
                id = 4,
                rounds = listOf(
                    Round(green = 1, red = 3, blue = 6),
                    Round(green = 3, red = 6),
                    Round(green = 3, blue = 15, red = 14)
                )
            ),
            Game(
                id = 5,
                rounds = listOf(
                    Round(red = 6, blue = 1, green = 3),
                    Round(blue = 2, red = 1, green = 2)
                )
            )
        )

        assertEquals(expected, actual)
    }
}
