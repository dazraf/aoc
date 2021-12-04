package io.dazraf.aoc.y2020.day13

import io.dazraf.aoc.Puzzle
import kotlin.math.ceil

fun main() = Puzzle(2020, 13, "Shuttle Search").solve(Puzzle::part1, Puzzle::part2)

fun Puzzle.part1(): Int {
  val (line1, line2) = dataSequence.toList()
  val time = line1.toDouble()
  val (bus, delta) = line2.split(",").filter { it != "x" }.map { it.toInt() }
    .map { bus -> bus to (ceil(time / bus) * bus - time) }
    .reduce { (bus, delta), (bus2, delta2) ->
      if (delta < delta2)
        bus to delta
      else
        bus2 to delta2
    }
  return bus * delta.toInt()
}

fun Puzzle.part2(): Long {
  val (_, line) = dataSequence.toList()
  val data = line.split(",")
  val indexAndBusIds = data
    .mapIndexed { index, s -> index to s } // pair up index and bus id
    .filter { (_, s) -> s != "x" } // eliminate 'x's
    .map { (i, s) -> i.toLong() to s.toLong() } // parse to longs
  // calculate expected remainders
  val remainders = indexAndBusIds.map { (index, busId) -> busId - index }.toLongArray()
  // and the discrete bus Ids
  val busIds = indexAndBusIds.map { (_, busId) -> busId }.toLongArray()
  // CRT to the rescue!
  return chineseRemainder(busIds, remainders)
}

/*
 * I had to look up the following section regarding finding an X that generates a set of remainders for a set of
 * co-primes.
 *
 * Chinese Remainder Theorem to the rescue:
 * https://en.wikipedia.org/wiki/Chinese_remainder_theorem
 *
 * There is a basic implementation in Kotlin on Rosetta Stone which I modified to use better Kotlin idioms and to
 * support Longs:
 * https://rosettacode.org/wiki/Chinese_remainder_theorem#Kotlin
 */

/**
 * This is the inverse mod - it returns a result for a [number] and a [modulus] such that
 * ([number] * result) % [modulus] == 1
 */
private fun inverseMultiplyMod(number: Long, modulus: Long): Long {
  if (modulus == 1L) return 1L
  var aa = number
  var bb = modulus
  var x0 = 0L
  var x1 = 1L
  while (aa > 1) {
    val q = aa / bb
    var t = bb
    bb = aa % bb
    aa = t
    t = x0
    x0 = x1 - q * x0
    x1 = t
  }
  if (x1 < 0) x1 += modulus
  return x1
}

private fun chineseRemainder(coPrimes: LongArray, remainders: LongArray): Long {
  val prod = coPrimes.reduce { acc, i -> acc * i }
  return coPrimes.indices.sumOf { index ->
    val p = prod / coPrimes[index]
    (remainders[index] * inverseMultiplyMod(p, coPrimes[index]) * p)
  } % prod
}



