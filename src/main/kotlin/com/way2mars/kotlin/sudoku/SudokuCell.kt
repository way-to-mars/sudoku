package com.way2mars.kotlin.sudoku


class SudokuCell(singleValue: Int) {
    private var values = mutableSetOf(singleValue)

    // Create a set of consecutive numbers [first .. last]
    constructor(first: Int, last: Int) : this(first){
        if (first >= last)
            throw IllegalArgumentException("Error in SudokuCell constructor. Second argument must be more then first argument")
        for (i in (first+1)..last)
            values.add(i)
    }

    val isFinal: Boolean
        get() = this.values.size == 1


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
            return "[x${takeFirstElement().toString(16)}]"
        }
        if (values.size < 10) return "<0${values.size}?"
        return "<${values.size}?"
    }

    fun toFullString(): String = values.toString()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SudokuCell

        if (values != other.values) return false

        return true
    }

    override fun hashCode(): Int {
        return values.hashCode()
    }

    fun setSingleValue(singleValue: Int) {
        values.clear()
        values.add(singleValue)
    }
}