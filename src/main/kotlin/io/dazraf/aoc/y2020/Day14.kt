package io.dazraf.aoc.y2020

import io.dazraf.aoc.*
import io.dazraf.aoc.y2020.Day14.Instruction.Mask
import io.dazraf.aoc.y2020.Day14.Instruction.MemSet

object Day14 : Puzzle(2020, 14, "Docking Data") {
  @Suppress("RegExpRedundantEscape")
  private val re = "(mask|mem)(?:\\[(\\d+)\\])? = (.*)".toRegex()

  @JvmStatic
  fun main(args: Array<String>) = Day14.solve()

  fun part1(): Long {
    val initialVM = VM({ mask: Mask, memSet: MemSet ->
      listOf(memSet.address to ((mask.unknownMask and memSet.value) or mask.setterMask))
    })
    return run(initialVM)
  }

  fun part2(): Long {
    val mapper = { mask: Mask, memSet: MemSet ->
      generateAddresses(mask.value, memSet.address).map { it to memSet.value }.toList()
    }
    val initialVM = VM(mapper)
    return run(initialVM)
  }

  private fun run(initialVM: VM): Long {
    val instructions = dataAsLines.map { it.parseInstruction() }.toList()
    val vm = instructions.fold(initialVM) { vm, instruction -> vm.apply(instruction) }
    return vm.memory.values.sum()
  }

  data class VM(
    val mapper: (Mask, MemSet) -> List<Pair<Long, Long>>,
    val memory: Map<Long, Long> = emptyMap(),
    val mask: Mask = Mask("X".repeat(36))
  ) {

    fun apply(instruction: Instruction) = when (instruction) {
      is Mask -> apply(instruction)
      is MemSet -> apply(instruction)
    }

    private fun apply(mask: Mask) = copy(mask = mask)
    private fun apply(memSet: MemSet) = copy(memory = memSet.execute(memory, mask, mapper))
  }

  sealed interface Instruction {
    data class Mask(val value: String) : Instruction {
      val unknownMask = value.fold(0L) { acc, ch ->
        (acc shl 1) or when (ch) {
          'X' -> 1L
          else -> 0L
        }
      }
      val setterMask = value.fold(0L) { acc, ch ->
        (acc shl 1) or when (ch) {
          '1' -> 1L
          else -> 0L
        }
      }
    }

    data class MemSet(val address: Long, val value: Long) : Instruction {
      fun execute(memory: Map<Long, Long>, mask: Mask, mapper: (Mask, MemSet) -> List<Pair<Long, Long>>) =
        memory + mapper(mask, this).toMap()
    }
  }

  private fun String.parseInstruction(): Instruction {
    val mr = re.find(this) ?: error("failed to parse: $this")
    val op = mr.groups[1]!!.value
    val value = mr.groups[3]!!.value
    return when (op) {
      "mask" -> Mask(value)
      "mem" -> MemSet(mr.groups[2]!!.value.toLong(), value.toLong())
      else -> error("unknown op $op")
    }
  }

  private fun generateAddresses(mask: String, address: Long): Sequence<Long> = sequence {
    val rightMostBit = address and 1L
    when {
      mask.isNotEmpty() -> {
        // recursive descent, each right-shifting the mask and address, and calculating sub parts of the mapped addresses
        generateAddresses(mask.take(mask.length - 1), address / 2).forEach { subAddr ->
          val leftShift = subAddr shl 1
          when (mask.last()) {
            // for 'X' we emit two addresses - one with the bit set and one without
            'X' -> yieldAll(listOf(leftShift, leftShift or 1L))
            // we set the bit always for a 1
            '1' -> yield(leftShift or 1L)
            // we never set the bit
            '0' -> yield(leftShift or rightMostBit)
          }
        }
      }
      else -> yield(0L)
    }
  }
}

