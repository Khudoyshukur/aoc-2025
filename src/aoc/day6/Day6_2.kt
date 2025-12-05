package aoc.day6

import java.io.File
import java.io.FileReader


fun main() {

    val file = File("src/aoc/day6/input.txt")
    file.createNewFile()
    val fileReader = FileReader(file)

    val input = fileReader.readText()
    val lines = input.split("\n")
}