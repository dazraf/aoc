package io.dazraf.aoc.y2020

import io.dazraf.aoc.*
import io.dazraf.aoc.utilities.combinations

object Day09 : Puzzle(2020, 9, "Encoding Error") {
  private const val PREAMBLE_SIZE = 25

  @JvmStatic
  fun main(args: Array<String>) = Day09.solve()

  fun part1() = dataAsLongArray.findInvalidItem(PREAMBLE_SIZE)

  fun part2(): Long {
    val invalidNumber = part1()
    val rangeSumsToInvalidNumber = sequence {
      for (start in (dataAsLongArray.indices)) {
        for (end in (start until dataAsLongArray.size)) {
          yield(start..end)
        }
      }
    }.first { range -> dataAsLongArray.slice(range).sum() == invalidNumber }
    // return sum of min and max in range
    return dataAsLongArray.slice(rangeSumsToInvalidNumber).let { (it.minOrNull() ?: 0) + (it.maxOrNull() ?: 0) }
  }

  private fun Array<Long>.findInvalidItem(preambleSize: Int) =
    (preambleSize until size).asSequence() // the data elements
      .map { index -> index to this[index] } // {index, item}
      .filter { (index, value) ->
        slice((index - preambleSize until index))
          .combinations(2).none { (lhs, rhs) -> lhs + rhs == value }
      }
      .map { (_, value) -> value }
      .single()
}