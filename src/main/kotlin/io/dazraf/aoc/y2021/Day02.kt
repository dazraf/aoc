package io.dazraf.aoc.y2021

import io.dazraf.aoc.*

object Day02 : Puzzle(2021, 2, "Dive!") {
  @JvmStatic
  fun main(args: Array<String>) = solve()

  fun part1() = run(part1Strategy)
  fun part2() = run(part2Strategy)

  private fun run(strategy: Strategy) = dataAsLines
    .parseWith(strategy)
    .fold(State(), Day02::evolveState)
    .let { it.x * it.y }

  private fun evolveState(state: State, fn: (State) -> State) = fn(state)

  private val part1Strategy = Strategy(
    forward = { amount -> { s -> s.copy(x = s.x + amount) } },
    up = { amount -> { s -> s.copy(y = s.y - amount) } },
    down = { amount -> { s -> s.copy(y = s.y + amount) } }
  )

  private val part2Strategy = Strategy(
    forward = { amount -> { s -> s.copy(x = s.x + amount, y = s.y + s.aim * amount) } },
    up = { amount -> { s -> s.copy(aim = s.aim - amount) } },
    down = { amount -> { s -> s.copy(aim = s.aim + amount) } }
  )

  private data class State(val x: Int = 0, val y: Int = 0, val aim: Int = 0)

  /**
   * A strategy is a set of functions, one per command type, that given an amount for the command,
   * create a [State] evolution function ([State]) -> [State]
   */
  private data class Strategy(
    val forward: (Int) -> (State) -> State,
    val up: (Int) -> (State) -> State,
    val down: (Int) -> (State) -> State
  )

  /**
   * parse the instructions to generate a [Sequence] of [State] evolving functions ([State]) -> [State]
   */
  private fun Sequence<String>.parseWith(strategy: Strategy) = parsePairs().map { (command, amount) ->
    when (command) {
      "forward" -> strategy.forward(amount)
      "down" -> strategy.down(amount)
      "up" -> strategy.up(amount)
      else -> error("unknown command $command")
    }
  }

  /**
   * first stage of parsing to [Pair<String, Int>]
   */
  private fun Sequence<String>.parsePairs() =
    this.map { line -> line.trim().split(' ').let { (c, a) -> c to a.toInt() } }
}