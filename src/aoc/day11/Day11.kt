package aoc.day11

import java.io.File
import java.io.FileReader

fun main() {

    val file = File("src/aoc/day11/input.txt")
    val fileReader = FileReader(file)

    val input = fileReader.readText()
    val lines = input.split("\n")

    val adj = hashMapOf<String, MutableList<String>>()
    for (line in lines) {
        val root = line.substringBefore(":")
        val paths = line.substringAfter(":").trim().split(" ")

        adj[root] = (adj[root] ?: mutableListOf()).also {
            it.addAll(paths)
        }
    }

    fun paths(node: String, visit: MutableSet<String>): Long {
        if (visit.contains(node)) return 0L
        if (node == "out") return 1L
        visit.add(node)

        val res = adj[node].orEmpty().sumOf {
            paths(it, visit)
        }

        visit.remove(node)

        return res
    }

    println(paths("you", mutableSetOf()))
}