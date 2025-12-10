package aoc.day07

import java.io.File
import java.io.FileReader
import java.util.LinkedList
import java.util.Queue

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
    for (i in matrix.indices) {
        for (j in matrix[0].indices) {
            if (matrix[i][j] == 'S') {
                beamQueue.add(Index(i, j))
                added.add(Index(i, j))
                break
            }
        }
    }

    val memo = hashMapOf<Index, Long>()
    fun countTimeline(index: Index): Long {
        if (memo[index] != null) return memo[index]!!
        if (index.isValid(m, n).not()) return 1
        if (matrix[index.i][index.j] in setOf('.', 'S')) {
            return countTimeline(index.copy(i = index.i + 1))
        }

        return (countTimeline(index.copy(i = index.i + 1, j = index.j + 1)) +
                countTimeline(index.copy(i = index.i + 1, j = index.j - 1))).also {
                    memo[index] = it
        }
    }

    println(countTimeline(beamQueue.remove()))
}