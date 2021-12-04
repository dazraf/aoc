package io.dazraf.aoc.y2021

import io.dazraf.aoc.*
import io.dazraf.aoc.utilities.parseBinaryInt

object Day03 : Puzzle(2021, 3, "Binary Diagnostic") {
  @JvmStatic
  fun main(args: Array<String>) = solve()

  fun part1(): Int {
    val data = dataAsLinesList
    val intList = data.map(::parseBinaryInt)
    val width = data.first().length
    val gamma = setBits(width) { position -> calcValueOfBit(intList, position) }
    val mask = setBits(width) { 1 }
    val epsilon = (gamma.inv()) and mask
    return epsilon * gamma
  }

  private fun setBits(width: Int, fn: (Int) -> Int) = (0 until width).fold(0) { acc, idx -> acc or (fn(idx) shl idx) }
  private fun calcValueOfBit(nums: List<Int>, idx: Int) = if (countBitN(nums, idx) >= nums.size / 2) 1 else 0
  private fun countBitN(nums: List<Int>, idx: Int) = nums.count { nthBit(it, idx) > 0 }
  private fun nthBit(num: Int, idx: Int) = num.shr(idx) and 1
}