fun main() {
    val cardsToOrder: Map<Char, Int> = mapOf(
        'A' to 14,
        'K' to 13,
        'Q' to 12,
        'J' to 11,
        'T' to 10,
        '9' to 9,
        '8' to 8,
        '7' to 7,
        '6' to 6,
        '5' to 5,
        '4' to 4,
        '3' to 3,
        '2' to 2
    )

    val cardsToOrderForPart2: Map<Char, Int> = cardsToOrder.plus('J' to 1)

    fun part1(input: List<String>): Int {
        val hands = input.map { line ->
            val lineSplit = line.split(" ")
            val handString = lineSplit[0]
            val bet = lineSplit[1].toInt()
            Hand(handString, bet, cardsToOrder)
        }

        return hands.sorted().foldIndexed(0) { index, acc, hand ->
            acc + (hand.bet * (index + 1))
        }
    }

    fun part2(input: List<String>): Int {
        val hands = input.map { line ->
            val lineSplit = line.split(" ")
            val handString = lineSplit[0]
            val bet = lineSplit[1].toInt()
            HandForPart2(handString, bet, cardsToOrderForPart2)
        }

        return hands.sorted().foldIndexed(0) { index, acc, hand ->
            acc + (hand.bet * (index + 1))
        }
    }

    val testInput = readInput("Day07_Test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

abstract class BaseHand() : Comparable<BaseHand> {
    abstract val handString: String
    abstract val bet: Int
    abstract val cardsToOrder: Map<Char, Int>
    abstract val charList: List<Char>
    lateinit var type: HandType

    override fun compareTo(other: BaseHand): Int {
        val differenceInStrength = this.type.strength - other.type.strength
        if (differenceInStrength != 0) return differenceInStrength

        val charOrderList = this.charList.mapNotNull { cardsToOrder[it] }
        val otherCharOrderList = other.charList.mapNotNull { cardsToOrder[it] }
        check(charOrderList.size == otherCharOrderList.size)
        val differencesOfCardOrdersAtEachPosition =
            charOrderList.zip(otherCharOrderList).map { pair -> pair.first - pair.second }

        return differencesOfCardOrdersAtEachPosition.firstOrNull { it != 0 } ?: 0
    }
}

data class Hand(override val handString: String, override val bet: Int, override val cardsToOrder: Map<Char, Int>) :
    BaseHand() {
    override val charList: List<Char> = this.handString.toCharArray().toList()

    init {
        check(charList.size == 5)

        val mapOfCardToCardCount: Map<Char, Int> = charList.groupBy { it }.mapValues { entry -> entry.value.count() }

        val cardToCardCountSorted = mapOfCardToCardCount.entries.sortedByDescending { it.value }

        type = when (cardToCardCountSorted[0].value) {
            5 -> HandType.FIVE_OF_A_KIND
            4 -> HandType.FOUR_OF_A_KIND
            3 -> if (cardToCardCountSorted[1].value == 2) HandType.FULL_HOUSE else HandType.THREE_OF_A_KIND
            2 -> if (cardToCardCountSorted[1].value == 2) HandType.TWO_PAIR else HandType.ONE_PAIR
            else -> HandType.HIGH_CARD
        }
    }
}

data class HandForPart2(
    override val handString: String, override val bet: Int, override val cardsToOrder: Map<Char, Int>
) : BaseHand() {
    override val charList = handString.toCharArray().toList()

    init {
        check(charList.size == 5)

        val mapOfCardToCardCount: MutableMap<Char, Int> =
            charList.groupBy { it }.mapValues { entry -> entry.value.count() }.toMutableMap()

        val numberOfJays = mapOfCardToCardCount.getOrDefault('J', 0)
        mapOfCardToCardCount.remove('J')
        val cardToCardCountSorted =
            mapOfCardToCardCount.entries.sortedByDescending { it.value }.map { mutableEntry -> mutableEntry.value }

        type = when (cardToCardCountSorted.getOrElse(0) { 0 } + numberOfJays) {
            5 -> HandType.FIVE_OF_A_KIND
            4 -> HandType.FOUR_OF_A_KIND
            3 -> if (cardToCardCountSorted[1] == 2) HandType.FULL_HOUSE else HandType.THREE_OF_A_KIND
            2 -> if (cardToCardCountSorted[1] == 2) HandType.TWO_PAIR else HandType.ONE_PAIR
            else -> HandType.HIGH_CARD
        }
    }
}

enum class HandType(val strength: Int) {
    HIGH_CARD(0), ONE_PAIR(1), TWO_PAIR(2), THREE_OF_A_KIND(3), FULL_HOUSE(4), FOUR_OF_A_KIND(5), FIVE_OF_A_KIND(6)
}