package io.dazraf.aoc.y2020

import io.dazraf.aoc.*
import io.dazraf.aoc.utilities.plus
import io.dazraf.aoc.utilities.toTypedArray
import io.dazraf.aoc.y2020.Day11.World
import io.dazraf.aoc.y2020.Day11.World.Companion.EMPTY
import io.dazraf.aoc.y2020.Day11.World.Companion.FLOOR
import io.dazraf.aoc.y2020.Day11.World.Companion.OCCUPIED
import io.dazraf.aoc.y2020.Day11.World.Companion.parse

/**
 * The value of a cell in a [World]
 */
typealias Cell = Char
/**
 * A position in the world
 */
typealias Coordinate = Pair<Int, Int>
/**
 * function that computes number of occupied seats for a given [Coordinate] in a [World]
 */
typealias OccupiedCounter = World.(Coordinate) -> Int
/**
 * function that evolves a [Coordinate] in a [World] returning a new Cell value
 */
typealias CoordinateEvolver = World.(Coordinate) -> Cell

object Day11 : Puzzle(2020, 11, "Seating System") {
  @JvmStatic
  fun main(args: Array<String>) = Day11.solve()

  fun part1(): Int {
    val part1Evolver = evolverGenerator(4, nearestNeighbourCounter)
    return runSimulation(part1Evolver)
  }

  fun part2(): Int {
    val part1Evolver = evolverGenerator(5, lineOfSightCounter)
    return runSimulation(part1Evolver)
  }

  private fun runSimulation(part1Evolver: CoordinateEvolver): Int {
    val lastWorld = generateSequence(parse(dataAsLines.toList())) { world ->
      when (val newWorld = world.evolve(part1Evolver)) {
        world -> null
        else -> newWorld
      }
    }.onEach {
//            println(it)
//            println()
    }.last()
    return lastWorld.occupiedCount
  }

  /**
   * @return a function that can evolve a [World] given the [occupiedCounter] and threshold [countThatMakesOccupierLeave]
   */
  private fun evolverGenerator(countThatMakesOccupierLeave: Int, occupiedCounter: OccupiedCounter): CoordinateEvolver {
    return { coords ->
      val value = get(coords)
      when {
        value == OCCUPIED && occupiedCounter(coords) >= countThatMakesOccupierLeave -> EMPTY
        value == EMPTY && occupiedCounter(coords) == 0 -> OCCUPIED
        else -> value
      }
    }
  }

  /**
   * Counts occupied seats based on nearest neighbours
   */
  private val nearestNeighbourCounter: OccupiedCounter = { coords ->
    nearestNeighbours.count { isOccupied(it + coords) }
  }

  /**
   * Counts occupied seats based on line-of-sight
   */
  private val lineOfSightCounter: OccupiedCounter = { coords ->
    nearestNeighbours.flatMap { direction ->
      generateSequence(coords + direction) { location ->
        when {
          location.inside(this) && this[location] == FLOOR -> location + direction
          else -> null
        }
      }.map { this[it] }.filter { it == OCCUPIED }
    }.count()
  }

  /**
   * A template for the nearest neighbours to a cell
   */
  private val nearestNeighbours = listOf(
    -1 to -1, 0 to -1, 1 to -1,
    -1 to 0, 1 to 0,
    -1 to 1, 0 to 1, 1 to 1,
  )

  private fun World.evolve(coordinateEvolver: CoordinateEvolver) =
    World(width, height, walk().map { coordinateEvolver(it) }.toTypedArray())

  private val World.occupiedCount get() = walk().count { isOccupied(it) }
  private fun World.isOccupied(coordinate: Coordinate) = get(coordinate) == OCCUPIED
  private fun Coordinate.outside(world: World) =
    let { (x, y) -> x < 0 || y < 0 || x >= world.width || y >= world.height }

  private fun Coordinate.inside(world: World) = !outside(world)

  data class World(
    val width: Int,
    val height: Int,
    val data: Array<Cell>
  ) {
    companion object {
      const val FLOOR = '.'
      const val EMPTY = 'L'
      const val OCCUPIED = '#'

      fun parse(lines: List<String>) = World(
        lines.firstOrNull()?.length ?: 0,
        lines.size,
        lines.flatMap { line -> line.map { it } }.toTypedArray()
      )
    }

    operator fun get(coordinate: Coordinate) = coordinate.let { (x, y) ->
      when {
        x < 0 || y < 0 || x >= width || y >= height -> FLOOR
        else -> data[y * width + x]
      }
    }

    fun walk() = sequence {
      (0 until height).forEach { y ->
        (0 until width).forEach { x ->
          yield(x to y)
        }
      }
    }

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (javaClass != other?.javaClass) return false

      other as World

      if (width != other.width) return false
      if (height != other.height) return false
      if (!data.contentEquals(other.data)) return false

      return true
    }

    override fun hashCode(): Int {
      var result = width
      result = 31 * result + height
      result = 31 * result + data.contentHashCode()
      return result
    }

    override fun toString(): String {
      return (0 until height).joinToString("\n") { y ->
        (0 until width).map { x -> get(x to y) }.joinToString("")
      }
    }
  }
}



