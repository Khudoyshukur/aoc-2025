package aoc.day12

import java.io.File
import java.io.FileReader

val file = File("src/aoc/day12/input.txt")

data class Shape(
    val shape: List<List<Char>>
)

fun Shape.print() {
    println()
    for (i in this.shape.indices) {
        for (j in this.shape[0].indices) {
            print("${this.shape[i][j]}\t")
        }
        println()
    }
    println()
}

fun Shape.rotateRight(): Shape {
    val newShape = MutableList(shape.size) { MutableList(shape[0].size) { '.' } }

    for (i in this.shape.indices) {
        for (j in this.shape[0].indices) {
            newShape[j][newShape.lastIndex - i] = this.shape[i][j]
        }
    }

    return Shape(shape = newShape)
}

fun Shape.flipVertical(): Shape {
    val newShape = MutableList(shape.size) { MutableList(shape[0].size) { '.' } }

    for (i in newShape.indices) {
        for (j in newShape[0].indices) {
            newShape[i][j] = this.shape[this.shape.lastIndex - i][j]
        }
    }

    return Shape(shape = newShape)
}

fun Shape.flipHorizontal(): Shape {
    val newShape = MutableList(shape.size) { MutableList(shape[0].size) { '.' } }

    for (i in newShape.indices) {
        for (j in newShape[0].indices) {
            newShape[i][j] = this.shape[i][this.shape[0].lastIndex - j]
        }
    }

    return Shape(shape = newShape)
}

fun parseShapes(): List<Shape> {
    val fileReader = FileReader(file)

    val input = fileReader.readText()
    val lines = input.split("\n")

    var iter = 0

    val shapes = mutableListOf<Shape>()
    while (iter < lines.size) {
        if (lines[iter].substringBefore(":").toIntOrNull() == null) break

        iter++
        val matrix = mutableListOf<String>()
        while (lines[iter].contains(":").not()) {
            val line = lines[iter++]
            if (line.isNotBlank()) {
                matrix.add(line)
            }
        }

        val shape = MutableList(matrix.size) { MutableList(matrix[0].length) { '.' } }
        for (i in matrix.indices) {
            for (j in matrix[0].indices) {
                shape[i][j] = matrix[i][j]
            }
        }
        shapes.add(Shape(shape = shape))
    }

    return shapes
}

fun Shape.manipulate(): List<Shape> {
    val rotations = mutableListOf(this)

    // here i will add rotated versions of the shape
    repeat(3) {
        rotations.add(
            rotations.last().rotateRight()
        )
    }

    // after rotation, shapes might also be flipped
    val flipped = mutableListOf<Shape>()
    for (rotation in rotations) {
        flipped.add(rotation.flipVertical())
        flipped.add(rotation.flipHorizontal())
    }

    return (rotations + flipped).toSet().toList()
}

fun List<Shape>.getManipulatedShapesList(): List<List<Shape>> {
    return this.map { it.manipulate() }
}


data class Polygon(
    val height: Int,
    val width: Int,
    val requiredShapesCount: List<Int>
)

fun parsePolygons(): List<Polygon> {
    val fileReader = FileReader(file)

    val input = fileReader.readText()
    val lines = input.split("\n")

    var iter = 0
    while (lines[iter].contains(":").not() || lines[iter].substringBefore(":").toIntOrNull() != null) {
        iter++
    }

    val polygons = mutableListOf<Polygon>()
    while (iter < lines.size) {
        val line = lines[iter++]
        val wh = line.substringBefore(":").trim().split("x")
        val w = wh[0].toInt()
        val h = wh[1].toInt()

        val requires = line.substringAfter(":").trim().split(" ").map { it.toInt() }

        val p = Polygon(
            width = w,
            height = h,
            requiredShapesCount = requires
        )
        polygons.add(p)
    }

    return polygons
}

fun main() {
    val shapes = parseShapes()
    val manipulatedShapes = shapes.getManipulatedShapesList()
//    for (shps in manipulatedShapes) {
//        println("*******************************")
//        shps.forEach { it.print() }
//    }

    val polygons = parsePolygons()
//    println(polygons)

    var res = 0
    for (i in polygons.indices) {
        println()
        print("Checking polygon $i/${polygons.lastIndex}")
        val polygon = polygons[i]
        if (isPolygonValidV2(polygon, manipulatedShapes)) {
            print(" -> true")
            res++
        } else {
            print(" -> false")
        }
    }
    println()

    println("Valid polygons: $res")
}

fun isPolygonValid(polygon: Polygon, manipulatedShapes: List<List<Shape>>): Boolean {
    val polygonMatrix = MutableList(polygon.height) { MutableList(polygon.width) { '.' } }
//    val memo = hashMapOf<Pair<Int, Int>, Boolean>()

    var totalArea = 0
    for (i in polygon.requiredShapesCount.indices) {
        val shapeArea = manipulatedShapes[i][0].shape.sumOf { row -> row.count { it == '#' } }
        totalArea += shapeArea * polygon.requiredShapesCount[i]
    }
    if (totalArea > polygon.width * polygon.height) return false

    fun fillShape(
        requirements: MutableList<Int>,
        matrix: MutableList<MutableList<Char>>,
    ): Boolean {
        val reqIndex = requirements.indexOfFirst { it > 0 }
        if (reqIndex == -1) return true

//        val key = Pair(
//            requirements.toIntArray().contentHashCode(),
//            matrixHash(matrix)
//        )
        //if (memo[key] != null) return memo[key]!!

        val shapes = manipulatedShapes[reqIndex]

        var anyValid = false

//        var firstRow = -1
//        var firstCol = -1
//        outer@ for (i in 0 until matrix.size.minus(2)) {
//            for (j in 0 until matrix[0].size.minus(2)) {
//                if (matrix[i][j] == '.') {
//                    firstRow = i
//                    firstCol = j
//                    break@outer
//                }
//            }
//        }
//
//        if (firstRow == -1) return false
//
//        for (shape in shapes) {
//            if (matrix.canFillWith(firstRow, firstCol, shape)) {
//                matrix.fillWith(firstRow, firstCol, shape)
//                requirements[reqIndex]--
//
//                if (fillShape(requirements, matrix)) {
//                    anyValid = true
//                    break
//                }
//
//                requirements[reqIndex]++
//                matrix.unfill(firstRow, firstCol, shape)
//            }
//        }

        for (i in matrix.indices) {
            for (j in matrix[0].indices) {
                if (matrix[i][j] != '.') continue

                for (shape in shapes) {
                    if (matrix.canFillWith(i, j, shape)) {
                        matrix.fillWith(i, j, shape)
                        requirements[reqIndex]--

                        if (fillShape(requirements, matrix)) {
                            anyValid = true
                            break
                        }

                        requirements[reqIndex]++
                        matrix.unfill(i, j, shape)
                    }
                }

                if (anyValid) break
            }
            if (anyValid) break
        }

        return anyValid.also {
            //memo[key] = it
        }
    }

    return fillShape(
        requirements = polygon.requiredShapesCount.toMutableList(),
        matrix =  polygonMatrix,
    )
}

fun MutableList<MutableList<Char>>.canFillWith(startI: Int, startJ: Int, shape: Shape): Boolean {
    val height = this.size
    val width = this[0].size
    if (height - startI < shape.shape.size) return false
    if (width - startJ < shape.shape[0].size) return false

    for (i in shape.shape.indices) {
        for (j in shape.shape[0].indices) {
            if (shape.shape[i][j] == '.') continue

            if (this[startI + i][startJ + j] != '.') return false
        }
    }

    return true
}

fun MutableList<MutableList<Char>>.fillWith(startI: Int, startJ: Int, shape: Shape) {
    for (i in shape.shape.indices) {
        for (j in shape.shape[0].indices) {
            if (shape.shape[i][j] == '.') continue

            this[startI + i][startJ + j] = shape.shape[i][j]
        }
    }
}

fun MutableList<MutableList<Char>>.unfill(startI: Int, startJ: Int, shape: Shape) {
    for (i in shape.shape.indices) {
        for (j in shape.shape[0].indices) {
            if (shape.shape[i][j] == '.') continue

            this[startI + i][startJ + j] = '.'
        }
    }
}

// in reddit, people recommending to try only area checking. But it is not correct based on my intuition
// let's see, maybe gives a right solution
private fun isPolygonValidV2(polygon: Polygon, manipulatedShapes: List<List<Shape>>): Boolean {
    var totalArea = 0
    for (i in polygon.requiredShapesCount.indices) {
        val shapeArea = manipulatedShapes[i][0].shape.sumOf { row -> row.count { it == '#' } }
        totalArea += shapeArea * polygon.requiredShapesCount[i]
    }

    return totalArea < polygon.width * polygon.height
}