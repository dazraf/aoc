package io.dazraf.aoc.y2021.day04

import io.dazraf.aoc.Puzzle

fun main() = Puzzle(2021, 4, "Giant Squid").solve(Puzzle::part1, Puzzle::part2)

fun Puzzle.part1(): Int {
  val (randomNumbers, boards) = parse()
  val winningBoard = generateSequence(boards) { currentBoards ->
    when {
      !randomNumbers.hasNext() || currentBoards.hasWinner() -> null
      else -> currentBoards.mark(randomNumbers.next())
    }
  }.last().single { it.isWinningBoard() }
  return winningBoard.score
}

fun Puzzle.part2(): Int {
  val (randomNumbers, boards) = parse()
  val winningBoard = generateSequence(boards) { currentBoards ->
    when {
      !randomNumbers.hasNext() -> null
      (currentBoards.size == 1 && currentBoards.single().isWinningBoard()) -> null
      else -> {
        val nextBoards = currentBoards.mark(randomNumbers.next())
        when (nextBoards.size) {
          1 -> nextBoards
          else -> nextBoards.filter { !it.isWinningBoard() }
        }
      }
    }
  }.onEach { println(it.format() + '\n') }
    .last().single { it.isWinningBoard() }
  return winningBoard.score
}

fun Puzzle.parse(): Pair<Iterator<Int>, List<Board>> {
  val iterator = dataSequence.iterator()
  val randomNumbers = iterator.readRandomNumbers().iterator()
  iterator.readLine()
  val boards = sequence {
    while (iterator.hasNext()) {
      yield(iterator.readBoard())
    }
  }.toList()
  return randomNumbers to boards
}

data class Position(val row: Int = 0, val col: Int = 0)

data class Board(val numbers: Map<Int, Position>, val lastNumber: Int = -1, val marked: Set<Position> = emptySet()) {
  private val rows = numbers.values.maxOf { it.row } + 1
  private val cols = numbers.values.maxOf { it.col } + 1

  fun mark(number: Int): Board = when (val position = numbers[number]) {
    null -> this
    else -> copy(marked = marked + position, lastNumber = number)
  }

  fun isWinningBoard(): Boolean {
    val seq =
      (0 until rows).asSequence().map { isWinningRow(it) } + (0 until cols).asSequence().map { isWinningCol(it) }
    return seq.any { it }
  }

  val score by lazy { numbers.filterValues { !isMarked(it) }.keys.sum() * lastNumber }

  private fun isWinningRow(row: Int) = (0 until cols).map { Position(row, it) }.all { isMarked(it) }
  private fun isWinningCol(col: Int) = (0 until rows).map { Position(it, col) }.all { isMarked(it) }
  private fun isMarked(pos: Position) = marked.contains(pos)
  override fun toString(): String = numbers.filterValues { marked.contains(it) }.keys.toString()
}

fun List<Board>.mark(number: Int) = map { it.mark(number) }
fun List<Board>.format() = joinToString("\n") { it.toString() }
fun List<Board>.hasWinner() = map { it.isWinningBoard() }.any { it }
fun Iterator<String>.readRandomNumbers() = next().split(',').map(String::toInt)
fun Iterator<String>.readLine() = next()
fun Iterator<String>.readBoard() = Board(readBoardNumbers())
private fun Iterator<String>.readBoardNumbers(): Map<Int, Position> {
  val numbers = generateSequence {
    if (hasNext()) {
      val line = next()
      when {
        line.isBlank() -> null
        else -> line.split(' ').filter { it.isNotBlank() }
      }
    } else {
      null
    }
  }.flatMapIndexed { row, line -> line.mapIndexed { col, value -> value.toInt() to Position(row, col) } }.toMap()
  return numbers
}
