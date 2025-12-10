package aoc.day04

import java.io.File
import java.io.FileReader

fun main() {
    val file = File("src/aoc/day04/input.txt")
    val fileReader = FileReader(file)

    val input = fileReader.readText()
    val lines = input.split("\n")
    val matrix = Array(lines.size) { CharArray(lines.first().length) }

    for (i in lines.indices) {
        for (j in lines.first().indices) {
            matrix[i][j] = lines[i][j]
        }
    }

    var count = 0
    val col = arrayOf(1, 1, 1, -1, -1, -1, 0, 0)
    val row = arrayOf(0, -1, 1, 0, -1, 1, 1, -1)
    for (i in matrix.indices) {
        for (j in matrix[0].indices) {
            if (matrix[i][j] != '@') continue
            var adjCount = 0

            for (k in 0 until 8) {
                val newI = i + row[k]
                val newJ = j + col[k]

                if (matrix.getOrNull(newI)?.getOrNull(newJ) == '@') {
                    adjCount++
                }
            }

            if (adjCount < 4) {
                count++
            }
        }
    }

    println(count)
}