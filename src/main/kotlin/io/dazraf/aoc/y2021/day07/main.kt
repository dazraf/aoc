package io.dazraf.aoc.y2021.day07

import io.dazraf.aoc.Puzzle
import io.dazraf.aoc.utilities.boundingRange
import io.dazraf.aoc.utilities.toIntList
import kotlin.math.abs

fun main() = Puzzle(2021, 7, "The Treachery of Whales").solve(Puzzle::part1, Puzzle::part2)

fun Puzzle.part1() = compute(::part1FuelCost)
fun Puzzle.part2() = compute(::part2FuelCost)

private fun Puzzle.compute(fuelCalculator: (Int) -> Int): Int {
  val crabs = dataList.first().toIntList()
  return crabs.boundingRange().map { candidatePosition ->
    crabs.sumOf { fuelCalculator(abs(it - candidatePosition)) }
  }.minOrNull() ?: 0
}

fun part1FuelCost(delta: Int) = delta
fun part2FuelCost(delta: Int) = (delta * (delta + 1)) / 2 // simple nth partial sum of series