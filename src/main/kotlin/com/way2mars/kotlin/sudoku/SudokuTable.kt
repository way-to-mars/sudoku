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
                           // else -> SudokuCell(dimension, Character.getNumericValue(indexedChar.value))
                            else -> SudokuCell(dimension, charToInt(indexedChar.value))
                        }
                         coordinate to cell
                        }
                    }
            .toMap()

    private fun charToInt(char: Char): Int{
        when(dimension){
            4, 9 -> return Character.getNumericValue(char)
        //    16 -> return char.toString().toInt(radix = 16)
            16 -> {
                val str = char.toString()
                val int = str.toInt(radix = 16)
                return int
            }
            else -> return char.code
        }
    }

    // check if the table is fulfilled
    private fun validateCells(): Boolean{
        if (cells.size != dimension*dimension){
            println("validateCells >> Error! Wrong dimension")
            return false
        }
        for (i in 0 until dimension)
            for (j in 0 until dimension) {
                val coordinate = Coordinate(i, j)
                if (!cells.containsKey(coordinate)) return false
            }
        println("validateCells >> Ok!")
        return true
    }

    fun solve(): Boolean{
        if (!validateCells()){
            println(this.toString())
            return false
        }

        println("Given SUDOKU is:\n$this")

        var iter = 0
        var totalExcluded = 0
        while (true){
            val countExcluded = trimIteration()
            iter++
            totalExcluded += countExcluded
            println("Iteration #$iter: $countExcluded numbers are excluded. Total #$totalExcluded numbers are excluded")
            if (countExcluded==0) break
        }
        println("The solved SUDOKU is\n$this")

        return true
    }

    fun trimIteration(): Int{
        var result = 0
        val delimiter = sqrt(dimension.toDouble()).toInt()
        for(i in 0 until dimension){
            result += trimCell { index -> Coordinate(x = i, y = index) }
            result += trimCell { index -> Coordinate(x = index, y = i) }
            // count quadrants from 0 = [0, 0]..[2, 2] to 8 = [6, 6]..[8, 8]
            result += trimCell { index ->
                val x = delimiter * (i / delimiter) + index / delimiter
                val y = delimiter * (i % delimiter) + index % delimiter
                Coordinate(x, y)
            }
        }
        return result
    }

    fun trimCell(coordinateGenerator: (Int) -> Coordinate): Int{
        var result = 0
        for( i in 0 until dimension){
            val checkedCoordinate = coordinateGenerator(i)
            val checkedValue = cells[checkedCoordinate]!!
           // if (checkedValue!!.isFinal) continue
            for(j in i+1 until dimension){
                val comparedCoordinate = coordinateGenerator(j)
                val comparedValue = cells[comparedCoordinate]!!
                result += checkedValue.exclude(comparedValue)
            }
        }
        return result  // count of excluded numbers
    }

    override fun toString(): String {
        var result = ""
        val delimiter = sqrt(dimension.toDouble()).toInt()
        for (i in 0 until dimension){
            for (j in 0 until dimension){
                val coordinate = Coordinate(i, j)
                result += cells[coordinate].toString()
                result += if (j == this.dimension-1) "\n"   // end of line
                else{
                    if (j % delimiter == delimiter-1) "  " // double space for quadrants
                    else " "
                }
            }
            if ( (i < dimension-1) && (i % delimiter == delimiter-1) ) result += "\n" // extra EOL for quadrants
        }
        return result
    }
}


