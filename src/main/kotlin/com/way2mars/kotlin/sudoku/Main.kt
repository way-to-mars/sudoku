@file:JvmName("Main")
package com.way2mars.kotlin.sudoku

import java.io.File


fun main() {
    val input = readFile("input.txt")

    val solution = readFile("solution.txt")

    println(input)
    println(solution)
}

fun readFile(dataFileName: String) : Map<Coordinate, Int> =
    File(dataFileName)
        .readLines()
        .withIndex()
        .flatMap { indexedString ->
            val xCoordinate = indexedString.index
            indexedString.value.toCharArray().withIndex()
                .filter { indexedChar -> indexedChar.value != '.' }
                .map { indexedChar ->
                    val yCoordinate = indexedChar.index
                    val coordinate = Coordinate(xCoordinate, yCoordinate)
                    val number = Character.getNumericValue(indexedChar.value)
                    coordinate to number
                }
        }
        .toMap()


