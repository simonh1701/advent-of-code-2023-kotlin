fun main() {
    fun part1(input: List<String>): Int {
        val timesInMs = input[0].substringAfter("Time:").split(" ").filter { it.isNotBlank() }.map { it.trim() }.map(String::toInt)
        val distancesInMm = input[1].substringAfter("Distance:").split(" ").filter { it.isNotBlank() }.map { it.trim() }.map(String::toInt)

        return timesInMs
                .mapIndexed { index, time -> Pair(time, distancesInMm[index]) }
                .map { countNumberOfWaysToBeatTheRecord(it.first, it.second) }
                .fold(1) { acc: Int, i: Int ->
                    acc * i
                }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day06_Test")
    check(part1(testInput) == 288)
    // check(part2(testInput) == 0)

    val input = readInput("Day06")
    part1(input).println()
    // part2(input).println()
}

fun countNumberOfWaysToBeatTheRecord(raceTimeInMs: Int, recordDistanceInMm: Int): Int {
    return (0..raceTimeInMs).map { buttonHoldTime ->
        buttonHoldTime * (raceTimeInMs - buttonHoldTime)
    }.count { it > recordDistanceInMm }
}
