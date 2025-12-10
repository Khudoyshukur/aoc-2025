package aoc.day07

import java.io.File
import java.io.FileReader
import java.util.LinkedList
import java.util.Queue

data class Index(
    val i: Int,
    val j: Int
) {
    fun isValid(m: Int, n: Int): Boolean {
        return (i in (0 until m)) && (j in (0 until n))
    }
}

fun main() {

    val file = File("src/aoc/day07/input.txt")
    val fileReader = FileReader(file)

    val input = fileReader.readText()
    val lines = input.split("\n")


    val matrix = Array(lines.size) { CharArray(lines.first().length) }
    for (i in lines.indices) {
        for (j in lines[0].indices) {
            matrix[i][j] = lines[i][j]
        }
    }

    val m = matrix.size
    val n = matrix[0].size
    val beamQueue: Queue<Index> = LinkedList()
    val added = mutableSetOf<Index>()
    var count = 0
    for (i in matrix.indices) {
        for (j in matrix[0].indices) {
            if (matrix[i][j] == 'S') {
                beamQueue.add(Index(i, j))
                added.add(Index(i, j))
                break
            }
        }
    }

    while (beamQueue.isNotEmpty()) {
        val beamIndex = beamQueue.remove()

        val bottom = beamIndex.copy(i = beamIndex.i + 1)
        if (bottom.isValid(m, n)) {
            if (matrix[bottom.i][bottom.j] == '.') {
                if (added.contains(bottom).not()) {
                    beamQueue.add(bottom)
                    added.add(bottom)
                }
            } else {
                count++
                val bottomRight = bottom.copy(j = bottom.j + 1)
                val bottomLeft = bottom.copy(j = bottom.j - 1)

                if (bottomRight.isValid(m, n)) {
                    if (added.contains(bottomRight).not()) {
                        beamQueue.add(bottomRight)
                        added.add(bottomRight)
                    }
                }

                if (bottomLeft.isValid(m, n)) {
                    if (added.contains(bottomLeft).not()) {
                        beamQueue.add(bottomLeft)
                        added.add(bottomLeft)
                    }
                }
            }
        }
    }

    println(count)
}