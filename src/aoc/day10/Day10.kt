package aoc.day10

import java.io.File
import java.io.FileReader
import java.util.LinkedList
import java.util.Queue

data class Button(
    val indices: List<Int>
)

data class Joltage(
    val amounts: MutableList<Int>
)

data class Machine(
    val lights: String,
    val buttons: List<Button>,
    val joltage: Joltage
)

fun main() {

    val file = File("src/aoc/day10/input.txt")
    val fileReader = FileReader(file)

    val input = fileReader.readText()
    val lines = input.split("\n")

    val machines = mutableListOf<Machine>()
    for (line in lines) {
        val sp = line.split(" ")
        machines.add(
            Machine(
                lights = sp[0].trim().removePrefix("[").removeSuffix("]"),
                joltage = sp.last().trim().removePrefix("{")
                    .removeSuffix("}").split(",").map { it.toInt() }
                    .let { Joltage(it.toMutableList()) },
                buttons = sp.drop(1).dropLast(1).map {
                    it.trim().removePrefix("(").removeSuffix(")")
                        .split(",").map { it.toInt() }.let { Button(it) }
                }
            )
        )
    }

    fun minToggles(
        machine: Machine,
    ): Int {
        val queue: Queue<Triple<String, Int, Button?>> = LinkedList()
        queue.add(Triple(String(CharArray(machine.lights.length){'.'}), 0, null))
        val seenToggles = hashMapOf<String, Int>()

        while (queue.isNotEmpty()) {
            val (state, toggles, prevButton) = queue.remove()
            if (seenToggles[state] == null) {
                seenToggles[state] = toggles
            } else {
                if (seenToggles[state]!! >= toggles) continue
                seenToggles[state] = toggles
            }

            //println("state: $state $toggles $prevButton")

            if (state == machine.lights) {
                return toggles
            }

            machine.buttons.forEach { button ->
                val builderState = state.toCharArray()
                if (button != prevButton) {
                    button.indices.forEach { idx ->
                        builderState[idx] = builderState[idx].toggle()
                    }

                    val newState = String(builderState)
                    //println("After $button $newState")
                    queue.add(Triple(newState, (toggles + 1), button))
                }
            }
        }

        return -1
    }

    fun calculateMinToggles(
        machine: Machine,
        currState: StringBuilder,
        seenStates: MutableSet<String>,
        buttonClicks: MutableList<Button>
    ): Int {
        val currStateStr = currState.toString()
        println("$currStateStr with $buttonClicks")
        if (currStateStr == machine.lights) {
            return 0
        }

//        for (i in 2 until buttonClicks.size) {
//            val chunks = buttonClicks.chunked(i)
//            if (chunks.size >= 2 && chunks[chunks.lastIndex] == chunks[chunks.lastIndex - 1]) return -1
//        }

//        if (buttonClicks.size > 0 && buttonClicks.size % 2 == 0) {
//            val chunks = buttonClicks.chunked(buttonClicks.size/2)
//            if (chunks[0] == chunks[1]) return -1
//        }

        if (seenStates.contains(currStateStr)) {
            //println("State in seen $currStateStr")
            return -1
        }
        seenStates.add(currStateStr)

        var min = -1
        machine.buttons.forEach { button ->
            button.indices.forEach { idx ->
                currState[idx] = currState[idx].toggle()
            }
            buttonClicks.add(button)

            val toggles = calculateMinToggles(
                machine = machine,
                currState = currState,
                seenStates = seenStates,
                buttonClicks = buttonClicks
            )
            if (toggles != -1) {
                if (min == -1) {
                    min = toggles + 1
                } else {
                    min = minOf(min, toggles + 1)
                }
            }

            button.indices.forEach { idx ->
                currState[idx] = currState[idx].toggle()
            }
            buttonClicks.removeLast()
        }

        return min
    }

    var res = 0
    for (machine in machines) {
//        val min = calculateMinToggles(
//            machine = machine,
//            currState = StringBuilder().apply {
//                repeat(machine.lights.length) {
//                    append(".")
//                }
//            },
//            seenStates = mutableSetOf(),
//            buttonClicks = mutableListOf()
//        )
        val min = minToggles(machine)

        println("For ${machine.lights} $min")

        res += min
    }

    println(res)
}

private fun Char.toggle(): Char {
    return if (this == '.') '#' else '.'
}