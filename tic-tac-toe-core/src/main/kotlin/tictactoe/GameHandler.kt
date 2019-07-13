package tictactoe

import io.vavr.control.Option

interface GameHandler {

    fun save(newGame: Game): Game

    fun get(): Option<Game>

}