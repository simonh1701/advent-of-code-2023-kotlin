fun main() {
    fun part1(input: String): Int {
        val regex = Regex("\\d+")
        val matchResults = regex.findAll(input)

        val inputLines = input.lines()
        val inputHeight = inputLines.size
        val inputWidth = inputLines.first().length

        return matchResults.filter { matchResult ->
            val range = matchResult.range
            val coordinatesOfMatch = range.map { index -> input.indexToCoordinate(index) }
            val coordinatesToCheck = coordinatesOfMatch.flatMap { coordinate ->
                coordinate.getAllCoordinatesAround(
                    inputHeight, inputWidth
                )
            }.toSet()

            // Filter out any digits and check if any of the remaining characters is not a dot, meaning there is an adjacent symbol and the number is therefore valid
            coordinatesToCheck
                .map { coordinate: Coordinate -> input.getCharAtCoordinate(coordinate) }
                .filter { c: Char -> !c.isDigit() }
                .any { c: Char -> c != '.' }
        }.sumOf { matchResult -> matchResult.value.toInt() }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInputAsString("Day03_Test")
    check(allLinesAreEquallyLong(testInput))
    check(part1(testInput) == 4361)
    // check(part2(testInput) == 0)

    val input = readInputAsString("Day03")
    check(allLinesAreEquallyLong(input))
    part1(input).println()
    // part2(input).println()
}

fun allLinesAreEquallyLong(input: String): Boolean {
    val lengthOfFirstLine = input.lines().first().length
    return input.lines().all { line -> line.length == lengthOfFirstLine }
}

data class Coordinate(val row: Int, val col: Int)

fun String.indexToCoordinate(index: Int): Coordinate {
    val lines = this.lines()
    val width =
        lines.first().length + 1  // + 1 to account for the line delimiter that was removed from each line by calling .lines()

    val row = index / width
    val col = index % width

    return Coordinate(row, col)
}

fun Coordinate.getAllCoordinatesAround(height: Int, width: Int): Set<Coordinate> {
    val rowRange = (this.row - 1).coerceAtLeast(0)..(this.row + 1).coerceAtMost(height - 1)
    val colRange = (this.col - 1).coerceAtLeast(0)..(this.col + 1).coerceAtMost(width - 1)

    return rowRange.flatMap { row -> colRange.map { col -> Coordinate(row, col) } }.toSet()
}

fun String.getCharAtCoordinate(coordinate: Coordinate): Char {
    val stringLines = this.split("\n")

    return stringLines[coordinate.row].elementAt(coordinate.col)
}
