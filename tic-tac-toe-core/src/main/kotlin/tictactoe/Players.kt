package tictactoe

import io.vavr.collection.Map

data class Players(private val players: Map<PlayerType, Player>, val currentPlayer: PlayerType) {

    fun playerCircleDto() = players.get(PlayerType.CIRCLE)
            .map { it.dto() }
            .orNull!!

    fun playerCrossDto() = players.get(PlayerType.CROSS)
            .map { it.dto() }
            .orNull!!

    fun switch() = Players(players, currentPlayer.opposite())

}