package aoc.day06

import java.io.File
import java.io.FileReader

fun main() {

    val file = File("src/aoc/day06/input.txt")
    val fileReader = FileReader(file)

    val input = fileReader.readText()
    val lines = input.split("\n")

    val cols = hashMapOf<Int, MutableList<String>>()
    val rowsCount = lines[0].split(" ")

    for (i in lines.indices) {
        val line = lines[i]

        var currLine = line
        while (currLine.contains("  ")) {
            currLine = currLine.replace("  ", " ")
        }

        val rows = currLine.split(" ").filter { it.isNotBlank() }
        for (j in rows.indices) {
            val curr = cols.getOrDefault(j, mutableListOf())
            curr.add(rows[j])
            cols[j] = curr
        }
    }

    var res = 0L
    for ((col, vals) in cols) {
        if (vals.last() == "*") {
            var curr = 1L
            vals.dropLast(1).map { it.toLong() }.forEach { curr *= it }
            res += curr
        } else {
            res += vals.dropLast(1).map { it.toLong() }.sum()
        }
    }

    println(res)
}