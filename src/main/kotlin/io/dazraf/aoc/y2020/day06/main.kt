package io.dazraf.aoc.y2020.day06

import io.dazraf.aoc.Puzzle
import java.lang.System.lineSeparator

fun main() = Puzzle(2020, 6, "Custom Customs").solve(Puzzle::part1, Puzzle::part2)

fun Puzzle.part1() = dataAsBlocks.sumOf { block ->
  block.replace(lineSeparator(), "").toSet().count()
}

fun Puzzle.part2() = dataAsBlocks.sumOf { block ->
  block
    .split(lineSeparator()) // split by people in the group
    .filter { it.isNotEmpty() } // remove empty lines
    .map { it.toSet() } // break up each person's selection
    .reduce { acc, selected -> acc.intersect(selected) } // find the set intersections
    .count() // count the common set of answered questions by all in the group
}
