fun main() {
    fun part1(input: List<String>): Int {
        val games = turnInputTextIntoGameObjects(input)

        return games
            // Filter out all games that are impossible with the given bag of cubes that only contains 14 blue ones, 13 green ones and 12 red ones
            .filter { game -> game.isValid }
            // Sum up the remaining game ids
            .fold(0) { acc, game -> acc + game.id }
    }

    fun part2(input: List<String>): Int {
        val games = turnInputTextIntoGameObjects(input)

        return games
            .map { game ->
                val minimumOfCubesPerColor = game.minimumOfCubesPerColor
                val minimumSetOfCubes = minimumOfCubesPerColor.map { minimumOfCubes ->
                    Cubes(
                        amount = minimumOfCubes.value,
                        color = minimumOfCubes.key
                    )
                }.toSet()
                Draw(minimumSetOfCubes).power
            }
            .fold(0) { acc, power -> acc + power }
    }

    val testInput = readInput("Day02_Test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

fun turnInputTextIntoGameObjects(input: List<String>): List<Game> {
    // Turn the input into a list of game objects
    val games = input.map { line ->
        // Get the ID of the game
        val gameId: Int = line.split(": ", ignoreCase = true, limit = 2)[0].substringAfterLast(' ').toInt()

        // Turn the game into a list of Draws where each draw is a set of Cubes
        val gameString = line.split(": ", ignoreCase = true, limit = 2)[1]
        val cubesPerDraw = gameString
            .split("; ")
            .map { draw ->
                draw.split(", ")
                    .map { pick ->
                        val amount = pick.substringBefore(" ").toInt()
                        val color: Color = when (pick.substringAfter(" ")) {
                            "blue" -> Color.BLUE
                            "green" -> Color.GREEN
                            "red" -> Color.RED
                            else -> {
                                throw Exception("Found unexpected color")
                            }
                        }

                        Cubes(amount, color)
                    }
                    .toSet()
            }

        val draws = cubesPerDraw.map { cubes -> Draw(cubes) }

        Game(gameId, draws)
    }

    return games
}

data class Game(val id: Int, val draws: List<Draw>) {
    val isValid: Boolean
        get() {
            return draws.all { draw -> draw.isValid }
        }

    val minimumOfCubesPerColor: Map<Color, Int>
        get() {
            fun maxAmountOfCubesInDrawFor(color: Color): Int = draws.maxOfOrNull { draw ->
                draw.setOfCubes.filter { cubes -> cubes.color == color }.fold(0) { acc, cubes -> acc + cubes.amount }
            } ?: 0

            return mapOf(
                Color.BLUE to maxAmountOfCubesInDrawFor(color = Color.BLUE),
                Color.GREEN to maxAmountOfCubesInDrawFor(color = Color.GREEN),
                Color.RED to maxAmountOfCubesInDrawFor(color = Color.RED)
            )
        }
}

data class Draw(val setOfCubes: Set<Cubes>) {
    val isValid: Boolean
        get() {
            return setOfCubes.all { cubes ->
                val limit = when (cubes.color) {
                    Color.BLUE -> 14
                    Color.GREEN -> 13
                    Color.RED -> 12
                }

                cubes.amount <= limit
            }
        }

    val power: Int
        get() {
            return setOfCubes.fold(1) { acc, cubes -> acc * cubes.amount }
        }
}

data class Cubes(val amount: Int, val color: Color)

enum class Color {
    BLUE, GREEN, RED
}