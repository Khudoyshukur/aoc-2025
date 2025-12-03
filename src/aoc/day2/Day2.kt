package aoc.day2

import java.io.File
import java.io.FileReader

fun main() {
//    println(isInvalidId("8484902030"))
//    return

    val file = File("src/aoc/day2/input.txt")
    file.createNewFile()
    val fileReader = FileReader(file)

    val input = fileReader.readText()
    val ids = input.trim().split(",")
    var res = mutableListOf<Long>()
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

//    for (i in 0 until id.length) {
//        for (j in (i + 1) until id.length) {
//            val length = j - i
//            if (length > (id.length - j)) break
//
//            val sub1 = id.slice(i until j)
//            val sub2 = id.slice(j until (j + length))
//
//            println("$sub1 $sub2")
//
//            if (sub1 == sub2) return true
//        }
//    }

    return false
}