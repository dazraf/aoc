package io.dazraf.aoc.y2021.day07

import io.dazraf.aoc.Puzzle
import io.dazraf.aoc.utilities.boundingRange
import io.dazraf.aoc.utilities.toIntList
import kotlin.math.abs

fun main() = Puzzle(2021, 7, "The Treachery of Whales").solve(Puzzle::part1, Puzzle::part2)

fun Puzzle.part1() = compute(::part1FuelCost)
fun Puzzle.part2() = compute(::part2FuelCost)

fun Puzzle.compute(fuelCalculator: (Int) -> Int) = readCrabs().let { crabs ->
    crabs.boundingRange().minOf { position -> crabs.sumOf { crab -> fuelCalculator(abs(crab - position)) } }
  }

fun part1FuelCost(delta: Int) = delta
fun part2FuelCost(delta: Int) = (delta * (delta + 1)) / 2 // simple nth partial sum of series

fun Puzzle.readCrabs() = data.trim().toIntList()
