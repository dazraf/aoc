package io.dazraf.aoc.y2020.day10

import io.dazraf.aoc.Puzzle
import io.dazraf.aoc.utilities.plus

fun main() = Puzzle(2020, 10, "Adapter Array").solve(Puzzle::part1, Puzzle::part2)

fun Puzzle.parse() = dataAsLongArray.sorted().let { sorted -> 0L + sorted + (sorted.last() + 3) }

fun Puzzle.part1(): Int {
  val parsed = parse()
  val ones = parsed.zipWithNext().count { (lhs, rhs) -> lhs + 1 == rhs }
  val threes = parsed.zipWithNext().count { (lhs, rhs) -> lhs + 3 == rhs }
  return ones * threes
}

fun Puzzle.part2(): Long {
  val parsed = parse()
  val cache = mutableMapOf<Long, Long>()
  val parsedSet = parsed.toSet()
  fun computePathsTo(value: Long): Long {
    return cache[value] ?: when {
      !parsedSet.contains(value) -> 0
      value == 0L -> 1
      else -> {
        computePathsTo(value - 1) + computePathsTo(value - 2) + computePathsTo(value - 3)
      }
    }.also {
      cache[value] = it
    }
  }
  return computePathsTo(parsed.last())
}
