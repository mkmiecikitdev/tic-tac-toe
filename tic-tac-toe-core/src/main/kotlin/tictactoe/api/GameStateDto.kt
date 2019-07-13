package tictactoe.api


data class GameStateDto(val playersDto: PlayersDto, val boardDto: BoardDto, val currentPlayer: PlayerTypeDto, val win: Boolean)