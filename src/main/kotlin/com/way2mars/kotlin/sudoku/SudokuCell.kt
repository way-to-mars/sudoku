package com.way2mars.kotlin.sudoku

import kotlin.IllegalArgumentException

const val DEFAULT_SUDOKU_SIZE = 9

class SudokuCell(
    private val dimension: Int = DEFAULT_SUDOKU_SIZE,
    singleValue: Int
) {
    init{
        val allowedDimensions: List<Int> = listOf(4, 9, 16)
        if (dimension !in allowedDimensions)
            throw IllegalArgumentException("Wrong dimension argument ${dimension}, must be one of the listed: $allowedDimensions")
    }

    private var values: MutableSet<Int> = when(singleValue) {
       in 0..dimension -> mutableSetOf(singleValue)
       else -> throw IllegalArgumentException("Out of range")
    }

    constructor(dimension: Int = DEFAULT_SUDOKU_SIZE) : this(dimension, 1){
        for (i in 2..dimension)
            values.add(i)
    }

    val isFinal: Boolean
        get(){
            if (this.values.size == 1) return true
            return false
        }


    /**
     *   If otherCell contains only one values {valueToRemove}
     *   then removes it from {this.values} but only if exists here
     *   Returns 1 if removing happened
     *   otherwise returns 0
     */
    fun exclude(otherCell: SudokuCell): Int{
        val isThisFinal = this.isFinal
        val isArgumentFinal = otherCell.isFinal

        // if both are final return 0
        if (isThisFinal && isArgumentFinal) return 0

        // if both are not final return 0
        if (!isThisFinal && !isArgumentFinal) return 0

        fun excludeValue(cell: SudokuCell, valueToRemove: Int) : Int{
             if (cell.values.contains(valueToRemove)){
                 cell.values.remove(valueToRemove)
                 return 1
             }
             return 0
        }

        if (isThisFinal) return excludeValue(otherCell, this.takeFirstElement())
        return excludeValue(this, otherCell.takeFirstElement())
    }

    private fun takeFirstElement(): Int{
        return values
            .toList()[0]
    }

    override fun toString(): String {
        if (values.size == 1){
            return values.toString()
        }
        return "<${values.size}?"
    }

    fun toFullString(): String = values.toString()

}