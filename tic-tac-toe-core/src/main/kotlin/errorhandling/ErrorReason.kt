package errorhandling

import io.vavr.control.Either

enum class ErrorReason {

    PLAYERS_HAVE_SAME_NAME,
    PLAYER_INCORRECT_NAME,

    BOARD_INCORRECT_SIZE,
    INCORRECT_CORDS,
    FIELD_IS_NOT_EMPTY,

    GAME_IS_OVER,
    GAME_IS_NOT_INIT;

    fun <T> toEither() = Either.left<ErrorReason, T>(this)

}

typealias Attempt<T> = Either<ErrorReason, T>

