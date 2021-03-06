package io.dazraf.aoc.y2020.day07

import io.dazraf.aoc.Puzzle

fun main() = Puzzle(2020, 7, "Handy Haversacks").solve(Puzzle::part1, Puzzle::part2)

fun Puzzle.part1() = parseInput().pathCount("shiny gold")

fun Puzzle.part2() = parseInput().totalBagsInside("shiny gold")

private fun Puzzle.parseInput() = dataSequence.map { lines -> parseLine(lines) }.toMap()

private fun Map<String, List<BagCount>>.totalBagsInside(bag: String): Int =
  bagCounts(bag).sumOf { it.count + it.count * totalBagsInside(it.colour) }

private fun Map<String, List<BagCount>>.pathCount(target: String) = keys.count { hasPath(it, target) }

private fun Map<String, List<BagCount>>.hasPath(bag: String, target: String): Boolean = when {
  bagCounts(bag).any { it.colour == target } -> true
  else -> bagCounts(bag).any { hasPath(it.colour, target) }
}

private fun Map<String, List<BagCount>>.bagCounts(bag: String) = get(bag) ?: error("bag not found $bag")

private fun parseLine(line: String): Pair<String, List<BagCount>> {
  val (colour, rest) = line.replace(".", "").split(" bags contain ")
  return if (rest.endsWith("no other bags")) {
    colour to emptyList()
  } else {
    colour to rest.split(", ").map(BagCount.Companion::parse)
  }
}

data class BagCount(val colour: String, val count: Int) {
  companion object {
    private var bagCountRe = "(\\d+) ([\\s\\S]+?(?=\\sbag))".toRegex()

    fun parse(fragment: String): BagCount {
      val (_, count, containedBag) = bagCountRe.find(fragment)?.groupValues
        ?: error("did not match expected patter: $fragment")
      return BagCount(containedBag, count.toInt())
    }
  }
}
