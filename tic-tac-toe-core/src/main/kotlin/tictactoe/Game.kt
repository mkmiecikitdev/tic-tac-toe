package tictactoe

import errorhandling.ErrorReason
import tictactoe.api.GameStateDto
import tictactoe.api.NewCounterDto
import tictactoe.api.PlayersDto


data class Game(private var players: Players, private var board: Board) {

    private var win = false

    fun tryPutCounter(newCounterDto: NewCounterDto) =
            if (win) ErrorReason.GAME_IS_OVER.toEither()
            else tryPutCounterIfGameIsNotWin(newCounterDto)

    fun getState(): GameStateDto {
        val dto = convertToDto()
        win = dto.win
        return dto
    }

    private fun tryPutCounterIfGameIsNotWin(newCounterDto: NewCounterDto) =
            board.tryPutCounter(players.currentPlayer.fieldType, newCounterDto)
                    .map { saveNewState(it, players.switch()) }
                    .map { getState() }

    private fun saveNewState(board: Board, players: Players) {
        this.board = board
        this.players = players
    }

    private fun convertToDto() =
            GameStateDto(
                    PlayersDto(players.playerCircleDto(), players.playerCrossDto()),
                    board.dto(),
                    players.currentPlayer.typeDto,
                    board.isWin(players.currentPlayer.opposite().fieldType)
            )
}