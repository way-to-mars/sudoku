package com.way2mars.kotlin.sudoku

import java.io.File
import kotlin.math.sqrt

class SudokuTable(private val dimension: Int, fileName: String) {
    private val cells: Map<Coordinate, SudokuCell> = this.readSudokuFile(dimension, fileName)

    private fun readSudokuFile(dimension: Int, dataFileName: String) : Map<Coordinate, SudokuCell> =
        File(dataFileName)
            .readLines()
            .withIndex()
            .flatMap { indexedString ->
                val xCoordinate = indexedString.index
                indexedString.value.toCharArray().withIndex()
                    .map { indexedChar ->
                        val yCoordinate = indexedChar.index
                        val coordinate = Coordinate(xCoordinate, yCoordinate)
                        if (xCoordinate >= dimension || yCoordinate >= dimension){
                            println("Input file contains a table of a bigger size")
                            return emptyMap()
                        }
                        val cell: SudokuCell = when(indexedChar.value) {
                            '.' -> SudokuCell(dimension)
                            else -> SudokuCell(dimension, Character.getNumericValue(indexedChar.value))
                        }
                         coordinate to cell
                        }
                    }
            .toMap()

    private fun validateCells(): Boolean{
        for (i in 0 until dimension)
            for (j in 0 until dimension) {
                val coordinate = Coordinate(i, j)
                if (!cells.containsKey(coordinate)) return false
            }
        return true
    }  // check if the table is fulfilled

    fun solve(): Boolean{
        if (!validateCells()){
            println("The table is incomplete!")
            println(this.toString())
            return false
        }

        println("Given SUDOKU is:")
        println(this.toString())


        return true
    }

    fun trimCell(input: Map<Coordinate, Int>, solution: Map<Coordinate, Int>,
                       coordinateGenerator: (Int) -> Coordinate): ConditionState{
        for( i in 0 until dimension){
            val checkedCoordinate = coordinateGenerator(i)
            val checkedValue = cells[checkedCoordinate]
            if (checkedValue!!.isFinal) continue
            for(j in i+1 until dimension){
                val comparedCoordinate = coordinateGenerator(j)
                val comparedValue = cells[comparedCoordinate]!!
                checkedValue.dec(comparedValue)
            }
        }
        return ConditionState.Ok
    }

    override fun toString(): String {
        var result = ""
        val delimiter = sqrt(dimension.toDouble()).toInt()
        for (i in 0 until dimension){
            for (j in 0 until dimension){
                val coordinate = Coordinate(i, j)
                result += cells[coordinate].toString()
                if (j == this.dimension-1) result += "\n"   // end of line
                else{
                    if (j % delimiter == delimiter-1) result += "  " // double space for quadrants
                    else result += " "
                }

            }
            if ( (i < dimension-1) && (i % delimiter == delimiter-1) ) result += "\n" // extra EOL for quadrants
        }
        return result
    }
}


