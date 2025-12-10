package aoc.day11

import java.io.File
import java.io.FileReader

fun main() {

    val file = File("src/aoc/day11/input1.txt")
    val fileReader = FileReader(file)

    val input = fileReader.readText()
    val lines = input.split("\n")

}