package io.dazraf.aoc.y2020

import io.dazraf.aoc.*
import io.dazraf.aoc.utilities.*

object Day04 : Puzzle(2020, 4, "Passport Processing") {
  private val requiredFields = "ecl iyr hgt eyr pid byr hcl".split(' ')

  @JvmStatic
  fun main(args: Array<String>) = Day04.solve()

  fun part1(): Int {
    return processData(listOf { keys.containsAll(requiredFields) })
  }

  fun part2(): Int {
    return processData(listOf(
      { keys.containsAll(requiredFields) },
      { intField("byr").`in`(1920..2002) },
      { intField("iyr").`in`(2010..2020) },
      { intField("eyr").`in`(2020..2030) },
      {
        val hgt = field("hgt")
        when {
          hgt.contains("cm") -> intField("hgt", "cm").`in`(150..193)
          hgt.contains("in") -> intField("hgt", "in").`in`(59..76)
          else -> false
        }
      },
      { field("hcl").matches("#[0-9a-f]{6}") },
      { field("ecl").`in`(listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")) },
      { field("pid").matches("\\d{9}") }
    ))
  }

  private fun processData(conditions: List<Map<String, String>.() -> Boolean>) =
    dataAsGroupedFields.count { passport -> conditions.all { condition -> passport.condition() } }
}