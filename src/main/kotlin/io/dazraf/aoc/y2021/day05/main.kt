package io.dazraf.aoc.y2021.day05

import io.dazraf.aoc.Puzzle
import io.dazraf.aoc.utilities.*
import kotlin.math.abs
import kotlin.math.max

fun main() = Puzzle(2021, 5, "Hydrothermal Venture").solve(Puzzle::part1, Puzzle::part2)

fun Puzzle.part1(): Int = run(Segment::isRectilinear)
fun Puzzle.part2(): Int = run { true }

fun Puzzle.run(segmentSelectionStrategy: (Segment) -> Boolean): Int {
  val points = parseSegments().filter(segmentSelectionStrategy).flatMap(Segment::generatePoints)
  val pointCount = points.groupBy { it }.map { it.key to it.value.size }
  return pointCount.filter { (_, v) -> v > 1 }.size
}

typealias Point = Pair<Int, Int>
val Point.x get() = first
val Point.y get() = second

data class Segment(val start: Point, val end: Point) {
  val isRectilinear get() = (start.x == end.x) || (start.y == end.y)
}

fun Segment.generatePoints(): List<Point> {
  val delta = end - start
  val normalised = delta / max(abs(delta.x), abs(delta.y))
  return generateSequence(start) {
    when (it) {
      end -> null
      else -> it + normalised
    }
  }.toList()
}

fun Puzzle.parseSegments() = dataList.map { line ->
  line.split("->").map { it.trim() }.map(::parseSegment).let { (start, end) -> Segment(start, end)}
}

fun parseSegment(txt: String) = txt.toIntList().let { (x, y) -> Point(x, y) }
