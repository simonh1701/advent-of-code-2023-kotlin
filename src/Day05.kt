fun main() {
    fun part1(input: String): Long {
        val seeds: List<Long> = input.substringAfter("seeds: ").lines().first().split(" ").map(String::toLong)
        val locations: List<Long> = seeds.map { seed -> seedToLocation(seed, input) }

        return locations.min()
    }

    fun part2(input: String): Long {
        return 0
    }

    val testInput = readInputAsString("Day05_Test")
    check(part1(testInput).toInt() == 35)
    // check(part2(testInput) == 0)

    val input = readInputAsString("Day05")
    part1(input).println()
    // part2(input).println()
}

fun toListOfMapEntries(stringList: List<String>): List<MapEntry> {
    return stringList.map { line ->
        val values = line.split(" ").map(String::toLong)
        val destRangeStart = values[0]
        val sourceRangeStart = values[1]
        val range = values[2]

        val transform = destRangeStart - sourceRangeStart

        MapEntry(sourceRangeStart, range, transform)
    }
}

fun mapValueWithListOfMapEntries(valueToMap: Long, listOfMapEntries: List<MapEntry>): Long {
    val mapEntry = listOfMapEntries.find { entry ->
        val offsetFromSourceRangeStart = valueToMap - entry.sourceRangeStart
        offsetFromSourceRangeStart >= 0 && offsetFromSourceRangeStart <= entry.range
    }

    val transform = mapEntry?.transform ?: 0

    return valueToMap + transform
}

fun seedToLocation(seed: Long, input: String): Long {
    val seedToSoilMapLines = input
        .substringAfter("seed-to-soil map:")
        .substringBefore("soil-to-fertilizer map:")
        .lines().filter { it.isNotBlank() }

    val soilToFertilizerMapLines = input
        .substringAfter("soil-to-fertilizer map:")
        .substringBefore("fertilizer-to-water map:")
        .lines().filter { it.isNotBlank() }

    val fertilizerToWaterMapLines = input
        .substringAfter("fertilizer-to-water map:")
        .substringBefore("water-to-light map:")
        .lines().filter { it.isNotBlank() }

    val waterToLightMapLines = input
        .substringAfter("water-to-light map:")
        .substringBefore("light-to-temperature map:")
        .lines().filter { it.isNotBlank() }

    val lightToTemperatureMapLines = input
        .substringAfter("light-to-temperature map:")
        .substringBefore("temperature-to-humidity map:")
        .lines().filter { it.isNotBlank() }

    val temperatureToHumidityMapLines = input
        .substringAfter("temperature-to-humidity map:")
        .substringBefore("humidity-to-location map:")
        .lines().filter { it.isNotBlank() }

    val humidityToLocationMapLines = input
        .substringAfter("humidity-to-location map:")
        .lines().filter { it.isNotBlank() }

    val seedToSoilMap = toListOfMapEntries(seedToSoilMapLines)
    val soilToFertilizerMap = toListOfMapEntries(soilToFertilizerMapLines)
    val fertilizerToWaterMap = toListOfMapEntries(fertilizerToWaterMapLines)
    val waterToLightMap = toListOfMapEntries(waterToLightMapLines)
    val lightToTemperatureMap = toListOfMapEntries(lightToTemperatureMapLines)
    val temperatureToHumidityMap = toListOfMapEntries(temperatureToHumidityMapLines)
    val humidityToLocationMap = toListOfMapEntries(humidityToLocationMapLines)

    val soil = mapValueWithListOfMapEntries(seed, seedToSoilMap)
    val fertilizer = mapValueWithListOfMapEntries(soil, soilToFertilizerMap)
    val water = mapValueWithListOfMapEntries(fertilizer, fertilizerToWaterMap)
    val light = mapValueWithListOfMapEntries(water, waterToLightMap)
    val temperature = mapValueWithListOfMapEntries(light, lightToTemperatureMap)
    val humidity = mapValueWithListOfMapEntries(temperature, temperatureToHumidityMap)
    val location = mapValueWithListOfMapEntries(humidity, humidityToLocationMap)

    return location
}

data class MapEntry(val sourceRangeStart: Long, val range: Long, val transform: Long)
