package io.dazraf.aoc

import com.github.lalyos.jfiglet.FigletFont
import io.dazraf.aoc.utilities.matches
import io.github.classgraph.ClassGraph
import java.io.File

fun main() {
  println(jumboText("Advent of Code!"))

  val scanResult = ClassGraph()
    .enableClassInfo()
    .enableMethodInfo()
    .scan()

  val packageInfos =
    scanResult.packageInfo.filter { it.name.matches("""io\.dazraf\.aoc\.y\d+\.day\d+""") }.toList().sorted()

  packageInfos.map { packageInfo ->
    packageInfo.classInfo.filter { classInfo -> classInfo.name.endsWith("MainKt") }.single()
  }
    .flatMap { it.methodInfo }
    .filter { it.name == "main" && it.parameterInfo.isEmpty() }
    .forEach { it.loadClassAndGetMethod().invoke(null) }
}

open class Puzzle(val year: Int, val day: Int, val title: String = "") {
  val dataFile = File("src/main/kotlin/io/dazraf/aoc/y${year}/day${day.toString().padStart(2, '0')}/data.txt")
  val data by lazy { dataFile.readText() }
  val dataList by lazy { dataFile.readLines() }
  val dataSequence get() = dataFile.inputStream().bufferedReader().lineSequence()
  val dataIntList by lazy { dataList.map(String::toInt) }
  val dataAsLongArray by lazy { dataSequence.map { it.toLong() }.toList().toTypedArray() }
  val dataIntSequence get() = dataList.asSequence().map { it.toInt() }
  val dataAsBlocks by lazy { data.split(System.lineSeparator() + System.lineSeparator()) }
  val dataAsGroupedFields by lazy {
    dataAsBlocks.map { block ->
      block
        .split("\\s".toRegex())
        .filter { it.isNotBlank() }
        .associate { field ->
          val (n, v) = field.trim().split(':')
          n to v
        }.toSortedMap()
    }
  }

  override fun toString(): String {
    return "${jumboText("Year $year Day $day", "script")}\n$title"
  }

  fun solve(vararg parts: Puzzle.() -> Any) {
    println(this)
    parts.forEachIndexed { index, fn ->
      println("Part ${index + 1}")
      println(fn())
    }
  }
}

fun Map<String, String>.field(name: String, default: String = ""): String = get(name) ?: default
fun Map<String, String>.intField(name: String, remove: String = "") = field(name).replace(remove, "").toIntOrNull() ?: 0

private fun jumboText(message: String, font: String = "rectangles") =
  FigletFont.convertOneLine("classpath:/io/dazraf/aoc/fonts/$font.flf", message)