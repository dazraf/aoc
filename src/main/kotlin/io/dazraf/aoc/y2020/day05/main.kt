package io.dazraf.aoc.y2020.day05

import io.dazraf.aoc.Puzzle

fun main() = Puzzle(2020, 5, "Binary Boarding").solve(Puzzle::part1, Puzzle::part2)

fun Puzzle.part1() = dataSequence
  .map(::computeSeatId)
  .maxOrNull() ?: 0

fun Puzzle.part2() = dataSequence
  .map(::computeSeatId)
  .sorted()
  .zipWithNext() // pair-wise window
  .first { (first, second) -> first + 2 == second }.first + 1

fun computeSeatId(seatAddress: String): Int {
  val row = seatAddress.take(7).fold(0..127) { acc, char -> acc.applyOp(char) }.first
  val col = seatAddress.drop(7).fold(0..7) { acc, char -> acc.applyOp(char) }.first
  return row * 8 + col
}

fun IntRange.applyOp(char: Char) = when (char) {
  'F', 'L' -> (first until (first + count() / 2))
  'B', 'R' -> ((first + count() / 2)..last)
  else -> error("unknown operator $char")
}

