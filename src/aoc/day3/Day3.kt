package aoc.day3

import java.io.File
import java.io.FileReader

fun main() {

    val file = File("src/aoc/day3/input1.txt")
    file.createNewFile()
    val fileReader = FileReader(file)

    val input = fileReader.readText()
    val lines = input.split('\n').map { it.trim() }

    var res = 0L
    for (line in lines) {
        res += findMax(line)
    }

    println(res)
}

private fun findMax(line: String): Long {
    var max = 0
    for (i in 1 until (line.length - 1)) {
        if (line[i] > line[max]) {
            max = i
        }
    }

    var first = line[max]

    var firstMaxIndex = max
    max = max + 1
    for (i in (firstMaxIndex + 1) until line.length) {
        if (line[i] > line[max]) {
            max = i
        }
    }

    val second = line[max]

    return "$first$second".toLong().also {
        println("For $line $it")
    }
}