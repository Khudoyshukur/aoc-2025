package aoc.day03

import java.io.File
import java.io.FileReader


val memo = hashMapOf<Pair<Int, Int>, String>()

fun main() {

    val file = File("src/aoc/day3/input.txt")
    file.createNewFile()
    val fileReader = FileReader(file)

    val input = fileReader.readText()
    val lines = input.split('\n').map { it.trim() }

    var res = 0L
    for (line in lines) {
        memo.clear()
        res += maxSubsequence(
            0, line, 12
        ).toLong()

        println(memo)
    }

    println(res)
}

private fun maxSubsequence(
    index: Int,
    line: String,
    curr: StringBuilder,
    max: Array<String>
) {
    if (curr.length == 12) {
        if (max[0] < curr.toString()) {
            max[0] = curr.toString()
        }
        return
    }
    if (index >= line.length) return

    // include
    curr.append(line[index])
    maxSubsequence(
        index + 1,
        line,
        curr = curr,
        max = max
    )
    curr.deleteAt(curr.lastIndex)

    // not include
    maxSubsequence(
        index + 1,
        line,
        curr,
        max
    )
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