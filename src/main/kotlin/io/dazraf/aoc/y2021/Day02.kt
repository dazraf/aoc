package io.dazraf.aoc.y2021

import io.dazraf.aoc.*

object Day02 : Puzzle(2021, 2, "Dive!") {
  @JvmStatic
  fun main(args: Array<String>) = solve()

  fun part1() = run(part1Strategy, Part1State()) { it.y * it.x }
  fun part2() = run(part2Strategy, Part2State()) { it.y * it.x }

  private fun <S : Any> run(strategy: Strategy<S>, initial: S, resultCalc: (S) -> Int) =
    dataAsLines.parse(strategy)
      .fold(initial) { state, fn -> fn(state) }
      .let(resultCalc)

  private data class Part1State(val x: Int = 0, val y: Int = 0)

  private val part1Strategy = Strategy<Part1State>(
    forward = { amount -> { s -> s.copy(x = s.x + amount) } },
    up = { amount -> { s -> s.copy(y = s.y - amount) } },
    down = { amount -> { s -> s.copy(y = s.y - amount) } }
  )

  private data class Part2State(val x: Int = 0, val y: Int = 0, val aim: Int = 0)

  private val part2Strategy = Strategy<Part2State>(
    forward = { amount ->
      { pos ->
        pos.copy(
          x = pos.x + amount,
          y = pos.y + pos.aim * amount
        )
      }
    },
    up = { amount -> { pos -> pos.copy(aim = pos.aim - amount) } },
    down = { amount -> { pos -> pos.copy(aim = pos.aim + amount) } }
  )

  private data class Strategy<S : Any>(
    val forward: (Int) -> (S) -> S,
    val up: (Int) -> (S) -> S,
    val down: (Int) -> (S) -> S
  )

  /**
   * parse the instructions to generate a [Sequence] of transformers ([S]) -> [S]
   */
  private fun <S : Any> Sequence<String>.parse(strategy: Strategy<S>) = parsePairs().map { (command, amount) ->
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