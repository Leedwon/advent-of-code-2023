package day15

import util.readFileLines

private const val fileName = "/day15.txt"

data class Step(
    val label: String,
    val operation: Operation
)

sealed interface Operation {
    data object Remove : Operation
    data class Assign(val focalLength: Int) : Operation
}

data class Lens(
    val label: String,
    val focalLength: Int
)

fun solveDay151(fileName: String): Int {
    val input = readFileLines(fileName).first().split(",")

    return input.sumOf { hash(it) }
}

fun solveDay152(fileName: String): Int {
    val steps = readFileLines(fileName)
        .first()
        .split(",")
        .map { parseStep(it) }

    val boxes = mutableMapOf<Int, MutableList<Lens>>()

    for (step in steps) {
        val index = hash(step.label)
        val box = boxes.getOrPut(index) { mutableListOf() }
        when (step.operation) {
            is Operation.Assign -> {
                val indexToReplace = box.indexOfFirst { it.label == step.label }
                val lens = Lens(label = step.label, focalLength = step.operation.focalLength)
                if (indexToReplace != -1) {
                    box.removeAt(indexToReplace)
                    box.add(indexToReplace, lens)
                } else {
                    box.add(lens)
                }
            }

            Operation.Remove -> box.removeAll { it.label == step.label }
        }
    }

    val result = boxes.map { (key, value) ->
        (key + 1) * value.foldIndexed(0) { index, acc, lens ->
            acc + ((index + 1) * lens.focalLength)
        }
    }.sum()

    return result
}

private fun parseStep(input: String): Step {
    return if (input.contains('-')) {
        parseRemoval(input)
    } else {
        parseAssign(input)
    }
}

private fun parseRemoval(input: String): Step {
    return Step(
        label = input.dropLast(1),
        operation = Operation.Remove
    )
}

private fun parseAssign(input: String): Step {
    val (label, focalLength) = input.split("=")
    return Step(
        label = label,
        operation = Operation.Assign(focalLength.toInt())
    )
}

private fun hash(input: String): Int {
    return input.fold(0) { acc, c ->
        ((acc + c.code) * 17) % 256
    }
}

fun main() {
    println(solveDay151(fileName))
    println(solveDay152(fileName))
}
