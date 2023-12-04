import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val firstSplit = line.split(" | ")

            val winningNumbers = firstSplit
                .first()
                .split(": ")
                .last()
                .split(" ")
                .filterNot { s: String -> s.isEmpty() }
                .map { s: String -> s.toInt() }

            val myNumbers = firstSplit
                .last()
                .split(" ")
                .filterNot { s: String -> s.isEmpty() }
                .map { s: String -> s.toInt() }

            val numberOfMatchingNumbers = myNumbers.count { myNumber -> myNumber in winningNumbers }

            if (numberOfMatchingNumbers <= 0)
                0
            else
                2.toDouble().pow(numberOfMatchingNumbers - 1).toInt()
        }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day04_Test")
    check(part1(testInput) == 13)
    // check(part2(testInput) == 0)

    val input = readInput("Day04")
    part1(input).println()
    // part2(input).println()
}
