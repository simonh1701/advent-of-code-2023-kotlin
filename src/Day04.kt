import kotlin.math.pow

fun main() {
    fun getWinningNumbers(input: List<String>) = input
        .first()
        .split(": ")
        .last()
        .split(" ")
        .filterNot { s: String -> s.isEmpty() }
        .map { s: String -> s.toInt() }

    fun getMyNumbers(input: List<String>) = input
        .last()
        .split(" ")
        .filterNot { s: String -> s.isEmpty() }
        .map { s: String -> s.toInt() }

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val firstSplit = line.split(" | ")

            val winningNumbers = getWinningNumbers(firstSplit)

            val myNumbers = getMyNumbers(firstSplit)

            val numberOfMatchingNumbers = myNumbers.count { myNumber -> myNumber in winningNumbers }

            if (numberOfMatchingNumbers <= 0)
                0
            else
                2.toDouble().pow(numberOfMatchingNumbers - 1).toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val mapOfGameNumberToNumberOfWinningNumbersAndNumberOfCards: MutableMap<Int, Pair<Int, Int>> =
            input.map { line ->
                val firstSplit = line.split(" | ")

                val gameNumber = firstSplit
                    .first()
                    .split(": ")
                    .first()
                    .split(" ")
                    .last()
                    .toInt()

                val winningNumbers = getWinningNumbers(firstSplit)

                val myNumbers = getMyNumbers(firstSplit)

                val numberOfMatchingNumbers = myNumbers.count { myNumber -> myNumber in winningNumbers }

                Pair(gameNumber, Pair(numberOfMatchingNumbers, 1))
            }.associateBy({ it.first }, { it.second }).toMutableMap()

        for (entry in mapOfGameNumberToNumberOfWinningNumbersAndNumberOfCards) {
            val numberOfWinningNumbers = entry.value.first
            val numberOfCards = entry.value.second
            val currentGameNumber = entry.key

            for (i in currentGameNumber + 1..currentGameNumber + numberOfWinningNumbers) {
                val pairOfNumberOfWinningNumbersAndNumberOfCards =
                    mapOfGameNumberToNumberOfWinningNumbersAndNumberOfCards[i]

                pairOfNumberOfWinningNumbersAndNumberOfCards?.let {
                    mapOfGameNumberToNumberOfWinningNumbersAndNumberOfCards[i] =
                        it.copy(second = it.second + numberOfCards)
                }
            }
        }

        return mapOfGameNumberToNumberOfWinningNumbersAndNumberOfCards.map { entry -> entry.value.second }.sum()
    }

    val testInput = readInput("Day04_Test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
