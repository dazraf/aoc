package io.dazraf.aoc.y2020

import io.dazraf.aoc.*
import io.dazraf.aoc.utilities.combinations
import io.dazraf.aoc.utilities.multiply

object Day01 : Puzzle(year = 2020, day = 1, title = "Report Repair") {

  @JvmStatic
  fun main(args: Array<String>) = Day01.solve()

  fun part1() = process(2)

  fun part2() = process(3)

  private fun Puzzle.process(size: Int): Int = dataAsIntList.combinations(size).first { it.sum() == 2020 }.multiply()
}



