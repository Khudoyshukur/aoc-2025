package aoc.day8

import java.io.File
import java.io.FileReader
import java.util.PriorityQueue
import kotlin.math.sqrt

fun main() {

    val file = File("src/aoc/day8/input.txt")
    val fileReader = FileReader(file)

    val input = fileReader.readText()
    val lines = input.split("\n")

    val coords = mutableListOf<Coor>()
    for (line in lines) {
        val sp = line.split(",")
        coords.add(
            Coor(
                x = sp[0].toDouble(),
                y = sp[1].toDouble(),
                z = sp[2].toDouble()
            )
        )
    }

    val parent = IntArray(coords.size) { it }

    fun find(x: Int): Int {
        if (parent[x] == x) return x
        parent[x] = find(parent[x])

        return parent[x]
    }
    fun union(x: Int, y: Int) {
        parent[find(x)] = find(y)
    }

    val pq = PriorityQueue<Distance>(compareBy { it.distance })

    for (i in coords.indices) {
        for (j in (i + 1) until coords.size) {
            if (i != j) {
                pq.add(
                    Distance(
                        c1 = coords[i],
                        i1 = i,
                        c2 = coords[j],
                        i2 = j,
                        distance = distance(coords[i], coords[j])
                    )
                )
            }
        }
    }

    val connectionsMade = mutableListOf<Pair<Coor, Coor>>()
    while (pq.isNotEmpty()) {
        val (c1, i1, c2, i2, distance) = pq.remove()
        println("MinDist $distance $c1 $c2 $i1 $i2")

        if (find(i1) != find(i2)) {
            //println("Connecting $c1 and $c2")
            union(i1, i2)

            connectionsMade.add(c1 to c2)
        }
    }

    val lastOne = connectionsMade.last()
    println(lastOne.first.x.toLong() * lastOne.second.x.toLong())

//    val components = hashMapOf<Int, Int>()
//    for (i in parent.indices) {
//        val p = find(i)
//        components[p] = (components[p] ?: 0) + 1
//    }
//
//    println(parent)
//    println(components)
//
//    val resList =  components.keys.sortedByDescending { components[it]!! }
//        .take(3).map { components[it]!! }
//    var res = 1L
//    resList.forEach { res *= it }
//
//    println(res)
}
