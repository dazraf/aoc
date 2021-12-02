package io.dazraf.aoc.y2020

import io.dazraf.aoc.*
import io.dazraf.aoc.utilities.plus

object Day03 : Puzzle(2020, 3, "Toboggan Trajectory") {
  @JvmStatic
  fun main(args: Array<String>) = Day03.solve()

  fun part1(): Int {
    val board = Board.parse(dataAsString)
    return generateSequence((0 to 0)) { (x, y) ->
      (x + 3) to (y + 1)
    }.takeWhile { (_, y) ->
      y < board.height
    }.count { (x, y) ->
      board.isTree(x, y)
    }
  }

  fun part2(): Int {
    val board = Board.parse(dataAsString)
    return listOf(1 to 1, 3 to 1, 5 to 1, 7 to 1, 1 to 2).map { delta ->
      generateSequence((0 to 0)) { it + delta }.takeWhile { (_, y) ->
        y < board.height
      }.count { (x, y) ->
        board.isTree(x, y)
      }
    }.fold(1) { acc, trees -> acc * trees }
  }

  class Board(private val data: Array<BooleanArray>) {
    companion object {
      fun parse(txt: String): Board {
        return Board(txt.lines()
          .filter { it.isNotBlank() }
          .map { line ->
            line.trim().map {
              when (it) {
                '.' -> false
                else -> true
              }
            }.toBooleanArray()
          }.toTypedArray()
        )
      }
    }

    private val width by lazy {
      data.first().size
    }

    val height by lazy {
      data.size
    }

    fun isTree(x: Int, y: Int): Boolean {
      val xn = x.mod(width)
      val yn = y.mod(height)
      return data[yn][xn]
    }
  }
}

