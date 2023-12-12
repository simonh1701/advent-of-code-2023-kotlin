fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val continuousGroups = line.split(' ').last().split(',').map { it.toInt() }
            val conditionRecord = line.split(' ').first()
            val allVariants = generateAllVariants(conditionRecord)
            allVariants.map {
                checkIfConditionRecordIsAPossibleArrangementAccordingToTheContinuousGroupsOfDamagedSprings(
                    it,
                    continuousGroups
                )
            }.count { it }
        }
    }

    /* fun part2(input: List<String>): Int {
        return input.size
    } */

    val testInput = readInput("Day12_Test")
    check(part1(testInput) == 21)
    // check(part2(testInput) == 0)

    val input = readInput("Day12")
    part1(input).println()
    // part2(input).println()
}

fun checkIfConditionRecordIsAPossibleArrangementAccordingToTheContinuousGroupsOfDamagedSprings(
    conditionRecord: String,
    expectedContinuousGroupsOfDamagedSprings: List<Int>
): Boolean {
    val actualContinuousGroupsOfDamagedSprings = conditionRecord.split('.').filter { it.isNotBlank() }.map { it.length }
    return actualContinuousGroupsOfDamagedSprings == expectedContinuousGroupsOfDamagedSprings
}

fun generateAllVariants(conditionRecordWithPlaceholders: String): Set<String> {
    val numberOfPlaceholders = conditionRecordWithPlaceholders.count { it == '?' }

    val replacedByPoint = conditionRecordWithPlaceholders.replaceFirst('?', '.')
    val replacedByHashtag = conditionRecordWithPlaceholders.replaceFirst('?', '#')

    if (numberOfPlaceholders <= 1) return setOf(replacedByPoint, replacedByHashtag)

    return generateAllVariants(replacedByPoint) + generateAllVariants(replacedByHashtag)
}