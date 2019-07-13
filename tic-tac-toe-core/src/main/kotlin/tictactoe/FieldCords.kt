package tictactoe

data class FieldCords (val row: Int, val column: Int) {

    fun isCorrect(boardSize: Int) = row in 1 until boardSize && column in 1 until boardSize

}