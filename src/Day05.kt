fun main() {
    fun part1(input: String): Long {
        val seeds: List<Long> = input.substringAfter("seeds: ").lines().first().split(" ").map(String::toLong)
        val maps = generateMaps(input)
        val locations: List<Long> = seeds.map { seed -> seedToLocation(seed, maps) }

        return locations.min()
    }

    fun part2(input: List<String>): Long {
        val seedRanges = input.first().substringAfter(" ").split(" ").map(String::toLong).chunked(2)
            .map { it.first()..<(it.first() + it.last()) }

        val maps = input.drop(2).joinToString("\n").split("\n\n").map { section ->
            section.lines().drop(1).associate {
                it.split(" ").map(String::toLong).let { (dest, source, length) ->
                    source..<(source + length) to dest..<(dest + length)
                }
            }
        }

        val locationRanges = seedRanges.flatMap { seedsRange ->
            maps.fold(listOf(seedsRange)) { aac, map ->
                aac.flatMap { getOutputRanges(map, it) }
            }
        }

        return locationRanges.minOf { it.first }
    }

    val testInputAsString = readInputAsString("Day05_Test")
    val testInput = readInput("Day05_Test")
    check(part1(testInputAsString).toInt() == 35)
    check(part2(testInput).toInt() == 46)

    val inputAsString = readInputAsString("Day05")
    val input = readInput("Day05")
    part1(inputAsString).println()
    part2(input).println()
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

fun seedToLocation(seed: Long, maps: Maps): Long {
    val soil = mapValueWithListOfMapEntries(seed, maps.seedToSoilMap)
    val fertilizer = mapValueWithListOfMapEntries(soil, maps.soilToFertilizerMap)
    val water = mapValueWithListOfMapEntries(fertilizer, maps.fertilizerToWaterMap)
    val light = mapValueWithListOfMapEntries(water, maps.waterToLightMap)
    val temperature = mapValueWithListOfMapEntries(light, maps.lightToTemperatureMap)
    val humidity = mapValueWithListOfMapEntries(temperature, maps.temperatureToHumidityMap)
    return mapValueWithListOfMapEntries(humidity, maps.humidityToLocationMap)
}

data class Maps(
    val seedToSoilMap: List<MapEntry>,
    val soilToFertilizerMap: List<MapEntry>,
    val fertilizerToWaterMap: List<MapEntry>,
    val waterToLightMap: List<MapEntry>,
    val lightToTemperatureMap: List<MapEntry>,
    val temperatureToHumidityMap: List<MapEntry>,
    val humidityToLocationMap: List<MapEntry>,
)

data class MapEntry(val sourceRangeStart: Long, val range: Long, val transform: Long)

fun generateMaps(input: String): Maps {
    val seedToSoilMapLines =
        input.substringAfter("seed-to-soil map:").substringBefore("soil-to-fertilizer map:").lines()
            .filter { it.isNotBlank() }

    val soilToFertilizerMapLines =
        input.substringAfter("soil-to-fertilizer map:").substringBefore("fertilizer-to-water map:").lines()
            .filter { it.isNotBlank() }

    val fertilizerToWaterMapLines =
        input.substringAfter("fertilizer-to-water map:").substringBefore("water-to-light map:").lines()
            .filter { it.isNotBlank() }

    val waterToLightMapLines =
        input.substringAfter("water-to-light map:").substringBefore("light-to-temperature map:").lines()
            .filter { it.isNotBlank() }

    val lightToTemperatureMapLines =
        input.substringAfter("light-to-temperature map:").substringBefore("temperature-to-humidity map:").lines()
            .filter { it.isNotBlank() }

    val temperatureToHumidityMapLines =
        input.substringAfter("temperature-to-humidity map:").substringBefore("humidity-to-location map:").lines()
            .filter { it.isNotBlank() }

    val humidityToLocationMapLines =
        input.substringAfter("humidity-to-location map:").lines().filter { it.isNotBlank() }

    val seedToSoilMap = toListOfMapEntries(seedToSoilMapLines)
    val soilToFertilizerMap = toListOfMapEntries(soilToFertilizerMapLines)
    val fertilizerToWaterMap = toListOfMapEntries(fertilizerToWaterMapLines)
    val waterToLightMap = toListOfMapEntries(waterToLightMapLines)
    val lightToTemperatureMap = toListOfMapEntries(lightToTemperatureMapLines)
    val temperatureToHumidityMap = toListOfMapEntries(temperatureToHumidityMapLines)
    val humidityToLocationMap = toListOfMapEntries(humidityToLocationMapLines)

    return Maps(
        seedToSoilMap,
        soilToFertilizerMap,
        fertilizerToWaterMap,
        waterToLightMap,
        lightToTemperatureMap,
        temperatureToHumidityMap,
        humidityToLocationMap
    )
}

/**
 * Maps a given input range to a list of output ranges.
 */
fun getOutputRanges(map: Map<LongRange, LongRange>, input: LongRange): List<LongRange> {
    // In this list all input ranges are stored that were actually mapped to a different output range
    val mappedInputRanges = mutableListOf<LongRange>()

    val outputRanges = map.entries.mapNotNull { (source, dest) ->

        // Find the start and the end of the range in the input range that is actually mapped.
        // Meaning, find the endpoints of the range that is in both source AND input
        val start = maxOf(source.first, input.first)
        val end = minOf(source.last, input.last)

        if (start <= end) {
            mappedInputRanges += start..end  // Add the "intersection" range to the list of mapped input ranges
            (dest.first - source.first).let { (start + it)..(end + it) }  // Covert the map entry to the output range
        } else null  // If start is greater than end than the input range and the current map entry do not overlap, meaning no input is mapped.
    }

    // Sort mapped input ranges by first value
    mappedInputRanges.sortBy { it.first }

    val cuts =
        listOf(input.first) + mappedInputRanges.flatMap { listOf(it.first, it.last) } + listOf(input.last)//.distinct()
            .sorted()

    val unmappedInputRanges = cuts.chunked(2).mapNotNull { (first, second) ->
        if (second - first > 1) {
            if (first == cuts.first()) {
                first..<second
            } else if (second == cuts.last()) {
                first + 1..second
            } else {
                first + 1..<second
            }
        } else null
    }

    return outputRanges + unmappedInputRanges
}
