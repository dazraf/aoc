package io.dazraf.aoc.y2020.day01

import io.dazraf.aoc.*
import io.dazraf.aoc.utilities.combinations
import io.dazraf.aoc.utilities.multiply

fun main() = Puzzle(year = 2020, day = 1, title = "Report Repair").solve(Puzzle::part1, Puzzle::part2)

fun Puzzle.part1() = process(2)

fun Puzzle.part2() = process(3)

fun Puzzle.process(size: Int): Int = dataIntList.combinations(size).first { it.sum() == 2020 }.multiply()



