import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1And2(input: String, expansionFactor: Int): Long {
        val rowsToExpand = rowsToExpand(input)
        val columnsToExpand = columnsToExpand(input)

        val regex = Regex("#")
        val matchResults = regex.findAll(input)
        val coordinatesOfGalaxies = matchResults.map { matchResult ->
            input.indexToUniverseCoordinate(matchResult.range.first)
        }.toList()

        return coordinatesOfGalaxies.flatMapIndexed { index, coordinatesOfThisGalaxy ->
            coordinatesOfGalaxies.drop(index + 1).map { coordinatesOtherGalaxy ->
                coordinatesOfThisGalaxy.distanceTo(
                    coordinatesOtherGalaxy, expansionFactor, rowsToExpand, columnsToExpand
                )
            }
        }.sum()
    }

    val testInput = readInputAsString("Day11_Test")
    check(part1And2(testInput, expansionFactor = 2) == 374.toLong())
    check(part1And2(testInput, expansionFactor = 10) == 1030.toLong())
    check(part1And2(testInput, expansionFactor = 100) == 8410.toLong())

    val input = readInputAsString("Day11")
    part1And2(input, expansionFactor = 2).println()
    part1And2(input, expansionFactor = 1_000_000).println()
}

data class UniverseCoordinate(val row: Int, val col: Int)

fun String.indexToUniverseCoordinate(index: Int): UniverseCoordinate {
    val lines = this.lines()
    val width =
        lines.first().length + 1  // + 1 to account for the line delimiter that was removed from each line by calling .lines()

    val row = index / width
    val col = index % width

    return UniverseCoordinate(row, col)
}

fun rowsToExpand(universeMap: String): Set<Int> {
    return universeMap.lines().mapIndexedNotNull { index, line ->
        if (line.all { it == '.' }) index else null
    }.toSet()
}

fun columnsToExpand(universeMap: String): Set<Int> {
    return universeMap.lines().first().mapIndexedNotNull { index, _ ->
        if (universeMap.lines().all { it[index] == '.' }) index else null
    }.toSet()
}

fun UniverseCoordinate.distanceTo(
    other: UniverseCoordinate, expansionFactor: Int, rowsToExpand: Set<Int>, columnsToExpand: Set<Int>
): Long {
    val smallerRow = min(this.row, other.row)
    val greaterRow = max(this.row, other.row)
    val smallerCol = min(this.col, other.col)
    val greaterCol = max(this.col, other.col)

    val crossedRows = smallerRow + 1..greaterRow
    val crossedColumns = smallerCol + 1..greaterCol

    val numberOfCrossedRows = crossedRows.count()
    val numberOfCrossedColumns = crossedColumns.count()

    val numberOfCrossedExpandedRows = crossedRows.intersect(rowsToExpand).count()
    val numberOfCrossedExpandedColumns = crossedColumns.intersect(columnsToExpand).count()

    val numberOfCrossedNonExpandedRows = numberOfCrossedRows - numberOfCrossedExpandedRows
    val numberOfCrossedNonExpandedColumns = numberOfCrossedColumns - numberOfCrossedExpandedColumns

    val realNumberOfCrossedRows: Long =
        (numberOfCrossedNonExpandedRows + numberOfCrossedExpandedRows * expansionFactor).toLong()
    val realNumberOfCrossedColumns: Long =
        (numberOfCrossedNonExpandedColumns + numberOfCrossedExpandedColumns * expansionFactor).toLong()

    return realNumberOfCrossedRows + realNumberOfCrossedColumns
}
