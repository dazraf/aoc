package io.dazraf.aoc.y2020.day02

import io.dazraf.aoc.Puzzle

fun main() = Puzzle(year = 2020, day = 2, title = "Password Philosophy").solve(Puzzle::part1, Puzzle::part2)

fun Puzzle.part1(): Int = parse().count { it.isValidForPart1 }

fun Puzzle.part2(): Int = parse().count { it.isValidForPart2 }

fun Puzzle.parse() = dataList.map { PolicyAndPassword.parse(it) }

val PolicyAndPassword.isValidForPart1: Boolean
  get() {
    val count = password.count { it == policy.letter }
    return count >= policy.min && count <= policy.max
  }

val PolicyAndPassword.isValidForPart2: Boolean
  get() {
    val i1 = policy.min - 1
    val i2 = policy.max - 1
    return when {
      i1 < 0 || i1 >= password.length || i2 < 0 || i2 >= password.length -> false
      password[i1] == policy.letter && password[i2] != policy.letter -> true
      password[i1] != policy.letter && password[i2] == policy.letter -> true
      else -> false
    }
  }

data class PolicyAndPassword(val policy: Policy, val password: String) {
  companion object {
    fun parse(txt: String): PolicyAndPassword {
      val (lhs, password) = txt.split(':').map { it.trim() }.filter { it.isNotEmpty() }
      return PolicyAndPassword(Policy.parse(lhs), password)
    }
  }
}

data class Policy(val min: Int, val max: Int, val letter: Char) {
  companion object {
    fun parse(txt: String): Policy {
      val (lhs, rhs) = txt.split(' ').map { it.trim() }.filter { it.isNotEmpty() }
      val (min, max) = parseMinMax(lhs)
      val letter = rhs.first()
      return Policy(min, max, letter)
    }

    private fun parseMinMax(txt: String): Pair<Int, Int> {
      val (min, max) = txt.split('-').map { it.trim().toInt() }
      return min to max
    }
  }
}


