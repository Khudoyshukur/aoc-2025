package aoc.day5

import java.io.File
import java.io.FileReader
import kotlin.math.max


fun main() {

    val file = File("src/aoc/day5/input.txt")
    file.createNewFile()
    val fileReader = FileReader(file)

    val input = fileReader.readText()
    val lines = input.split("\n")

    val ranges = mutableListOf<String>()
    val ids = mutableListOf<String>()
    var range = true
    for (line in lines) {
        if (line == "") {
            range = false
            continue
        }

        if (range) {
            ranges.add(line)
        } else {
            ids.add(line)
        }
    }

    val idRanges = ranges.map {
        val sp = it.split("-")
        LongRange(sp[0].toLong(), sp[1].toLong())
    }

    val sortedRanges = idRanges.sortedWith(
        compareBy<LongRange> { it.start }.thenBy { it.endInclusive }
    )

    var curr = sortedRanges[0]
    val merged = mutableListOf<LongRange>()
    for (i in 1 until sortedRanges.size) {
        val r = sortedRanges[i]
        if (overlaps(curr, r)) {
            curr = merge(curr, r)
        } else {
            merged.add(curr)
            curr = r
        }
    }

    merged.add(curr)
    println(merged)
    println(merged.sumOf { it.endInclusive - it.start + 1 })
}

private fun overlaps(a1: LongRange, a2: LongRange): Boolean {
    return (a1.start in (a2.start..a2.endInclusive)) || (a1.endInclusive in (a2.start..a2.endInclusive)) ||
            (a2.start in (a1.start..a1.endInclusive)) || (a2.endInclusive in (a1.start..a1.endInclusive))
}

private fun merge(a1: LongRange, a2: LongRange): LongRange {
    return LongRange(
        minOf(a1.start, a2.start),
        max(a2.endInclusive, a1.endInclusive)
    )
}