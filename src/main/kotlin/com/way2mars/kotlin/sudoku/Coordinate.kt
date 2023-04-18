package com.way2mars.kotlin.sudoku

class Coordinate(
    val x: Int,
    val y: Int
){
    override fun toString(): String {
        return "($x, $y)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Coordinate

        if (x != other.x) return false
        return y == other.y
    }

    override fun hashCode(): Int {
        return 10*x + y
    }
}