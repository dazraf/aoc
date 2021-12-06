package io.dazraf.aoc.y2021.day06

import io.dazraf.aoc.Puzzle
import io.dazraf.aoc.utilities.toIntList

const val resetFishTimer = 6
const val newFishTimer = 8

fun main() = Puzzle(2021, 6, "Lanternfish").solve(Puzzle::part1, Puzzle::part2)

fun Puzzle.part1() = calculate(80)
fun Puzzle.part2() = calculate(256)

fun Puzzle.calculate(totalDays: Int) =
  generateSequence(0 to initialPopulation()) { (day, population) ->
    when (day) {
      totalDays -> null
      else -> (day + 1) to evolve(population)
    }
  }.last().second.sum()

// approach: we encode the population in an array, with the i'th element being the number of fish with i iterations left
// on their timer

fun Puzzle.initialPopulation() = Array(newFishTimer + 1) { 0L }.apply {
  dataList.first().toIntList().forEach { i -> ++this[i] }
}

fun evolve(population: Array<Long>) =
  (0..newFishTimer).map { daysLeft ->
    when (daysLeft) {
      // when timer runs out it resets back to [resetFishTimer]
      resetFishTimer -> population[daysLeft + 1] + population[0]
      // when timer runs out the fish reproduces a new one with timer value [newFishTimer]
      newFishTimer -> population[0]
      // everyone else merely ticks down
      else -> population[daysLeft + 1]
    }
  }.toTypedArray()