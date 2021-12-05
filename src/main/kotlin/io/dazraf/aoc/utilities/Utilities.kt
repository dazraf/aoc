package io.dazraf.aoc.utilities

import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

// Maths
@JvmName("plusIntInt")
operator fun Pair<Int, Int>.plus(rhs: Pair<Int, Int>): Pair<Int, Int> = first + rhs.first to second + rhs.second

@JvmName("minusIntInt")
operator fun Pair<Int, Int>.minus(rhs: Pair<Int, Int>): Pair<Int, Int> = first - rhs.first to second - rhs.second

@JvmName("timesIntInt")
operator fun Pair<Int, Int>.times(rhs: Pair<Int, Int>): Pair<Int, Int> = first * rhs.first to second * rhs.second

@JvmName("divIntInt")
operator fun Pair<Int, Int>.div(rhs: Pair<Int, Int>): Pair<Int, Int> = first / rhs.first to second / rhs.second
operator fun Pair<Int, Int>.times(factor: Int): Pair<Int, Int> = first * factor to second * factor
val Pair<Int, Int>.manhattanDistance get() = abs(first) + abs(second)
fun Pair<Int, Int>.addY(amount: Int) = Pair(first, second + amount)
fun Pair<Int, Int>.addX(amount: Int) = Pair(first + amount, second)
fun Pair<Int, Int>.rotate(degrees: Int): Pair<Int, Int> {
  val radians = Math.toRadians(-degrees.toDouble())
  val s = sin(radians)
  val c = cos(radians)
  val newX = (first * c - second * s).roundToInt()
  val newY = (first * s + second * c).roundToInt()
  return Pair(newX, newY)
}

@JvmName("plusShortShort")
operator fun Pair<Short, Short>.plus(rhs: Pair<Short, Short>): Pair<Int, Int> = first + rhs.first to second + rhs.second

@JvmName("minusShortShort")
operator fun Pair<Short, Short>.minus(rhs: Pair<Short, Short>): Pair<Int, Int> =
  first - rhs.first to second - rhs.second

@JvmName("timesShortShort")
operator fun Pair<Short, Short>.times(rhs: Pair<Short, Short>): Pair<Int, Int> =
  first * rhs.first to second * rhs.second

@JvmName("divShortShort")
operator fun Pair<Short, Short>.div(rhs: Pair<Short, Short>): Pair<Int, Int> = first / rhs.first to second / rhs.second

@JvmName("plusByteByte")
operator fun Pair<Byte, Byte>.plus(rhs: Pair<Byte, Byte>): Pair<Int, Int> = first + rhs.first to second + rhs.second

@JvmName("minusByteByte")
operator fun Pair<Byte, Byte>.minus(rhs: Pair<Byte, Byte>): Pair<Int, Int> = first - rhs.first to second - rhs.second

@JvmName("timesByteByte")
operator fun Pair<Byte, Byte>.times(rhs: Pair<Byte, Byte>): Pair<Int, Int> = first * rhs.first to second * rhs.second

@JvmName("divByteByte")
operator fun Pair<Byte, Byte>.div(rhs: Pair<Byte, Byte>): Pair<Int, Int> = first / rhs.first to second / rhs.second

operator fun Pair<Long, Long>.plus(rhs: Pair<Long, Long>): Pair<Long, Long> = first + rhs.first to second + rhs.second
operator fun Pair<Long, Long>.minus(rhs: Pair<Long, Long>): Pair<Long, Long> = first - rhs.first to second - rhs.second
operator fun Pair<Long, Long>.times(rhs: Pair<Long, Long>): Pair<Long, Long> = first * rhs.first to second * rhs.second
operator fun Pair<Long, Long>.div(rhs: Pair<Long, Long>): Pair<Long, Long> = first / rhs.first to second / rhs.second

operator fun Pair<Double, Double>.times(number: Number): Pair<Double, Double> {
  val d = number.toDouble()
  return first * d to second * d
}

fun List<Int>.multiply() = reduceOrNull { lhs, rhs -> lhs * rhs } ?: 0
fun List<Long>.multiply() = reduceOrNull { lhs, rhs -> lhs * rhs } ?: 0
inline operator fun <reified T : Number> T.plus(rhs: List<T>): List<T> = listOf(this) + rhs

// Collections and related

fun Int.`in`(range: IntRange): Boolean = this >= range.first && this <= range.last
fun <T> T.`in`(list: List<T>) = list.contains(this)
fun String.matches(expression: String) = Regex(expression).matches(this)

fun <T> Iterable<T>.combinations(size: Int): Sequence<List<T>> {
  return sequence {
    when (size) {
      0 -> yield(emptyList<T>())
      else -> {
        this@combinations.forEach { value ->
          this@combinations.drop(1).combinations(size - 1).forEach {
            yield(listOf(value) + it)
          }
        }
      }
    }
  }
}

inline fun <reified T> Sequence<T>.toTypedArray() = toList().toTypedArray()
fun parseBinaryInt(str: String) = Integer.parseUnsignedInt(str, 2)
fun String.toLongList() = split(",").map(String::trim).map(String::toLong)
fun IntRange.grow(amount: Int) = (start - amount)..(endInclusive + amount)

fun IntArray.incrementForRanges(ranges: List<IntRange>): IntArray {
  val clone = this.clone()
  var carry = 1
  var index = ranges.size - 1
  while (carry == 1 && index >= 0) {
    val range = ranges[index]
    val sum = clone[index] + carry
    when {
      sum > range.last -> clone[index] = range.first
      else -> {
        clone[index] = sum
        carry = 0
      }
    }
    index--
  }
  return clone
}

fun IntArray.isMaxOfRanges(ranges: List<IntRange>) = this.zip(ranges).all { (value, range) -> value == range.last }
fun List<IntRange>.generatePointSequence(): Sequence<IntArray> = generateSequence(this.map { it.first }.toIntArray()) {
  when {
    it.isMaxOfRanges(this) -> null
    else -> it.incrementForRanges(this)
  }
}

fun List<IntRange>.grow(amount: Int): List<IntRange> = map { range -> (range.first - amount)..(range.last + amount) }
fun String.toIntList() = split(',').map(String::toInt)
