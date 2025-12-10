package aoc.day01

import java.io.File
import java.io.FileReader

fun main() {
    val file = File("src/aoc/day01/input.txt")
    val fileReader = FileReader(file)

    val input = fileReader.readText()
    val inputs = input.split("\n").map { it.trim() }

    var curr = 50
    var res = 0

    for (input in inputs) {
        val rotation = input.drop(1).toInt() % 100
        when(input[0]) {
            'L' -> {
                if (rotation > curr) {
                    curr = (100 - (rotation - curr))
                } else {
                    curr -= rotation
                }
            }
            'R' -> {
                curr = (curr + rotation) % 100
            }
        }

        if (curr == 0) {
            res++
        }
    }

    println(res)
}