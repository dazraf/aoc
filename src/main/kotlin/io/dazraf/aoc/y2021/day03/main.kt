package io.dazraf.aoc.y2021.day03

import io.dazraf.aoc.*
import io.dazraf.aoc.utilities.parseBinaryInt

fun main() = Puzzle(2021, 3, "Binary Diagnostic").solve(Puzzle::part1, Puzzle::part2)

fun Puzzle.part1(): Int {
  val (values, width, mask) = parse()
  val gamma = setBits(width) { pos -> mostCommonBitCriteria(values, pos) }
  val epsilon = (gamma.inv()) and mask
  return epsilon * gamma
}

fun Puzzle.part2(): Int {
  val (values, width) = parse()
  val o2generatorRating = computeCriteria(values, width, ::mostCommonBitCriteria)
  val co2scrubberRating = computeCriteria(values, width, ::leastCommonBitCriteria)
  return o2generatorRating * co2scrubberRating
}

data class PuzzleData(val values: List<Int>, val width: Int, val mask: Int = setBits(width) { 1 })

fun Puzzle.parse(): PuzzleData {
  val data = dataList
  val values = data.map(::parseBinaryInt)
  val width = data.first().length
  return PuzzleData(values, width)
}

/**
 * Iterate from the MSB to LSB, incrementally filtering [values] according to the [bitCriteria]
 */
fun computeCriteria(values: List<Int>, width: Int, bitCriteria: (List<Int>, Int) -> Int) =
  generateSequence((width - 1) to values) { (pos, values) ->
    when {
      values.size <= 1 -> null
      pos < 0 -> null
      else -> {
        val requiredBit = bitCriteria(values, pos)
        (pos - 1) to values.filter { it.nthBit(pos) == requiredBit }
      }
    }
  }.last().second.single()

/**
 * Set bits with values generated from [fn] which maps a bit position to a 0 or 1
 */
fun setBits(width: Int, fn: (Int) -> Int) = (0 until width).fold(0) { acc, pos -> acc.setNthBit(pos, fn(pos)) }
fun mostCommonBitCriteria(nums: List<Int>, pos: Int) = if (countBitN(nums, pos) * 2 >= nums.size) 1 else 0
fun leastCommonBitCriteria(nums: List<Int>, pos: Int) = if (countBitN(nums, pos) * 2 < nums.size) 1 else 0
fun countBitN(nums: List<Int>, pos: Int) = nums.count { it.nthBit(pos) > 0 }
fun Int.setNthBit(pos: Int, value: Int) = this or (value shl pos)
fun Int.nthBit(pos: Int) = shr(pos) and 1
