package tictactoe

import tictactoe.api.PlayerDto

data class Player(private val name: String, private val type: PlayerType) {

    fun dto() = PlayerDto(name, type.typeDto)

}