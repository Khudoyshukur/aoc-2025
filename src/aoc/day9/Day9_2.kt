package aoc.day9

import java.io.File
import java.io.FileReader


fun main() {

    val file = File("src/aoc/day9/input.txt")
    val fileReader = FileReader(file)

    val input = fileReader.readText()
    val lines = input.split("\n")
}