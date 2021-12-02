package io.dazraf.aoc

import com.github.lalyos.jfiglet.FigletFont
import io.dazraf.aoc.utilities.matches
import io.github.classgraph.ClassGraph
import java.lang.reflect.Modifier


private const val santa = "🎅"
private const val tree = "🎄"
private const val present = "🎁"

fun main() {
  println(jumboText("Advent of Code!"))
  allPuzzles.forEach { it.solve() }
}

open class Puzzle(val year: Int, val day: Int, val title: String = "") {
  val resourceFile = "io/dazraf/aoc/y${year}/day${day}.txt"
}

fun Puzzle.solve() {
  println(jumboText("Year $year Day $day", "script"))
  println(title)
  this.javaClass.declaredMethods.filter { Modifier.isPublic(it.modifiers) && it.name.matches("part\\d+") }
    .sortedBy { it.name }.forEach { fn ->
      println("$present Part ${fn.name.drop(4)}")
      println(fn.invoke(this))
    }
}

val Puzzle.dataAsIntList get() = dataAsLines.map { it.toInt() }.toList()
val Puzzle.dataAsLongArray get() = dataAsLines.map { it.toLong() }.toList().toTypedArray()
val Puzzle.puzzleDataURL
  get() = this.javaClass.classLoader.getResource(resourceFile) ?: error("resource not found: $resourceFile")
val Puzzle.dataAsString get() = puzzleDataURL.readText()
val Puzzle.dataAsLines get() = puzzleDataURL.openStream().bufferedReader().lineSequence()
val Puzzle.dataAsBlocks get() = dataAsString.split(System.lineSeparator() + System.lineSeparator())
val Puzzle.dataAsGroupedFields
  get() = dataAsBlocks.map { block ->
    block
      .split("\\s".toRegex())
      .filter { it.isNotBlank() }
      .associate { field ->
        val (n, v) = field.trim().split(':')
        n to v
      }.toSortedMap()
  }

fun Map<String, String>.field(name: String, default: String = ""): String = get(name) ?: default
fun Map<String, String>.intField(name: String, remove: String = "") = field(name).replace(remove, "").toIntOrNull() ?: 0

val allPuzzles by lazy {
  ClassGraph()
    .enableClassInfo()
    .scan()
    .getClassInfo(Puzzle::class.qualifiedName).subclasses
    .map { classInfo -> classInfo.loadClass() }
    .sortedBy { it.name }
    .mapNotNull { it.kotlin.objectInstance as Puzzle? }
}

private fun jumboText(message: String, font: String = "rectangles") =
  FigletFont.convertOneLine("classpath:/io/dazraf/aoc/fonts/$font.flf", message)