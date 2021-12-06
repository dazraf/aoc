package io.dazraf.aoc.y2020.day08

import io.dazraf.aoc.Puzzle
import io.dazraf.aoc.y2020.day08.Op.*

fun main() = Puzzle(2020, 8, "Handheld Halting").solve(Puzzle::part1, Puzzle::part2)
fun Puzzle.part1(): Int = runVM(dataSequence.parseProgram()).acc

fun Puzzle.part2(): Int {
  val vm = sequenceOfViablePrograms(dataSequence.parseProgram())
    .map(::runVM)
    .filter { !it.loopDetected }
    .first()
  return vm.acc
}

fun sequenceOfViablePrograms(program: Array<Instruction>) = sequence {
  yield(program)
  program
    .createModifiedInstructions()
    .forEach { (index, instruction) ->
      yield(program.copyOf().apply {
        this[index] = instruction
      })
    }
}

/**
 * The registers of our virtual machine
 */
data class Registers(val acc: Int = 0, val pc: Int = 0, val loopDetected: Boolean = false)

/**
 * set of allowed operations
 */
enum class Op {
  NOP,
  ACC,
  JMP
}

interface Transformer : (Registers) -> Registers

/**
 * A [Transformer] that applies an [op] and [operand] to a [Registers]
 */
data class Instruction(val op: Op, val operand: Int = 0) : Transformer {
  override fun toString(): String = "$op $operand"
  override fun invoke(r: Registers) = with(r) {
    when (op) {
      ACC -> copy(acc = acc + operand, pc = pc + 1)
      NOP -> copy(pc = pc + 1)
      JMP -> copy(pc = pc + operand)
    }
  }
}

/**
 * A [Transformer] that wraps a [transformer] and ensures that it's only called once
 * If it called more than once it sets the [Registers.loopDetected] flag
 */
data class Guard(val transformer: Transformer) : Transformer {
  private var visited: Boolean = false
  override operator fun invoke(r: Registers) = if (visited) {
    r.copy(loopDetected = true)
  } else {
    visited = true
    transformer.invoke(r)
  }
}

fun <T : Transformer> runVM(instructions: Array<T>): Registers {
  val guardedInstructions = instructions.map { Guard(it) }.toTypedArray()
  return generateSequence(Registers()) { registers ->
    when {
      registers.loopDetected || registers.pc >= guardedInstructions.size -> null
      else -> guardedInstructions[registers.pc](registers)
    }
  }.last()
}

fun Sequence<String>.parseProgram() = map(::parseInstruction).toList().toTypedArray()

fun parseInstruction(line: String) = line.split(' ').let { (opString, operandString) ->
  val operand = operandString.toInt()
  val op = when (opString) {
    "acc" -> ACC
    "nop" -> NOP
    "jmp" -> JMP
    else -> error("unknown op: $operandString")
  }
  Instruction(op, operand)
}

fun Array<Instruction>.createModifiedInstructions() =
  mapIndexed { index, instruction -> index to instruction }
    .filter { (_, instruction) -> (instruction.op == NOP || instruction.op == JMP) }
    .flipInstructions()

fun List<Pair<Int, Instruction>>.flipInstructions() =
  map { (index, instruction) ->
    index to instruction.copy(
      op = when (instruction.op) {
        JMP -> NOP
        NOP -> JMP
        else -> instruction.op
      }
    )
  }


