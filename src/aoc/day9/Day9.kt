package aoc.day9

import java.io.File
import java.io.FileReader
import kotlin.math.abs
import kotlin.math.max

data class Coor(
    val i: Int,
    val j: Int
)

fun main() {

    val file = File("src/aoc/day9/input.txt")
    val fileReader = FileReader(file)

    val input = fileReader.readText()
    val lines = input.split("\n")
    val coords = mutableListOf<Coor>()
    for (line in lines) {
        val sp = line.split(",")
        coords.add(
            Coor(
                i = sp[0].toInt(),
                j = sp[1].toInt()
            )
        )
    }

    var largest = 0L

    for (i in coords.indices) {
        for (j in (i + 1) until coords.size) {
            val c1 = coords[i]
            val c2 = coords[j]

            val height = abs(c1.i - c2.i) + 1
            val width = abs(c1.j - c2.j) + 1

            largest = max(largest, height.toLong() * width)
        }
    }

    println(largest)
}