package aoc.day4

import java.io.File
import java.io.FileReader


val memo = hashMapOf<Pair<Int, Int>, String>()

fun main() {

    val file = File("src/aoc/day4/input.txt")
    file.createNewFile()
    val fileReader = FileReader(file)

    val input = fileReader.readText()
}