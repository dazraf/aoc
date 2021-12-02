package io.dazraf.aoc.y2021

import io.dazraf.aoc.*

object Day01 : Puzzle(2021, 1, "Sonar Sweep") {
  @JvmStatic
  fun main(args: Array<String>) = solve()

  fun part1() = dataAsIntList.countDepthIncreases()
  fun part2() = dataAsIntList.toTypedArray().let { data ->
    (0 until data.size - 2).map { i -> data.slice(i..(i + 2)).sum() }.countDepthIncreases()
  }

  private fun List<Int>.countDepthIncreases() = zipWithNext().map { (lhs, rhs) -> rhs - lhs }.count { it > 0 }
}