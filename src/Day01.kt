fun main() {
    fun part1(input: List<String>): Int {
        val integerChars = '0'..'9'

        val calibrationValues = input
            .map { line -> "${line.find { it in integerChars }}${line.reversed().find { it in integerChars }}" }
            .map { it.toInt() }

        return calibrationValues.reduce { acc, value -> acc + value }
    }

    /*fun part2(input: List<String>): Int {
        return input.size
    }*/

    val testInput = readInput("Day01_Part1_Test")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()
    // part2(input).println()
}
