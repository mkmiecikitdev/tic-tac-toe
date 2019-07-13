package tictactoe

import errorhandling.ErrorReason
import io.vavr.Tuple
import io.vavr.Tuple2
import io.vavr.collection.HashMap
import io.vavr.collection.Stream
import io.vavr.control.Validation
import io.vavr.control.Validation.invalid
import io.vavr.control.Validation.valid
import tictactoe.api.NewGameDto

class GameCreator(private val boardSequencesCreator: BoardSequencesCreator, private val randomPlayerTypeGenerator: () -> PlayerType) {

    fun create(newGameDto: NewGameDto) =
            Validation.combine(
                    validNames(newGameDto.player1Name, newGameDto.player2Name),
                    validBoardSize(newGameDto.boardSize)
            )
                    .ap { names, size -> createGame(names, size, randomPlayerTypeGenerator.invoke()) }
                    .mapError { it.first() }
                    .toEither()!!


    private fun createGame(names: Tuple2<String, String>, size: Int, firstPlayerType: PlayerType) =
            Game(
                    createPlayers(names, firstPlayerType),
                    createEmptyBoard(size)
            )

    private fun createPlayers(names: Tuple2<String, String>, firstPlayerType: PlayerType) =
            Players(
                    HashMap.of(
                            firstPlayerType, Player(names._1(), firstPlayerType),
                            firstPlayerType.opposite(), Player(names._2(), firstPlayerType.opposite())
                    ),
                    PlayerType.CIRCLE
            )

    private fun createEmptyBoard(size: Int) = Board(FieldMap(createFieldMap(size)), boardSequencesCreator.create(size))

    private fun validNames(name1: String, name2: String): Validation<ErrorReason, Tuple2<String, String>> {
        if (name1.isBlank() || name2.isBlank()) return invalid(ErrorReason.PLAYER_INCORRECT_NAME)
        if (name1 == name2) return invalid(ErrorReason.PLAYERS_HAVE_SAME_NAME)
        return valid(Tuple.of(name1, name2))
    }

    private fun validBoardSize(size: Int): Validation<ErrorReason, Int> =
            if (size < 3 || size > 10) invalid(ErrorReason.BOARD_INCORRECT_SIZE)
            else valid(size)

    private fun createFieldMap(size: Int) =
            Stream.ofAll(0 until size)
                    .flatMap { row ->
                        Stream.ofAll(0 until size)
                                .map { column ->
                                    FieldCords(row, column)
                                }

                    }
                    .toMap { Tuple.of(it, FieldType.EMPTY) }

}