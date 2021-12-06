package io.dazraf.aoc.y2021.day01

import io.dazraf.aoc.Puzzle

fun main() = Puzzle(2021, 1, "Sonar Sweep").solve(Puzzle::part1, Puzzle::part2)

fun Puzzle.part1() = parse().countDepthIncreases(1)
fun Puzzle.part2() = parse().countDepthIncreases(3)

fun Puzzle.parse() = dataIntSequence

private fun Sequence<Int>.countDepthIncreases(windowSize: Int) =
  windowed(windowSize)
    .zipWithNext()
    .count { (window1, window2) ->
      window2.last() > window1.first()
    }
