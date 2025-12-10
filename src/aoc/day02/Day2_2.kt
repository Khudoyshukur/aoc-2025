package aoc.day02

import java.io.File
import java.io.FileReader

fun main() {
    val file = File("src/aoc/day02/input.txt")
    val fileReader = FileReader(file)

    val input = fileReader.readText()
    val ids = input.trim().split(",")
    val res = mutableListOf<Long>()
    for (idRange in ids) {
        val range = idRange.trim().split("-")
        val from = range[0].toLong()
        val to = range[1].toLong()

        for (k in from..to) {
            if (isInvalidId(k.toString())) {
                res.add(k)
            }
        }
    }

    println(res.sum())
}

private fun isInvalidId(id: String): Boolean {
    if (id.isBlank()) return true
    if (id[0] == '0') return true

    for (k in 2..id.length) {
        if (id.length % k != 0) continue
        val chunked = id.chunked(id.length / k)

        if (chunked.toSet().size == 1) return true
    }

    return false
}