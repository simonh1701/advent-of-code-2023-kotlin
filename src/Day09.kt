fun main() {
    fun part1(input: List<String>): Int {
        val numberLists = input.map { line -> line.split(" ").map(String::toInt) }
        return numberLists.sumOf { it.next() }
    }

    fun part2(input: List<String>): Int {
        val numberLists = input.map { line -> line.split(" ").map(String::toInt) }
        return numberLists.sumOf { it.previous() }
    }

    val testInput = readInput("Day09_Test")
    check(part1(testInput) == 114)
    check(part2(testInput) == 2)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}

fun List<Int>.next(): Int {
    if (this.all { it == 0 }) return 0

    val differences = this.windowed(2, 1).map { x -> x.last() - x.first() }

    return this.last() + differences.next()
}

fun List<Int>.previous(): Int {
    if (this.all { it == 0 }) return 0

    val differences = this.windowed(2, 1).map { x -> x.last() - x.first() }

    return this.first() - differences.previous()
}
