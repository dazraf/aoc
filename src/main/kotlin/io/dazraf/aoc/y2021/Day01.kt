package io.dazraf.aoc.y2021

import io.dazraf.aoc.*

object Day01 : Puzzle(2021, 1, "Sonar Sweep") {
  @JvmStatic
  fun main(args: Array<String>) = solve()

  fun part1() = dataAsIntSeq.countDepthIncreases(1)
  fun part2() = dataAsIntSeq.countDepthIncreases(3)

  private fun Sequence<Int>.countDepthIncreases(windowSize: Int) =
    windowed(windowSize)
      .zipWithNext()
      .count { (window1, window2) ->
        window2.last() > window1.first()
      }
}