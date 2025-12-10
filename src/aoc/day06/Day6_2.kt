package aoc.day06

import java.io.File
import java.io.FileReader

fun main() {

    val file = File("src/aoc/day06/input.txt")
    val fileReader = FileReader(file)

    val input = fileReader.readText()
    val lines = input.split("\n")

    val iterators = MutableList(lines.size) { 0 }
    var globalRes = 0L

    while (true) {
        while (true) {
            for (index in 0 until iterators.size) {
                if (iterators[index] < lines[index].length && lines[index][iterators[index]] == ' ') {
                    iterators[index]++
                }
            }

            var allDigit = true
            for (index in iterators.indices) {
                if (iterators[index] < lines[index].length && lines[index].getOrNull(iterators[index]) == ' ') {
                    allDigit = false
                }
            }

            if (allDigit) break
        }


        if (lines.isEmpty()) continue
        val ops = lines.last().getOrNull(iterators[iterators.size - 1]) ?: break
        var res = if (ops == '*') 1L else 0L
        val nums = mutableListOf<String>()

        while (true) {
            var hasDigit = false
            for (index in iterators.dropLast(1).indices) {
                if (lines[index].getOrNull(iterators[index])?.isDigit() == true) {
                    hasDigit = true
                    break
                }
            }
            if (hasDigit.not()) {
                iterators[iterators.lastIndex]++
                break
            }

            val minIteratorIndex = iterators.dropLast(1).min()
            val num = StringBuilder("")
            for (index in iterators.indices) {
                if (iterators[index] == minIteratorIndex) {
                    if (lines[index].getOrNull(iterators[index])?.isDigit() == true) {
                        num.append(lines[index][iterators[index]])
                    }
                    iterators[index]++
                }
            }
            nums.add((num.toString()))
        }
        if (nums.isNotEmpty()) {
            if (ops == '*') {
                var curr = 1L
                nums.map { it.toLong() }.forEach { curr *= it }
                res = curr
            } else {
                res += nums.sumOf { it.toLong() }
            }
            globalRes += res
        }

        println("$nums res: $res global: $globalRes op: $ops")
    }
}