package aoc.day01

import java.io.File
import java.io.FileReader
import kotlin.math.abs

fun main() {
    val file = File("src/aoc/day1/input.txt")
    file.createNewFile()
    val fileReader = FileReader(file)

    val input = fileReader.readText()
    val inputs = input.split("\n").map { it.trim() }

    var curr = 50
    var res = 0

    for (input in inputs) {
        val from = curr
        val rotation = input.drop(1).toInt()
        when(input[0]) {
            'L' -> {
                curr -= rotation
            }
            'R' -> {
                curr += rotation
            }
        }

        if (abs(curr) % 100 == 0) {
            res++
        }

        if (from > curr) {
            for (k in (curr + 1) until from) {
                if (abs(k) % 100 == 0) {
                    res++
                }
            }
        } else {
            for (k in (from + 1) until curr) {
                if (abs(k) % 100 == 0) {
                    res++
                }
            }
        }
    }

    println(res)
}