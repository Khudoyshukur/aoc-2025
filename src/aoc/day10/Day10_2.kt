package aoc.day10

import java.io.File
import java.io.FileReader
import java.util.LinkedList
import java.util.PriorityQueue
import java.util.Queue
import com.microsoft.z3.*

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


    var res = 0
    for (machine in machines) {
//        val min = minToggles(machine)
        val min = getMinPresses(machine)

        println("For ${machine} $min")

        res += min
    }

    println(res)
}

fun minToggles(
    machine: Machine,
): Int {
//    val queue: PriorityQueue<Pair<MutableList<Int>, Int>> = PriorityQueue(
//        compareBy<Pair<MutableList<Int>, Int>> { it.second }
//    )
    val queue: Queue<Pair<MutableList<Int>, Int>> = LinkedList()
    queue.add(Pair(MutableList(machine.joltage.amounts.size) { 0 }, 0))
    val seenToggles = hashMapOf<Int, Int>()

    while (queue.isNotEmpty()) {
        println("Queue size: ${queue.size}")
        val (state, toggles) = queue.remove()
        val key = state.toIntArray().contentHashCode()
        val prev = seenToggles[key]
        if (prev != null && prev <= toggles) continue
        seenToggles[key] = toggles

        //println("state: $state $toggles")

        if (state == machine.joltage.amounts) {
            return toggles
        }

        for (button in machine.buttons) {
            var nTimes = 1
            //while (true) {
                var valid = true
                val builderState = state.toMutableList()
                //println("St: $builderState $nTimes")

                for (idx in button.indices) {
                    builderState[idx] = builderState[idx] + nTimes

                    if (builderState[idx] > machine.joltage.amounts[idx]) {
                        valid = false
                        break
                    }
                }
                //println("Ch: $builderState ${machine.joltage.indices}")

                if (valid.not()) break

                queue.add(Pair(builderState.toMutableList(), (toggles + nTimes)))
//                nTimes++
//            }
        }
    }

    return -1
}

// solution with z3 library, I am sorry ((
fun getMinPresses(machine: Machine): Int {
    val buttons = machine.buttons
    val target = machine.joltage.amounts
    val n = target.size
    val m = buttons.size

    Context().use { ctx ->
        val opt = ctx.mkOptimize()
        val presses = (0 until m).map { i ->
            ctx.mkIntConst("p$i")
        }

        presses.forEach { press ->
            opt.Add(ctx.mkGe(press, ctx.mkInt(0)))
        }

        for (counterIdx in 0 until n) {
            val affectingButtons = buttons.indices.filter { btnIdx ->
                counterIdx in buttons[btnIdx].indices
            }

            if (affectingButtons.isNotEmpty()) {
                val terms = affectingButtons.map { presses[it] }.toTypedArray()
                val sum = if (terms.size == 1) {
                    terms[0]
                } else {
                    ctx.mkAdd(*terms)
                }
                opt.Add(ctx.mkEq(sum, ctx.mkInt(target[counterIdx])))
            }
        }

        val totalPresses = if (presses.size == 1) {
            presses[0]
        } else {
            ctx.mkAdd(*presses.toTypedArray())
        }
        opt.MkMinimize(totalPresses)

        val status = opt.Check()

        return if (status == Status.SATISFIABLE) {
            val model = opt.model
            presses.sumOf { press ->
                (model.evaluate(press, true) as IntNum).int
            }
        } else {
            -1
        }
    }
}