package io.dazraf.aoc.y2020

import io.dazraf.aoc.*

object Day15 : Puzzle(2020, 15, "Rambunctious Recitation") {
  @JvmStatic
  fun main(args: Array<String>) = Day15.solve()

  fun part1() = calculate(2020)

  fun part2() = calculate(30_000_000)

  private fun calculate(nthNumber: Int): Int {
    val startingNumbers = dataAsLines.first().split(",").map { it.toInt() }
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
}