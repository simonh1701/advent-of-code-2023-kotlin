fun main() {
    fun part1(input: List<String>): Int {
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
            // Filter out all games that are impossible with the given bag of cubes that only contains 14 blue ones, 13 green ones and 12 red ones
            .filter { game -> game.isValid }
            // Sum up the remaining game ids
            .fold(0) { acc, game -> acc + game.id }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInputPart1 = readInput("Day02_Test")
    check(part1(testInputPart1) == 8)

    val input = readInput("Day02")
    part1(input).println()
    // part2(input).println()
}

data class Game(val id: Int, val draws: List<Draw>) {
    val isValid: Boolean
        get() {
            return draws.all { draw -> draw.isValid }
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
}

data class Cubes(val amount: Int, val color: Color)

enum class Color {
    BLUE, GREEN, RED
}