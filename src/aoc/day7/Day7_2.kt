package aoc.day7

import java.io.File
import java.io.FileReader
import java.util.LinkedList
import java.util.Queue

fun main() {

    val file = File("src/aoc/day7/input.txt")
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
    return

//    println(beamQueue)
//
//
//    while (beamQueue.isNotEmpty()) {
//        val beamIndex = beamQueue.remove()
//
//        val bottom = beamIndex.copy(i = beamIndex.i + 1)
//        if (bottom.isValid(m, n)) {
//            if (matrix[bottom.i][bottom.j] == '.') {
//                if (added.contains(bottom).not()) {
//                    beamQueue.add(bottom)
//                    added.add(bottom)
//                }
//            } else {
//                count++
//                val bottomRight = bottom.copy(j = bottom.j + 1)
//                val bottomLeft = bottom.copy(j = bottom.j - 1)
//
//                if (bottomRight.isValid(m, n)) {
//                    if (added.contains(bottomRight).not()) {
//                        beamQueue.add(bottomRight)
//                        added.add(bottomRight)
//                    }
//                }
//
//                if (bottomLeft.isValid(m, n)) {
//                    if (added.contains(bottomLeft).not()) {
//                        beamQueue.add(bottomLeft)
//                        added.add(bottomLeft)
//                    }
//                }
//            }
//        }
//    }
//
//    println(count)
}