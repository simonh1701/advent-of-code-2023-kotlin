fun main() {
    fun part1(input: List<String>): Int {
        val network = input.drop(2).associate { line ->
            val node = line.split(" = ").first()
            val leftAndRight = line.split(" = ").last().trim('(').trim(')').split(", ")
            val left = leftAndRight.first()
            val right = leftAndRight.last()

            Pair(node, Direction(left, right))
        }

        val instructions = input.first().toCharArray()

        var currentNode = "AAA"
        var turns = 0

        while (currentNode != "ZZZ") {
            val turnToTake = instructions[turns % instructions.size]
            currentNode = if (turnToTake == 'L') network[currentNode]!!.left else network[currentNode]!!.right
            turns++
        }

        return turns
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput1 = readInput("Day08_Test1")
    val testInput2 = readInput("Day08_Test2")
    check(part1(testInput1) == 2)
    check(part1(testInput2) == 6)
    // check(part2(testInput) == 0)

    val input = readInput("Day08")
    part1(input).println()
    // part2(input).println()
}

data class Direction(val left: String, val right: String)

