package io.dazraf.aoc.y2020.day15

import io.dazraf.aoc.Puzzle

fun main() = Puzzle(2020, 15, "Rambunctious Recitation").solve(Puzzle::part1, Puzzle::part2)

fun Puzzle.part1() = calculate(2020)

fun Puzzle.part2() = calculate(30_000_000)

fun Puzzle.calculate(nthNumber: Int): Int {
  val startingNumbers = dataSequence.first().split(",").map { it.toInt() }
  val memory = startingNumbers
    .dropLast(1)
    .mapIndexed { index, i -> i to (index + 1) }
    .toMap().toMutableMap()

  var lastNumber = startingNumbers.last()
  for (turn in (startingNumbers.size + 1)..nthNumber) {
    val next = when (val last = memory[lastNumber]) {
      null -> 0
      else -> (turn - 1) - last
    }
    memory[lastNumber] = turn - 1
    lastNumber = next
  }
  return lastNumber
}
