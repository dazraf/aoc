package io.dazraf.aoc.y2021.day06

import io.dazraf.aoc.Puzzle
import io.dazraf.aoc.utilities.toIntList

const val resetFishTimer = 6
const val newFishTimer = 8

fun main() = Puzzle(2021, 6, "Lanternfish").solve(Puzzle::part1, Puzzle::part2)

fun Puzzle.part1() = calculate(80)
fun Puzzle.part2() = calculate(256)

fun Puzzle.calculate(totalDays: Int) =
  generateSequence(State(initialPopulation(), totalDays), State::evolveOrNull).last().totalPopulation

fun Puzzle.initialPopulation() =
  dataList.first().toIntList().groupBy { it }.map { (days, fish) -> days to fish.size.toLong() }.toMap()

class State(private val population: Map<Int, Long>, val daysRemaining: Int) {
  operator fun get(day: Int) = population[day] ?: 0
  val totalPopulation by lazy { population.values.sum() }
}

fun State.evolveOrNull() = if (daysRemaining == 0) null else evolve()

private fun State.evolve() = State(
  (0..newFishTimer).associateWith { daysLeft ->
    when (daysLeft) {
      resetFishTimer -> this[daysLeft + 1] + this[0]
      newFishTimer -> this[0]
      else -> this[daysLeft + 1]
    }
  }, daysRemaining - 1
)

