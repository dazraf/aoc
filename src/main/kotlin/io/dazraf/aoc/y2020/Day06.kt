package io.dazraf.aoc.y2020

import io.dazraf.aoc.Puzzle
import io.dazraf.aoc.dataAsBlocks
import io.dazraf.aoc.solve
import java.lang.System.lineSeparator

object Day06 : Puzzle(2020, 6, "Custom Customs") {
  @JvmStatic
  fun main(args: Array<String>) = Day06.solve()

  fun part1() = dataAsBlocks.sumOf { block ->
    block.replace(lineSeparator(), "").toSet().count()
  }

  fun part2() = dataAsBlocks.sumOf { block ->
    block
      .split(lineSeparator()) // split by people in the group
      .filter { it.isNotEmpty() } // remove empty lines
      .map { it.toSet() } // break up each person's selection
      .reduce { acc, selected -> acc.intersect(selected) } // find the set intersections
      .count() // count the common set of answered questions by all in the group
  }
}