fun main() {
    fun part1(input: String): Int {
        val regex = Regex("S")
        val matchResult = regex.find(input)!!
        val coordinateOfStart = input.indexToLandscapeCoordinate(matchResult.range.first)

        var currentField = MoveDirection.values().firstNotNullOf { startDirection ->
            val nextFieldInThisStartDirection = Field.create(input, coordinateOfStart, startDirection).next()
            if (!nextFieldInThisStartDirection.hasNext()) null else nextFieldInThisStartDirection
        }

        var steps = 1

        while (currentField !is Start) {
            currentField = currentField.next()
            steps++
        }

        return steps / 2
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput1 = readInputAsString("Day10_Test1")
    val testInput2 = readInputAsString("Day10_Test2")
    check(allLinesAreEquallyLong(testInput1))
    check(allLinesAreEquallyLong(testInput2))
    check(part1(testInput1) == 4)
    check(part1(testInput2) == 8)
    // check(part2(testInput) == 0)

    val input = readInputAsString("Day10")
    check(allLinesAreEquallyLong(input))
    part1(input).println()
    // part2(input).println()
}

data class LandscapeCoordinate(val row: Int, val col: Int)

fun String.indexToLandscapeCoordinate(index: Int): LandscapeCoordinate {
    val lines = this.lines()
    val width = lines.first().length + 1  // + 1 to account for the line delimiter that was removed from each line by calling .lines()

    val row = index / width
    val col = index % width

    return LandscapeCoordinate(row, col)
}

fun String.getCharAtLandscapeCoordinate(landscapeCoordinate: LandscapeCoordinate): Char {
    val stringLines = this.split("\n")

    return stringLines[landscapeCoordinate.row].elementAt(landscapeCoordinate.col)
}

enum class MoveDirection {
    NORTH, EAST, SOUTH, WEST
}

interface Field {
    val landscapeMap: String
    val coordinate: LandscapeCoordinate
    val direction: MoveDirection

    fun next(): Field

    fun hasNext(): Boolean

    fun move(moveDirection: MoveDirection): Field {
        val inputLines = landscapeMap.lines()
        val inputHeight = inputLines.size
        val inputWidth = inputLines.first().length

        val newRow = when (moveDirection) {
            MoveDirection.NORTH -> coordinate.row - 1
            MoveDirection.EAST -> coordinate.row
            MoveDirection.SOUTH -> coordinate.row + 1
            MoveDirection.WEST -> coordinate.row
        }

        val newCol = when (moveDirection) {
            MoveDirection.NORTH -> coordinate.col
            MoveDirection.EAST -> coordinate.col + 1
            MoveDirection.SOUTH -> coordinate.col
            MoveDirection.WEST -> coordinate.col - 1
        }

        if (newRow < 0 || newRow >= inputHeight || newCol < 0 || newCol >= inputWidth) throw Error("Tried to move out of bounds of landscape map")

        val newCoordinate = LandscapeCoordinate(newRow, newCol)

        return create(landscapeMap, newCoordinate, moveDirection)
    }

    companion object {
        fun create(landscapeMap: String, coordinate: LandscapeCoordinate, direction: MoveDirection): Field {
            val charAtThisLandscapeCoordinate = landscapeMap.getCharAtLandscapeCoordinate(coordinate)

            return when (charAtThisLandscapeCoordinate) {
                '|' -> VerticalPipe(landscapeMap, coordinate, direction)
                '-' -> HorizontalPipe(landscapeMap, coordinate, direction)
                'L' -> CornerPipeL(landscapeMap, coordinate, direction)
                'J' -> CornerPipeJ(landscapeMap, coordinate, direction)
                '7' -> CornerPipe7(landscapeMap, coordinate, direction)
                'F' -> CornerPipeF(landscapeMap, coordinate, direction)
                '.' -> Ground(landscapeMap, coordinate, direction)
                'S' -> Start(landscapeMap, coordinate, direction)
                else -> throw Error("Unexpected char.")
            }
        }
    }
}

data class VerticalPipe(override val landscapeMap: String, override val coordinate: LandscapeCoordinate, override val direction: MoveDirection) : Field {
    override fun next(): Field {
        if (direction == MoveDirection.EAST || direction == MoveDirection.WEST) throw Error("Vertical Pipe has no next for specified direction")
        return move(direction)
    }

    override fun hasNext(): Boolean {
        return !(direction == MoveDirection.EAST || direction == MoveDirection.WEST)
    }
}

data class HorizontalPipe(override val landscapeMap: String, override val coordinate: LandscapeCoordinate, override val direction: MoveDirection) : Field {
    override fun next(): Field {
        if (direction == MoveDirection.NORTH || direction == MoveDirection.SOUTH) throw Error("Horizontal Pipe has no next for specified direction")
        return move(direction)
    }

    override fun hasNext(): Boolean {
        return !(direction == MoveDirection.NORTH || direction == MoveDirection.SOUTH)
    }
}

data class CornerPipeL(override val landscapeMap: String, override val coordinate: LandscapeCoordinate, override val direction: MoveDirection) : Field {
    override fun next(): Field {
        val newDirection = when (direction) {
            MoveDirection.NORTH, MoveDirection.EAST -> throw Error("Corner Pipe L has no next for specified direction")
            MoveDirection.WEST -> MoveDirection.NORTH
            MoveDirection.SOUTH -> MoveDirection.EAST
        }

        return move(newDirection)
    }

    override fun hasNext(): Boolean {
        return !(direction == MoveDirection.NORTH || direction == MoveDirection.EAST)
    }
}

data class CornerPipeJ(override val landscapeMap: String, override val coordinate: LandscapeCoordinate, override val direction: MoveDirection) : Field {
    override fun next(): Field {
        val newDirection = when (direction) {
            MoveDirection.NORTH, MoveDirection.WEST -> throw Error("Corner Pipe J has no next for specified direction")
            MoveDirection.EAST -> MoveDirection.NORTH
            MoveDirection.SOUTH -> MoveDirection.WEST
        }

        return move(newDirection)
    }

    override fun hasNext(): Boolean {
        return !(direction == MoveDirection.NORTH || direction == MoveDirection.WEST)
    }
}

data class CornerPipe7(override val landscapeMap: String, override val coordinate: LandscapeCoordinate, override val direction: MoveDirection) : Field {
    override fun next(): Field {
        val newDirection = when (direction) {
            MoveDirection.SOUTH, MoveDirection.WEST -> throw Error("Corner Pipe 7 has no next for specified direction")
            MoveDirection.NORTH -> MoveDirection.WEST
            MoveDirection.EAST -> MoveDirection.SOUTH
        }

        return move(newDirection)
    }

    override fun hasNext(): Boolean {
        return !(direction == MoveDirection.SOUTH || direction == MoveDirection.WEST)
    }
}

data class CornerPipeF(override val landscapeMap: String, override val coordinate: LandscapeCoordinate, override val direction: MoveDirection) : Field {
    override fun next(): Field {
        val newDirection = when (direction) {
            MoveDirection.EAST, MoveDirection.SOUTH -> throw Error("Corner Pipe F has no next for specified direction")
            MoveDirection.NORTH -> MoveDirection.EAST
            MoveDirection.WEST -> MoveDirection.SOUTH
        }

        return move(newDirection)
    }

    override fun hasNext(): Boolean {
        return !(direction == MoveDirection.EAST || direction == MoveDirection.SOUTH)
    }
}

data class Ground(override val landscapeMap: String, override val coordinate: LandscapeCoordinate, override val direction: MoveDirection) : Field {
    override fun next(): Field {
        throw Error("Ground has no next for specified direction")
    }

    override fun hasNext(): Boolean {
        return false
    }
}

data class Start(override val landscapeMap: String, override val coordinate: LandscapeCoordinate, override val direction: MoveDirection) : Field {
    override fun next(): Field {
        return move(direction)
    }

    override fun hasNext(): Boolean {
        return true
    }
}
