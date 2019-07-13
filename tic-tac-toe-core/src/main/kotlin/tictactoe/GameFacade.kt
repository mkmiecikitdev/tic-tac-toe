package tictactoe

import errorhandling.ErrorReason
import tictactoe.api.NewCounterDto
import tictactoe.api.NewGameDto

class GameFacade(private val gameCreator: GameCreator, private val gameHandler: GameHandler) {

    fun createNewGame(newGameDto: NewGameDto) =
            gameCreator.create(newGameDto)
                    .map { gameHandler.save(it) }
                    .map { it.getState() }!!

    fun getCurrentGame() = gameHandler.get()

    fun putCounter(newCounterDto: NewCounterDto) =
            gameHandler.get()
                    .toEither(ErrorReason.GAME_IS_NOT_INIT)
                    .flatMap { it.tryPutCounter(newCounterDto) }

}