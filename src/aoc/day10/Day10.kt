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
                    queue.add(Triple(newState, (toggles + 1), button))
                }
            }
        }

        return -1
    }

    var res = 0
    for (machine in machines) {
        val min = minToggles(machine)

        res += min
    }

    println(res)
}

private fun Char.toggle(): Char {
    return if (this == '.') '#' else '.'
}