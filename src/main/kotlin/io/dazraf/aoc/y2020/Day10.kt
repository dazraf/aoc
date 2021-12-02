package io.dazraf.aoc.y2020

import io.dazraf.aoc.Puzzle
import io.dazraf.aoc.dataAsLongArray
import io.dazraf.aoc.solve
import io.dazraf.aoc.utilities.plus

object Day10 : Puzzle(2020, 10, "Adapter Array") {
  @JvmStatic
  fun main(args: Array<String>) = Day10.solve()

  private val data by lazy { dataAsLongArray.sorted().let { sorted -> 0L + sorted + (sorted.last() + 3) } }
  private val destination by lazy { data.last() }
  private val dataSet by lazy { data.toSet() }

  fun part1(): Int {
    val ones = data.zipWithNext().count { (lhs, rhs) -> lhs + 1 == rhs }
    val threes = data.zipWithNext().count { (lhs, rhs) -> lhs + 3 == rhs }
    return ones * threes
  }

  fun part2(): Long {
    val cache = mutableMapOf<Long, Long>()
    fun computePathsTo(value: Long): Long {
      return cache[value] ?: when {
        !dataSet.contains(value) -> 0
        value == 0L -> 1
        else -> {
          computePathsTo(value - 1) + computePathsTo(value - 2) + computePathsTo(value - 3)
        }
      }.also {
        cache[value] = it
      }
    }
    return computePathsTo(destination)
  }
}
