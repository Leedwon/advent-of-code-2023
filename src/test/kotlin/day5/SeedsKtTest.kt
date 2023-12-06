package day5

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SeedsKtTest {

    @Test
    fun solvesDay51() {
        val fileName = "/day5.txt"

        val actual = solveDay51(fileName)
        val expected = 35L

        assertEquals(expected, actual)
    }

    @Test
    fun solvesDay52() {
        val fileName = "/day5.txt"

        val actual = solveDay52(fileName)
        val expected = 46L

        assertEquals(expected, actual)
    }

    @Test
    fun parsesInput() {
        val fileName = "/day5.txt"

        val actual = parseInput(fileName)

        val expected = Data(
            seeds = listOf(79, 14, 55, 13),
            seedRanges = listOf(
                SeedRange(start = 79, length = 14),
                SeedRange(start = 55, length = 13),
            ) ,
            seedsToSoil = listOf(
                RangeMap(destinationStart = 50, sourceStart = 98, length = 2),
                RangeMap(destinationStart = 52, sourceStart = 50, length = 48),
            ),
            soilToFertilizer = listOf(
                RangeMap(destinationStart = 0, sourceStart = 15, length = 37),
                RangeMap(destinationStart = 37, sourceStart = 52, length = 2),
                RangeMap(destinationStart = 39, sourceStart = 0, length = 15),
            ),
            fertilizerToWater = listOf(
                RangeMap(destinationStart = 49, sourceStart = 53, length = 8),
                RangeMap(destinationStart = 0, sourceStart = 11, length = 42),
                RangeMap(destinationStart = 42, sourceStart = 0, length = 7),
                RangeMap(destinationStart = 57, sourceStart = 7, length = 4),
            ),
            waterToLight = listOf(
                RangeMap(destinationStart = 88, sourceStart = 18, length = 7),
                RangeMap(destinationStart = 18, sourceStart = 25, length = 70),
            ),
            lightToTemperature = listOf(
                RangeMap(destinationStart = 45, sourceStart = 77, length = 23),
                RangeMap(destinationStart = 81, sourceStart = 45, length = 19),
                RangeMap(destinationStart = 68, sourceStart = 64, length = 13),
            ),
            temperatureToHumidity = listOf(
                RangeMap(destinationStart = 0, sourceStart = 69, length = 1),
                RangeMap(destinationStart = 1, sourceStart = 0, length = 69),
            ),
            humidityToLocation = listOf(
                RangeMap(destinationStart = 60, sourceStart = 56, length = 37),
                RangeMap(destinationStart = 56, sourceStart = 93, length = 4),
            )
        )

        assertEquals(expected, actual)
    }
}
