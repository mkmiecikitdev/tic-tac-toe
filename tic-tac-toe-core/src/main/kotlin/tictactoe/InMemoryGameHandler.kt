package tictactoe

import io.vavr.control.Option

class InMemoryGameHandler(private var game: Option<Game> = Option.none()) : GameHandler {

    override fun get() = game

    override fun save(newGame: Game): Game {
        game = Option.of(newGame)
        return newGame
    }

}