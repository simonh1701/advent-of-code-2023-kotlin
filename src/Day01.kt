fun main() {
    fun part1(input: List<String>): Int {
        val integerChars = '0'..'9'

        val calibrationValues = input
            .map { line -> "${line.find { it in integerChars }}${line.reversed().find { it in integerChars }}" }
            .map { it.toInt() }

        return calibrationValues.reduce { acc, value -> acc + value }
    }

    fun part2(input: List<String>): Int {
        val integerStrings = mapOf(
            "1" to 1,
            "2" to 2,
            "3" to 3,
            "4" to 4,
            "5" to 5,
            "6" to 6,
            "7" to 7,
            "8" to 8,
            "9" to 9,
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9
        )

        val calibrationValues = input
            .map { line ->
                "${
                    integerStrings[line.findAnyOf(strings = integerStrings.keys)!!.second]
                }${
                    integerStrings[line.findLastAnyOf(strings = integerStrings.keys)!!.second]
                }"
            }
            .map { it.toInt() }

        return calibrationValues.reduce { acc, value -> acc + value }
    }

    val testInputPart1 = readInput("Day01_Part1_Test")
    check(part1(testInputPart1) == 142)

    val testInputPart2 = readInput("Day01_Part2_Test")
    check(part2(testInputPart2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
