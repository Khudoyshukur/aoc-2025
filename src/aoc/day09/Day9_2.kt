package aoc.day09

import java.io.File
import java.io.FileReader
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {

    val file = File("src/aoc/day09/input.txt")
    val fileReader = FileReader(file)

    val input = fileReader.readText()
    val lines = input.split("\n")
    val coords = mutableListOf<Coor>()

    for (line in lines) {
        val sp = line.split(",")
        coords.add(
            Coor(
                i = sp[1].toInt(),
                j = sp[0].toInt()
            )
        )
    }
    val coordsSet = coords.toSet()

    val greenCoords = mutableSetOf<Coor>()

    val iCoords = hashMapOf<Int, MutableList<Coor>>()
    val jCoords = hashMapOf<Int, MutableList<Coor>>()

    for (c in coords) {
        iCoords[c.i] = (iCoords[c.i] ?: mutableListOf()).also {
            it.add(c)
        }

        jCoords[c.j] = (jCoords[c.j] ?: mutableListOf()).also {
            it.add(c)
        }
    }

    val connectedComponents = mutableListOf<List<Coor>>()
    val seenIndices = mutableSetOf<Coor>()
    fun dfs(
        visited: MutableSet<Coor>,
        index: Coor,
        path: MutableList<Coor>,
        prev: Coor? = null
    ) {
        seenIndices.add(index)
        if (visited.contains(index)) {
            // connected component
            connectedComponents.add(path.toList())
            return
        }
        visited.add(index)

        val adj = mutableListOf<Coor>()
        for (a in iCoords[index.i]!!) {
            if (a != index) {
                adj.add(a)
            }
        }

        for (a in jCoords[index.j]!!) {
            if (a != index) {
                adj.add(a)
            }
        }

        for (a in adj) {
            if (prev != a && (coordsSet.contains(a))) {
                path.add(a)
                dfs(visited, a, path, prev = index)
                path.removeLast()
            }
        }

        visited.remove(index)
    }

    for (c in coords) {
        if (seenIndices.contains(c).not()) {
            dfs(visited = mutableSetOf(), index = c, prev = null, path = mutableListOf(c))
        }
    }

    println("Flagging green...")
    greenCoords.addAll(coords)
    val validPairs = HashMap<Int, MutableSet<Pair<Int, Int>>>()
    val validColPairs = HashMap<Int, MutableSet<Pair<Int, Int>>>()
    for (component in connectedComponents) {
        fillGreenFast(component, greenCoords)

        val rowFullMap = hashMapOf<Int, MutableList<Coor>>()
        for (c in greenCoords) {
            rowFullMap[c.i] = (rowFullMap[c.i] ?: mutableListOf()).also {
                it.add(c)
            }
        }

        for ((row, rowCoords) in rowFullMap) {
            val minJ = rowCoords.minBy { it.j }.j
            val maxJ = rowCoords.maxBy { it.j }.j

            validPairs[row] = (validPairs[row] ?: mutableSetOf()).also {
                it.add(minJ to maxJ)
            }
        }

        val collFullMap = hashMapOf<Int, MutableList<Coor>>()
        for (c in greenCoords) {
            collFullMap[c.j] = (collFullMap[c.j] ?: mutableListOf()).also {
                it.add(c)
            }
        }

        for ((col, colCoords) in collFullMap) {
            val minI = colCoords.minBy { it.i }.i
            val maxI = colCoords.maxBy { it.i }.i

            validColPairs[col] = (validColPairs[col] ?: mutableSetOf()).also {
                it.add(minI to maxI)
            }
        }
    }

    // FOR VISUALIZING
//    val matrix = Array(
//        coords.maxBy { it.i }.i + 1
//    ) {
//        CharArray(
//            coords.maxBy { it.j }.j + 1
//        ) { '.' }
//    }
//    println()
//    for (i in matrix.indices) {
//        for (j in matrix[0].indices) {
//            val ch =
//                if (coordsSet.contains(Coor(i, j))) {
//                'O'
//            } else
//                if (greenCoords.contains(Coor(i, j))) {
//                'X'
//            } else matrix[i][j]
//
//            print("${ch} \t")
//        }
//        println()
//    }

    println("Calculating largest...")
    var largest = 0L

    for (i in coords.indices) {
        for (j in (i + 1) until coords.size) {
            val c1 = coords[i]
            val c2 = coords[j]

            val topRow = min(c1.i, c2.i)
            val bottomRow = max(c1.i, c2.i)
            val leftCol = min(c1.j, c2.j)
            val rightCol = max(c1.j, c2.j)

            var valid = true

            for (iK in topRow..bottomRow) {
                val rowRanges = validPairs[iK].orEmpty()
                if (rowRanges.none {
                        leftCol in it.first..it.second && rightCol in it.first..it.second
                    }) {
                    valid = false
                    break
                }
            }

            for (jK in leftCol..rightCol) {
                val colRanges = validColPairs[jK].orEmpty()

                if (colRanges.none {
                        topRow in it.first..it.second && bottomRow in it.first..it.second
                    }) {
                    valid = false
                    break
                }
            }

            if (valid) {
                val height = abs(c1.i - c2.i) + 1
                val width = abs(c1.j - c2.j) + 1
                val area = height.toLong() * width.toLong()

                largest = max(area, largest)
            }
        }
    }

    println()
    println(largest)
}

fun fillGreenFast(component: List<Coor>, green: MutableSet<Coor>) {

    val rows = component.groupBy { it.i }
    val cols = component.groupBy { it.j }

    for ((row, list) in rows) {
        val minJ = list.minOf { it.j }
        val maxJ = list.maxOf { it.j }
        for (j in minJ..maxJ) {
            val c = Coor(row, j)
            if (!component.contains(c)) green.add(c)
        }
    }

    for ((col, list) in cols) {
        val minI = list.minOf { it.i }
        val maxI = list.maxOf { it.i }
        for (i in minI..maxI) {
            val c = Coor(i, col)
            if (!component.contains(c)) green.add(c)
        }
    }
}
