package io.dazraf.aoc.y2020

import io.dazraf.aoc.*
import io.dazraf.aoc.utilities.*
import io.dazraf.aoc.y2020.Day12.Op.*

object Day12 : Puzzle(2020, 12, "Rain Risk") {
  @JvmStatic
  fun main(args: Array<String>) = Day12.solve()

  data class Instruction(val op: Op, val amount: Int) {
    companion object {
      fun parse(line: String) = Instruction(Op.valueOf(line.take(1)), line.drop(1).toInt())
    }
  }

  enum class Op { N, S, E, W, L, R, F }

  fun part1(): Int {
    data class State(val position: Pair<Int, Int> = 0 to 0, val direction: Pair<Int, Int> = 1 to 0)

    val boat = parseInstructions()
      .fold(State()) { state, instruction ->
        when (instruction.op) {
          N -> state.copy(position = state.position.addY(instruction.amount))
          S -> state.copy(position = state.position.addY(-instruction.amount))
          E -> state.copy(position = state.position.addX(instruction.amount))
          W -> state.copy(position = state.position.addX(-instruction.amount))
          R -> state.copy(direction = state.direction.rotate(instruction.amount))
          L -> state.copy(direction = state.direction.rotate(-instruction.amount))
          F -> state.copy(position = state.position + state.direction * instruction.amount)
        }
      }
    return boat.position.manhattanDistance
  }

  fun part2(): Int {
    data class State(val boat: Pair<Int, Int> = 0 to 0, val wayPoint: Pair<Int, Int> = 10 to 1)
    val (boat, _) = parseInstructions()
      .fold(State()) { state, instruction ->
        when (instruction.op) {
          N -> state.copy(wayPoint = state.wayPoint.addY(instruction.amount))
          S -> state.copy(wayPoint = state.wayPoint.addY(-instruction.amount))
          E -> state.copy(wayPoint = state.wayPoint.addX(instruction.amount))
          W -> state.copy(wayPoint = state.wayPoint.addX(-instruction.amount))
          R -> state.copy(wayPoint = state.wayPoint.rotate(instruction.amount))
          L -> state.copy(wayPoint = state.wayPoint.rotate(-instruction.amount))
          F -> state.copy(boat = state.boat + state.wayPoint * instruction.amount)
        }
      }
    return boat.manhattanDistance
  }

  private fun parseInstructions() = dataAsLines.map(Instruction::parse)
}