package day5

import util.readFileLines

private const val fileName = "/day5.txt"

fun solveDay51(fileName: String): Long {
    val data = parseInput(fileName)

    val sortedMaps = listOf(
        data.seedsToSoil,
        data.soilToFertilizer,
        data.fertilizerToWater,
        data.waterToLight,
        data.lightToTemperature,
        data.temperatureToHumidity,
        data.humidityToLocation
    )

    return data.seeds.minOf { getLocationForSeed(it, sortedMaps) }
}

fun solveDay52(fileName: String): Long {
    val data = parseInput(fileName)

    val sortedMaps = listOf(
        data.seedsToSoil,
        data.soilToFertilizer,
        data.fertilizerToWater,
        data.waterToLight,
        data.lightToTemperature,
        data.temperatureToHumidity,
        data.humidityToLocation
    )

    return data.seedRanges.minOf { seedRange -> getLocationsForSeedRange(seedRange, sortedMaps).min() }
}

private fun getLocationsForSeedRange(seedRange: SeedRange, allMaps: List<List<RangeMap>>): List<Long> {
    val locations = mutableListOf<Long>()

    // map index to ranges to process
    val rangesToMap = mutableMapOf<Int, ArrayDeque<SeedRange>>()

    for (mapIndex in allMaps.indices) {
        rangesToMap[mapIndex] = if (mapIndex == 0) {
            ArrayDeque(listOf(seedRange))
        } else {
            ArrayDeque()
        }
    }

    allMaps.forEachIndexed { index, maps ->
        val currentQueue = rangesToMap[index]!!
        val nextQueue = rangesToMap[index + 1]

        for (map in maps) {
            val currentElements = currentQueue.toList()

            for (element in currentElements) {
                if (!element.isOverlapping(map)) {
                    continue
                }
                currentQueue.remove(element)
                val currentRange = element
                val rangeStart = currentRange.start
                val rangeEnd = currentRange.start + currentRange.length
                val mapSourceStart = map.sourceStart
                val mapSourceEnd = map.sourceStart + map.length
                val sourceToDestDiff = map.destinationStart - map.sourceStart

                val intersectStart = if (mapSourceStart < rangeStart) {
                    rangeStart
                } else {
                    mapSourceStart
                }

                val intersectEnd = if (mapSourceEnd > rangeEnd) {
                    rangeEnd
                } else {
                    mapSourceEnd
                }

                if (nextQueue != null) {
                    nextQueue.add(
                        SeedRange(
                            start = intersectStart + sourceToDestDiff,
                            length = intersectEnd - intersectStart
                        )
                    )
                } else {
                    locations.add(intersectStart + sourceToDestDiff)
                }

                if (intersectStart - rangeStart > 0) {
                    currentQueue.add(
                        SeedRange(
                            start = rangeStart,
                            length = intersectStart - rangeStart
                        )
                    )
                }

                if (rangeEnd - intersectEnd > 0) {
                    currentQueue.add(
                        SeedRange(
                            start = intersectEnd,
                            length = rangeEnd - intersectEnd
                        )
                    )
                }
            }
        }
        if (currentQueue.isNotEmpty()) {
            if (nextQueue != null) {
                nextQueue.addAll(currentQueue)
            } else {
                locations.addAll(currentQueue.map { it.start })
            }
        }
    }

    return locations
}

private fun getLocationForSeedRange(seedRange: SeedRange, allMaps: List<List<RangeMap>>): Long {
    var locations = listOf(seedRange)
    for (maps in allMaps) {
        locations = getNextValues(locations, maps)
    }
    return locations.minOf { it.start }
}


private fun getNextValues(seedRanges: List<SeedRange>, maps: List<RangeMap>): List<SeedRange> {
    val mapped = mutableListOf<SeedRange>()
    val leftToMap = ArrayDeque(seedRanges)

    for (map in maps) {
        while (leftToMap.isNotEmpty() && leftToMap.first().isOverlapping(map)) {
            val range = leftToMap.removeFirst()
            val rangeStart = range.start
            val rangeEnd = range.start + range.length
            val mapSourceEnd = map.sourceStart + map.length
            val mapSourceStart = map.sourceStart
            val sourceToDestDiff = map.destinationStart - map.sourceStart
            println("start_range = ${range.start} length = ${range.length}")

            val intersectStart = if (mapSourceStart < rangeStart) {
                rangeStart
            } else {
                mapSourceStart
            }

            val intersectEnd = if (mapSourceEnd > rangeEnd) {
                rangeEnd
            } else {
                mapSourceEnd
            }

            mapped.add(
                SeedRange(
                    start = intersectStart + sourceToDestDiff,
                    length = intersectEnd - intersectStart
                )
            )
            if (intersectStart - rangeStart > 0) {
                leftToMap.add(
                    SeedRange(
                        start = rangeStart,
                        length = intersectStart - rangeStart
                    )
                )
            }

            if (rangeEnd - intersectEnd > 0) {
                leftToMap.add(
                    SeedRange(
                        start = intersectEnd,
                        length = rangeEnd - intersectEnd
                    )
                )
            }
        }
    }

    mapped.addAll(leftToMap)
    return mapped
}

private fun SeedRange.isOverlapping(map: RangeMap): Boolean {
    val start = start
    val end = start + length
    val mapStart = map.sourceStart
    val mapEnd = mapStart + map.length

    return start < mapEnd && mapStart < end
}

private fun getLocationForSeed(seed: Long, allMaps: List<List<RangeMap>>): Long {
    var value = seed
    for (maps in allMaps) {
        value = getNextValue(currentValue = value, maps = maps)
    }
    return value
}

private fun getNextValue(currentValue: Long, maps: List<RangeMap>): Long {
    for (map in maps) {
        val isInRange = currentValue >= map.sourceStart && currentValue - map.sourceStart <= map.length
        if (isInRange) {
            return map.destinationStart + (currentValue - map.sourceStart)
        }
    }
    return currentValue
}

data class Data(
    val seeds: List<Long>,
    val seedRanges: List<SeedRange>,
    val seedsToSoil: List<RangeMap>,
    val soilToFertilizer: List<RangeMap>,
    val fertilizerToWater: List<RangeMap>,
    val waterToLight: List<RangeMap>,
    val lightToTemperature: List<RangeMap>,
    val temperatureToHumidity: List<RangeMap>,
    val humidityToLocation: List<RangeMap>,
)

data class SeedRange(
    val start: Long,
    val length: Long
)

data class RangeMap(
    val destinationStart: Long,
    val sourceStart: Long,
    val length: Long,
)

fun parseInput(fileName: String): Data {
    val lines = readFileLines(fileName)
    val seeds = parseSeeds(lines.first()) // first line is seeds
    val seedRanges = parseSeedRanges(lines.first())
    val maps = parseAllMaps(lines.drop(2)) // dropping seeds and empty line

    return Data(
        seeds = seeds,
        seedRanges = seedRanges,
        seedsToSoil = maps[0],
        soilToFertilizer = maps[1],
        fertilizerToWater = maps[2],
        waterToLight = maps[3],
        lightToTemperature = maps[4],
        temperatureToHumidity = maps[5],
        humidityToLocation = maps[6]
    )

}

private fun parseSeeds(input: String): List<Long> {
    return input.split(":").last().trim().split(" ").map { it.toLong() }
}

private fun parseSeedRanges(input: String): List<SeedRange> {
    return input.split(":").last().trim().split(" ").chunked(2).map {
        SeedRange(
            start = it[0].toLong(),
            length = it[1].toLong()
        )
    }
}

private fun parseAllMaps(input: List<String>): List<List<RangeMap>> {
    val mapsToParse = mutableListOf<List<String>>()
    var currentMap = mutableListOf<String>()
    for (line in input) {
        if (line.isNotBlank()) {
            currentMap.add(line)
        } else {
            mapsToParse.add(currentMap)
            currentMap = mutableListOf()
        }
    }
    if (currentMap.isNotEmpty()) {
        mapsToParse.add(currentMap)
    }
    return mapsToParse.map { parseMaps(it) }
}

private fun parseMaps(input: List<String>): List<RangeMap> {
    return input.drop(1).map { parseMap(it) } // dropping name
}

private fun parseMap(input: String): RangeMap {
    val (destination, source, length) = input.split(" ")
    return RangeMap(
        destinationStart = destination.toLong(),
        sourceStart = source.toLong(),
        length = length.toLong()
    )
}

fun main() {
    println(solveDay51(fileName))
    println(solveDay52(fileName))
}
