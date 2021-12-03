package io.dazraf.aoc.y2021

import io.dazraf.aoc.Puzzle
import io.dazraf.aoc.dataAsLines
import io.dazraf.aoc.solve

object Day02 : Puzzle(2021, 2, "Dive!") {
  @JvmStatic
  fun main(args: Array<String>) = solve()

  fun part1() = run(part1Strategy)
  fun part2() = run(part2Strategy)

  private fun run(strategy: Map<String, (Int) -> (State) -> State>) =
    dataAsLines
      .parseWith(strategy)
      .fold(State()) { s, fn -> fn(s) }
      .let { it.x * it.y }

  private const val FORWARD = "forward"
  private const val UP = "up"
  private const val DOWN = "down"

  private val part1Strategy = mapOf<String, (Int) -> (State) -> State>(
    FORWARD to { amount -> { s -> s.copy(x = s.x + amount) } },
    UP to { amount -> { s -> s.copy(y = s.y - amount) } },
    DOWN to { amount -> { s -> s.copy(y = s.y + amount) } }
  )

  private val part2Strategy = mapOf<String, (Int) -> (State) -> State>(
    FORWARD to { amount -> { s -> s.copy(x = s.x + amount, y = s.y + s.aim * amount) } },
    UP to { amount -> { s -> s.copy(aim = s.aim - amount) } },
    DOWN to { amount -> { s -> s.copy(aim = s.aim + amount) } }
  )

  private data class State(val x: Int = 0, val y: Int = 0, val aim: Int = 0)

  private fun Sequence<String>.parseWith(strategy: Map<String, (Int) -> (State) -> State>) =
    parsePairs().map { (command, amount) ->
      strategy[command]?.invoke(amount) ?: error("unknown command $command")
    }

  private fun Sequence<String>.parsePairs() =
    this.map { line -> line.trim().split(' ').let { (c, a) -> c to a.toInt() } }
}
