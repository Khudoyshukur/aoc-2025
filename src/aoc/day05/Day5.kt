package aoc.day05

import java.io.File
import java.io.FileReader

fun main() {

    val file = File("src/aoc/day5/input.txt")
    file.createNewFile()
    val fileReader = FileReader(file)

    val input = fileReader.readText()
    val lines = input.split("\n")

    val ranges = mutableListOf<String>()
    val ids = mutableListOf<String>()
    var range = true
    for (line in lines) {
        if (line == "") {
            range = false
            continue
        }

        if (range) {
            ranges.add(line)
        } else {
            ids.add(line)
        }
    }

    val idRanges = ranges.map {
        val sp = it.split("-")
        LongRange(sp[0].toLong(), sp[1].toLong())
    }

    val idsLong = ids.map { it.toLong() }

    var res = 0

    for (id in idsLong) {
        for (range in idRanges) {
            if (id in range) {
                res++
                break
            }
        }
    }
    println(res)
}