package com.way2mars.kotlin.sudoku

import kotlin.math.sqrt
import java.io.File
import kotlin.contracts.contract

class SudokuTable2(dim: Dimension) {
    private val dimension = dim.getDimension()
    private val sizeOfTable = dimension * dimension
    private val minValue: Int = dim.getMinValue()
    private val maxValue: Int = dim.getMaxValue()

    // Initialize the void table. Every cell can hold any of possible values
    private val cells = Array(sizeOfTable) { SudokuCell(minValue, maxValue) }

    // Explicitly define allowed dimensions for Sudoku as a range [min, max]
    companion object {
        class Dimension(
            private val min: Int,
            private val max: Int
        ) {
            fun getDimension() = max - min + 1
            fun getMinValue() = min
            fun getMaxValue() = max
        }

        val FOUR = Dimension(1, 4)      // 1, 2, 3 ,4
        val NINE = Dimension(1, 9)      // 1, 2, 3, 4, 5, 6, 7, 8, 9
        val SIXTEEN = Dimension(0, 15)  // 0, 1, 2, ... A, B, C, D ,E, F
    }

    fun getCell(x: Int, y: Int): SudokuCell{
        if (x !in 0 until dimension || y !in 0 until dimension)
            throw IllegalArgumentException("Arguments of getCell methods are out of range")
        return cells[x*dimension+y]
    }
    fun setCellValue(x: Int, y: Int, singleValue: Int){
        if (x !in 0 until dimension || y !in 0 until dimension)
            throw IllegalArgumentException("Arguments of getCell methods are out of range")
        cells[x*dimension+y].setSingleValue(singleValue)
    }

    fun loadFromFile(dataFileName: String){
        File(dataFileName)
            .readLines()
            .withIndex()
            .filter { indexedString -> indexedString.index < dimension }
            .flatMap { indexedString ->
                val xCoordinate = indexedString.index
                indexedString.value.toCharArray().withIndex()
                    .filter { indexedChar -> indexedChar.index < dimension }
                    .map { indexedChar ->
                        val yCoordinate = indexedChar.index

                        // any non-HEX char means unknown value of the cell
                        try{
                            val cellValue = indexedChar.value.toString().toInt(radix = 16)
                            setCellValue(xCoordinate, yCoordinate, cellValue)
                        } catch (ex: NumberFormatException){
                            //println("Not a number: ${indexedChar.value}")
                        } catch (ex: IllegalArgumentException){
                            println("Illegal radix")
                        }
                    }
            }
    }

    fun ExcludingIteration(): Int{
        for (i in 0 until dimension){

        }
        return 0
    }



    override fun toString(): String {
        var result = ""
        val delimiter = sqrt(dimension.toDouble()).toInt()
        for (i in 0 until dimension){
            for (j in 0 until dimension){
                result += getCell(i, j).toString()
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