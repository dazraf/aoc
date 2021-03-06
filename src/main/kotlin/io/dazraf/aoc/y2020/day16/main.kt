package io.dazraf.aoc.y2020.day16

import io.dazraf.aoc.Puzzle
import io.dazraf.aoc.utilities.multiply
import io.dazraf.aoc.utilities.toLongList

fun main() = Puzzle(2020, 16).solve(Puzzle::part1, Puzzle::part2)

data class PuzzleData(
  val constraints: List<Constraint>,
  val ticket: List<Long>,
  val nearbyTickets: List<List<Long>>,
  val anyConstraint: Constraint = constraints.any()
)

fun Puzzle.part1() = puzzleData().run { part1() }

fun Puzzle.part2() = puzzleData().run { part2() }

fun PuzzleData.part1() = nearbyTickets
  .flatten()
  .filter { number -> !anyConstraint.sparseRange.contains(number) }
  .sum()

fun PuzzleData.part2(): Long {
  val validNearbyTickets = nearbyTickets.filter { ticket ->
    ticket.none { number -> !anyConstraint.sparseRange.contains(number) }
  }
  val dataColumns = (constraints.indices).map { index -> validNearbyTickets.map { it[index] } }
  // the following is a list of pairs
  // field name -> list of potential columns
  val fieldToPotentialColumnIndices = constraints.map { constraint ->
    constraint.name to dataColumns
      .mapIndexed { index, column -> index to column }
      .filter { (_, column) -> column.matches(constraint) }
      .map { (index, _) -> index }
  }
  // iterate until we've resolved all fields
  return resolveAllFields(fieldToPotentialColumnIndices)
    // filter only the departure fields
    .filter { (name, _) -> name.startsWith("departure") }
    // get their values from your ticket
    .map { (_, index) -> ticket[index] }
    // multiply and we're done
    .multiply()
}

fun resolveAllFields(fieldToPotentialColumnIndices: List<Pair<String, List<Int>>>) =
  generateSequence(fieldToPotentialColumnIndices) { potentialFields ->
    when {
      potentialFields.allResolved() -> null
      else -> removeResolvedColumnIndices(potentialFields)
    }
  }
    .last()
    .map { (name, indices) -> name to indices.single() }

/**
 * @param potentialFields - a list of field name to potential column indices list
 * @returns the list of field name to potential column indices with all resolved fields removed from the alternatives for other fields
 */
fun removeResolvedColumnIndices(potentialFields: List<Pair<String, List<Int>>>): List<Pair<String, List<Int>>> {
  val resolved = potentialFields.map { (_, indices) -> indices }.filter { indices -> indices.resolved() }.flatten()
  return potentialFields.map { (name, indices) ->
    when {
      indices.resolved() -> name to indices
      else -> name to indices.toMutableList().apply { removeAll(resolved) }.toList()
    }
  }
}

/**
 * Returns true iff all fields have exactly one column index
 */
fun List<Pair<String, List<Int>>>.allResolved() = all { (_, indices) -> indices.resolved() }

/**
 * Takes a list of potential column indices and returns true iff there is only one column index.
 */
fun List<Int>.resolved() = this.size == 1

fun List<Long>.matches(constraint: Constraint) = all { constraint.sparseRange.contains(it) }

fun Puzzle.puzzleData() = dataSequence.iterator().let { data ->
  PuzzleData(readConstraints(data), readTicket(data), readNearbyTickets(data))
}

fun readTicket(data: Iterator<String>): List<Long> {
  data.next() // 'your ticket:'
  val result = data.next().toLongList()
  data.next() // blank line
  return result
}

fun readNearbyTickets(data: Iterator<String>) = sequence {
  data.next() // 'nearby tickets:'
  while (data.hasNext()) yield(data.next().toLongList())
}.toList()

data class Constraint(val name: String, val sparseRange: ClosedRange<Long>)

fun readConstraints(data: Iterator<String>) = generateSequence {
  val nextLine = data.next()
  when {
    nextLine.isBlank() -> null
    else -> {
      val (name, rangeString) = nextLine.split(": ")
      val range = rangeString.toSparseClosedRange()
      Constraint(name, range)
    }
  }
}.toList()

fun List<Constraint>.any() = reduce { acc, range -> Constraint("", acc.sparseRange or range.sparseRange) }
fun String.toSparseClosedRange() = split(" or ")
  .map { it.split('-').map(String::trim).map(String::toLong) }
  .map { (from, to) -> LongRange(from, to) }
  .reduce { acc: ClosedRange<Long>, rhs: ClosedRange<Long> -> acc or rhs }

/**
 * Let's create sparse closed ranges - this is a slight bastardisation of [ClosedRange]
 * Will beg for forgiveness later
 */
infix fun <C : Comparable<C>, T : ClosedRange<C>> T.or(rhs: T): ClosedRange<C> {
  return object : ClosedRange<C> {
    override val start get() = minOf(this@or.start, rhs.start)
    override val endInclusive get() = maxOf(this@or.endInclusive, rhs.endInclusive)
    override fun contains(value: C) = this@or.contains(value) || rhs.contains(value)
    override fun isEmpty(): Boolean = this@or.isEmpty() && rhs.isEmpty()
  }
}


