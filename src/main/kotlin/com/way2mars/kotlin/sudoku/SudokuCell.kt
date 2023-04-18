package com.way2mars.kotlin.sudoku

import kotlin.IllegalArgumentException

const val DEFAULT_SUDOKU_SIZE = 9

class SudokuCell(
    private val dimension: Int = DEFAULT_SUDOKU_SIZE,
    singleValue: Int
) {
    init{
        val allowedDimensions = (2..16).map{ it*it }
        if (dimension !in allowedDimensions)
            throw IllegalArgumentException("Wrong dimension argument ${dimension}, must be one of the listed: $allowedDimensions")
    }

    private var values: MutableSet<Int> = when(singleValue) {
       in 1..dimension -> mutableSetOf(singleValue)
       else -> throw IllegalArgumentException("Out of range")
    }

    constructor(dimension: Int = DEFAULT_SUDOKU_SIZE) : this(dimension, 1){
        for (i in 2..dimension)
            values.add(i)
    }

    val isFinal: Boolean = values.size == 1

    fun dec(otherCell: SudokuCell){
        if (otherCell.isFinal){
            this.values.remove(otherCell.takeFirstElement())
        }
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

    fun toFullString(): String{
        return values.toString()
    }
}