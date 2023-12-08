fun main() {
    fun part1(input: List<String>): Int {
        val network = createNetworkFromInput(input)

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

    fun part2(input: List<String>): Long {
        val network = createNetworkFromInput(input)

        val instructions = input.first().toCharArray()

        val startNodes = network.keys.filter { it.endsWith('A') }.toList()

        return findLCMOfListOfNumbers(
            // This converts every node to the number of turns it takes to reach the first node that ends with 'Z'
            startNodes.map { startNode ->
                var currentNode = startNode
                var turns = 0

                while (!currentNode.endsWith('Z')) {
                    val turnToTake = instructions[turns % instructions.size]
                    currentNode = if (turnToTake == 'L') network[currentNode]!!.left else network[currentNode]!!.right
                    turns++
                }

                turns.toLong()
            }
        )
    }

    val testInput1 = readInput("Day08_Test1")
    val testInput2 = readInput("Day08_Test2")
    check(part1(testInput1) == 2)
    check(part1(testInput2) == 6)
    val testInput3 = readInput("Day08_Test3")
    check(part2(testInput3) == 6.toLong())

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}

data class Direction(val left: String, val right: String)

fun createNetworkFromInput(input: List<String>): Map<String, Direction> {
    return input.drop(2).associate { line ->
        val node = line.split(" = ").first()
        val leftAndRight = line.split(" = ").last().trim('(').trim(')').split(", ")
        val left = leftAndRight.first()
        val right = leftAndRight.last()

        Pair(node, Direction(left, right))
    }
}

// The following two functions were adapted from Ezra Kanake at https://www.baeldung.com/kotlin/lcm.
// Date accessed: 2023-12-08

/**
 * Finds the least common multiple of a and b
 */
fun findLCM(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0.toLong() && lcm % b == 0.toLong()) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

/**
 * Finds the least common multiple of all numbers in a list
 */
fun findLCMOfListOfNumbers(numbers: List<Long>): Long {
    // This function was modified from the original source code

    // Original code:
    /*
    var result = numbers[0]
    for (i in 1 until numbers.size) {
        result = findLCM(result, numbers[i])
    }
    return result
    */

    return numbers.reduce { acc, l -> findLCM(acc, l) }
}
