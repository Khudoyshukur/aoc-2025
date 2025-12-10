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

    if (id.length % 2 == 0) {
        if (id.slice(0 until id.length.div(2)) == id.slice(id.length.div(2) until id.length)) {
            return true
        }
    }

    return false
}