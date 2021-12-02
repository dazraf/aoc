package io.dazraf.aoc.y2020

import io.dazraf.aoc.*

object Day05 : Puzzle(2020, 5, "Binary Boarding") {
  @JvmStatic
  fun main(args: Array<String>) = Day05.solve()

  fun part1() = dataAsLines
    .map(::computeSeatId)
    .maxOrNull() ?: 0

  fun part2() = dataAsLines
    .map(::computeSeatId)
    .sorted()
    .zipWithNext() // pair-wise window
    .first { (first, second) -> first + 2 == second }.first + 1

  private fun computeSeatId(seatAddress: String): Int {
    val row = seatAddress.take(7).fold(0..127) { acc, char -> acc.applyOp(char) }.first
    val col = seatAddress.drop(7).fold(0..7) { acc, char -> acc.applyOp(char) }.first
    return row * 8 + col
  }

  private fun IntRange.applyOp(char: Char) = when (char) {
    'F', 'L' -> (first until (first + count() / 2))
    'B', 'R' -> ((first + count() / 2)..last)
    else -> error("unknown operator $char")
  }
}

