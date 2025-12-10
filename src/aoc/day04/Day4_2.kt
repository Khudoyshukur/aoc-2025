package aoc.day04

import java.io.File
import java.io.FileReader
import java.util.LinkedList
import java.util.Queue

data class Index(val i: Int, val j: Int)

fun main() {

    val file = File("src/aoc/day4/input.txt")
    file.createNewFile()
    val fileReader = FileReader(file)

    val input = fileReader.readText()
    val lines = input.split("\n")
    val matrix = Array(lines.size) { CharArray(lines.first().length) }

    println(matrix.size)
    println(matrix.first().size)

    for (i in lines.indices) {
        for (j in lines.first().indices) {
            matrix[i][j] = lines[i][j]
        }
    }

    val adjList = hashMapOf<Index, MutableSet<Index>>()

    val col = arrayOf(1, 1, 1, -1, -1, -1, 0, 0)
    val row = arrayOf(0, -1, 1, 0, -1, 1, 1, -1)
    for (i in matrix.indices) {
        for (j in matrix[0].indices) {
            if (matrix[i][j] != '@') continue
            val index = Index(i, j)
            adjList[index] = mutableSetOf<Index>()

            for (k in 0 until 8) {
                val newI = i + row[k]
                val newJ = j + col[k]

                if (matrix.getOrNull(newI)?.getOrNull(newJ) == '@') {
                    val curr = adjList.getOrDefault(
                        index,  mutableSetOf<Index>()
                    )
                    curr.add(Index(newI, newJ))

                    adjList[index] = curr
                }
            }
        }
    }

    var count = 0
    val queue: Queue<Index> = LinkedList()

    val added = mutableSetOf<Index>()
    for (key in adjList.keys) {
        if (adjList[key]!!.size < 4) {
            queue.add(key)
            added.add(key)
        }
    }

//    println(adjList)
//    println(queue)

    while (queue.isNotEmpty()) {
        val rmIndex = queue.remove()
        count++

        for (adj in adjList[rmIndex].orEmpty()) {
            val curr = adjList[adj] ?: continue
            curr.remove(rmIndex)

            if (curr.size < 4 && added.contains(adj).not()) {
                queue.add(adj)
                added.add(adj)
            }

            adjList[adj] = curr
        }

        adjList.remove(rmIndex)
    }

    println(count)
}