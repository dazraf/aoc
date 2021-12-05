package io.dazraf.aoc.y2021.day04

import io.dazraf.aoc.Puzzle
import io.dazraf.aoc.utilities.toIntList

fun main() = Puzzle(2021, 4, "Giant Squid").solve(Puzzle::part1, Puzzle::part2)

fun Puzzle.part1() = run(firstWinnerStrategy)

fun Puzzle.part2() = run(lastWinnerStrategy)

val firstWinnerStrategy = { boards: List<Board> -> boards }

val lastWinnerStrategy = { boards: List<Board> ->
  when (boards.size) {
    1 -> boards
    else -> boards.filter { !it.isWinningBoard }
  }
}

private fun Puzzle.run(strategy: (List<Board>) -> List<Board> = { it }): Int {
  val (randomNumbers, boards) = parse()
  return generateSequence(boards) { currentBoards ->
    when {
      !randomNumbers.hasNext() -> null
      currentBoards.hasWinner() -> null
      else -> strategy(currentBoards.mark(randomNumbers.next()))
    }
  }.last().single { it.isWinningBoard }.score
}

fun Puzzle.parse() = readRandomNumbers() to readBoards()

data class Position(val row: Int = 0, val col: Int = 0)

data class Board(val numbers: Map<Int, Position>, val lastNumber: Int = -1, val marked: Set<Position> = emptySet()) {
  private val rows = 0..numbers.values.maxOf { it.row }
  private val cols = 0..numbers.values.maxOf { it.col }

  val isWinningBoard by lazy {
    (rows.asSequence().map { isWinningRow(it) } + cols.asSequence().map { isWinningCol(it) }).any { it }
  }

  val score by lazy { numbers.filterValues { !isMarked(it) }.keys.sum() * lastNumber }

  private fun isWinningRow(row: Int) = cols.map { Position(row, it) }.all { isMarked(it) }

  private fun isWinningCol(col: Int) = rows.map { Position(it, col) }.all { isMarked(it) }

  private fun isMarked(pos: Position) = marked.contains(pos)

  override fun toString(): String = numbers.filterValues { marked.contains(it) }.keys.toString()
}

fun List<Board>.mark(number: Int) = map { it.mark(number) }

fun Board.mark(number: Int): Board = when (val position = numbers[number]) {
  null -> this
  else -> copy(marked = marked + position, lastNumber = number)
}

fun List<Board>.hasWinner() = map { it.isWinningBoard }.any { it }

fun Puzzle.readBoards() = dataAsBlocks.drop(1).map { readBoard(it) }

fun Puzzle.readRandomNumbers() = dataAsBlocks.first().toIntList().iterator()

fun readBoard(block: String) = Board(readBoardNumbers(block))

fun readBoardNumbers(block: String) = block.lines()
  .map { line -> line.split(' ').filter { it.isNotBlank() } }
  .flatMapIndexed { row, line -> line.mapIndexed { col, value -> value.toInt() to Position(row, col) } }.toMap()