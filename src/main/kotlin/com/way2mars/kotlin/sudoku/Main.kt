@file:JvmName("Main")
package com.way2mars.kotlin.sudoku

import java.io.File


fun main() {

    val sudoku1 = SudokuTable(SudokuTable.SIXTEEN, "input_16.txt")
    sudoku1.solve()

    val sudoku2 = SudokuTable(SudokuTable.NINE, "input9x9_2.txt")
    sudoku2.solve()

    val sudoku3 = SudokuTable(SudokuTable.FOUR, "input2x2.txt")
    sudoku3.solve()

//    val a = SudokuCell(4, 12)
//    val b = SudokuCell(100)
//
//    println("${a.toFullString()} and ${b.toFullString()} ==> $a & $b")
//    b.exclude(a)
//    println("${a.toFullString()} and ${b.toFullString()} ==> $a & $b")

}

fun checkSolution(input: Map<Coordinate, Int>, solution: Map<Coordinate, Int>): ConditionState{

    fun checkCondition(input: Map<Coordinate, Int>, solution: Map<Coordinate, Int>,
                       coordinateGenerator: (Int) -> Coordinate): ConditionState{
        for( i in 0 until 9-1){
            val checkedCoordinate = coordinateGenerator(i)
            val checkedValue = input[checkedCoordinate] ?: solution[checkedCoordinate] ?: return ConditionState.Incomplete
            for(j in i+1 until 9){
                val comparedCoordinate = coordinateGenerator(j)
                val comparedValue = input[comparedCoordinate] ?: solution[comparedCoordinate] ?: return ConditionState.Incomplete
                if( checkedValue == comparedValue) return ConditionState.Duplicate
            }
        }
        return ConditionState.Ok
    }

    for(i in 0 until 9){
        val rowState = checkCondition(input, solution) { index -> Coordinate(x = i, y = index) }
        if( rowState != ConditionState.Ok) return rowState

        val columnState = checkCondition(input, solution) { index -> Coordinate(x = index, y = i) }
        if( columnState != ConditionState.Ok) return columnState

        // count quadrants from 0 = [0, 0]..[2, 2] to 8 = [6, 6]..[8, 8]
        val quadrantState = checkCondition(input, solution) { index ->
            val x = 3 * (i / 3) + index / 3
            val y = 3 * (i % 3) + index % 3
            Coordinate(x, y)
        }
        if( quadrantState != ConditionState.Ok) return quadrantState
    }
    return ConditionState.Ok
}

fun readSudokuFile(dataFileName: String) : Map<Coordinate, Int> =
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


