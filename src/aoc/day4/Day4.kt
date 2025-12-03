package aoc.day4

import java.io.File
import java.io.FileReader

fun main() {

    val file = File("src/aoc/day3/input1.txt")
    file.createNewFile()
    val fileReader = FileReader(file)

    val input = fileReader.readText()
}