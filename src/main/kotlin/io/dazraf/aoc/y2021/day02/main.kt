package io.dazraf.aoc.y2021.day02

import io.dazraf.aoc.*

fun main() = Puzzle(2021, 2, "Dive!").solve(Puzzle::part1, Puzzle::part2)

fun Puzzle.part1() = run(part1Strategy)
fun Puzzle.part2() = run(part2Strategy)

fun Puzzle.run(strategy: Map<String, (Int) -> (State) -> State>) =
  dataSequence
    .parseWith(strategy)
    .fold(State()) { s, fn -> fn(s) }
    .let { it.x * it.y }

const val FORWARD = "forward"
const val UP = "up"
const val DOWN = "down"

val part1Strategy = mapOf<String, (Int) -> (State) -> State>(
  FORWARD to { amount -> { s -> s.copy(x = s.x + amount) } },
  UP to { amount -> { s -> s.copy(y = s.y - amount) } },
  DOWN to { amount -> { s -> s.copy(y = s.y + amount) } }
)

val part2Strategy = mapOf<String, (Int) -> (State) -> State>(
  FORWARD to { amount -> { s -> s.copy(x = s.x + amount, y = s.y + s.aim * amount) } },
  UP to { amount -> { s -> s.copy(aim = s.aim - amount) } },
  DOWN to { amount -> { s -> s.copy(aim = s.aim + amount) } }
)

data class State(val x: Int = 0, val y: Int = 0, val aim: Int = 0)

fun Sequence<String>.parseWith(strategy: Map<String, (Int) -> (State) -> State>) =
  parsePairs().map { (command, amount) ->
    strategy[command]?.invoke(amount) ?: error("unknown command $command")
  }

fun Sequence<String>.parsePairs() =
  this.map { line -> line.trim().split(' ').let { (c, a) -> c to a.toInt() } }
