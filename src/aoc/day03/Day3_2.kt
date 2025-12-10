package aoc.day03

import java.io.File
import java.io.FileReader


val memo = hashMapOf<Pair<Int, Int>, String>()

fun main() {
    val file = File("src/aoc/day03/input.txt")
    val fileReader = FileReader(file)

    val input = fileReader.readText()
    val lines = input.split('\n').map { it.trim() }

    var res = 0L
    for (line in lines) {
        memo.clear()
        res += maxSubsequence(
            0, line, 12
        ).toLong()
    }

    println(res)
}

private fun maxSubsequence(
    index: Int,
    line: String,
    size: Int,
): String {
    val key = index to size
    if (memo[key] != null) return memo[key]!!
    if (size == 0) return ""
    if (index >= line.length) {
        return "null"
    }

    val notInclude = maxSubsequence(
        index + 1,
        line,
        size
    )

    // include
    val next = maxSubsequence(
        index + 1,
        line,
        size - 1
    )
    if (next == "null") return notInclude
    val include = line[index] + next

    if (notInclude == "null") {
        return include
    }

    return maxOf(include, notInclude).also {
        memo[key] = it
    }
}