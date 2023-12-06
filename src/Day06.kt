fun main() {
    fun part1(input: List<String>): Long {
        val timesInMs = input[0].substringAfter("Time:").split(" ").filter { it.isNotBlank() }.map { it.trim() }.map(String::toLong)
        val distancesInMm = input[1].substringAfter("Distance:").split(" ").filter { it.isNotBlank() }.map { it.trim() }.map(String::toLong)

        return timesInMs
                .mapIndexed { index, time -> Pair(time, distancesInMm[index]) }
                .map { countNumberOfWaysToBeatTheRecord(it.first, it.second) }
                .fold(1) { acc: Long, i: Long -> acc * i }
    }

    fun part2(input: List<String>): Long {
        val timeInMs = input[0].substringAfter("Time:").split(" ").filter { it.isNotBlank() }.joinToString(separator = "") { it.trim() }.toLong()
        val distanceInMm = input[1].substringAfter("Distance:").split(" ").filter { it.isNotBlank() }.joinToString(separator = "") { it.trim() }.toLong()

        return countNumberOfWaysToBeatTheRecord(timeInMs, distanceInMm)
    }

    val testInput = readInput("Day06_Test")
    check(part1(testInput) == (288).toLong())
    check(part2(testInput) == (71503).toLong())

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

fun countNumberOfWaysToBeatTheRecord(raceTimeInMs: Long, recordDistanceInMm: Long): Long {
    return (0..raceTimeInMs).map { buttonHoldTime ->
        buttonHoldTime * (raceTimeInMs - buttonHoldTime)
    }.count { it > recordDistanceInMm }.toLong()
}
