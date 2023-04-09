@file:JvmName("Main")
package com.way2mars.kotlin.sudoku

import java.io.File
import javax.xml.stream.events.Characters

fun main() {
    val input = readInput()

    print(input)
}

fun readInput() : Map<Pair<Int, Int>, Int> {
    val input = File("input.txt")
        .readLines()
        .withIndex()
        .flatMap { indexedValue ->
            val xCoordinate = indexedValue.index
            indexedValue.value.toCharArray().withIndex()
                .filter { indexedChar -> indexedChar.value != '.' }
                .map { indexedChar ->
                    val yCoordinate = indexedChar.index
                    val number = Character.getNumericValue(indexedChar.value)
                    (xCoordinate to yCoordinate) to number
                }
        }
        .toMap()

    return input
}