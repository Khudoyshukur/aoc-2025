package aoc.day11

import java.io.File
import java.io.FileReader
import java.util.LinkedList
import java.util.Queue

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

    val memo = hashMapOf<Triple<String, Boolean, Boolean>, Long>()
    fun paths(node: String, dac: Boolean, fft: Boolean, visit: MutableSet<String>): Long {
        val key = Triple(node, dac, fft)
        if (memo.contains(key)) return memo[key]!!
        if (visit.contains(node)) return 0L
        if (node == "out") {
            if (!dac || !fft) return 0L

            return 1L
        }
        visit.add(node)

        val res = adj[node].orEmpty().sumOf {
            paths(
                node = it,
                dac = if (dac) true else (node == "dac"),
                fft = if (fft) true else (node == "fft"),
                visit = visit
            )
        }

        visit.remove(node)

        return res.also {
            memo[key] = it
        }
    }

    println(paths("svr", false, false, mutableSetOf()))
}