package io.dazraf.aoc.y2020

import io.dazraf.aoc.Puzzle
import io.dazraf.aoc.dataAsLines
import io.dazraf.aoc.solve
import io.dazraf.aoc.utilities.generatePointSequence
import io.dazraf.aoc.utilities.grow
import io.dazraf.aoc.utilities.toTypedArray

object Day17 : Puzzle(2020, 17, "Conway Cubes") {
  @JvmStatic
  fun main(args: Array<String>) = Day17.solve()

  fun part1() = runSimulation(3, 6).cellCount
  fun part2() = runSimulation(4, 6).cellCount

  private fun runSimulation(dimensions: Int, iterations: Int) =
    generateSequence(readSpace(dimensions)) { space -> space.evolve() }.take(iterations + 1).last()

  private data class HyperSpace(val cells: Set<HyperCell>) {
    val cellCount get() = cells.size
    val dimensions = cells.toDimensionRanges()
    fun isActive(cell: HyperCell) = cells.contains(cell)
  }

  private data class HyperCell(val coords: IntArray) {
    val neighbourhood by lazy { cellSequence(this.coords.map { (it..it).grow(1) }).filter { neighbour -> neighbour != this } }

    // the joy of JVM
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is HyperCell) return false

      if (!coords.contentEquals(other.coords)) return false

      return true
    }

    override fun hashCode(): Int {
      return coords.contentHashCode()
    }
  }

  private fun Collection<HyperCell>.toDimensionRanges(): List<IntRange> = map { it.coords }.toDimensionRanges()

  private fun List<IntArray>.toDimensionRanges(): List<IntRange> {
    val dimensions = this[0].size
    return (0 until dimensions).map { dimension ->
      val range = this.map { it[dimension] }
      val min = range.minOrNull() ?: 0
      val max = range.maxOrNull() ?: 0
      min..max
    }
  }

  private fun HyperSpace.evolve() = HyperSpace(cellSequence(dimensions.grow(1)).filter { shouldBeActive(it) }.toSet())

  private fun cellSequence(dimensions: List<IntRange>) = dimensions.generatePointSequence().map(::HyperCell)

  private fun HyperSpace.neighboursOf(cell: HyperCell) = cell.neighbourhood.count { neighbour -> isActive(neighbour) }

  private fun HyperSpace.shouldBeActive(cell: HyperCell) = shouldBeActive(isActive(cell), neighboursOf(cell))

  private fun shouldBeActive(isActive: Boolean, neighbours: Int) =
    (isActive && (neighbours == 2 || neighbours == 3)) || (!isActive && neighbours == 3)

  private fun readSpace(dimensionCount: Int): HyperSpace {
    check(dimensionCount >= 2) { "dimensions must be greater than 1" }
    val seed = dataAsLines.map { line -> line.map { it == '#' } }.map { it.toBooleanArray() }.toTypedArray()
    val yOffset = seed.size / 2
    val xOffset = seed[0].size / 2
    val activeCells = seed.flatMapIndexed { y, ySlice ->
      ySlice.mapIndexed { x, cell -> x to cell }.filter { (_, cell) -> cell }
        .map { (x, _) ->
          val a = IntArray(dimensionCount) { 0 }
          a[0] = x - xOffset
          a[1] = y - yOffset
          HyperCell(a)
        }
    }.toSet()
    return HyperSpace(activeCells)
  }
}
