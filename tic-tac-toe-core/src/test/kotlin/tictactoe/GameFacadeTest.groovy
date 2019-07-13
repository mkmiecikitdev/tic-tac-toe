package tictactoe

import errorhandling.ErrorReason
import io.vavr.control.Either
import spock.lang.Specification
import tictactoe.api.*

class GameFacadeTest extends Specification {

    private GameFacade facade

    def setup() {
        facade = new GameModule().createFacade()
    }

    def 'should be empty players and board when game is not created'() {

        given: 'no facade'

        when: 'no action'

        then: 'there are no players and rows is empty'

        facade.getCurrentGame().isEmpty()
    }

    def 'should register players and empty board'() {

        given: 'new game'

        def newGame = createNewGame(5)

        when: 'creating new game'

        def result = facade.createNewGame(newGame)

        then: 'players are registered and rows is empty'

        result.right
        def gameState = result.get()
        def players = gameState.playersDto
        players.player1.type == PlayerTypeDto.CIRCLE
        players.player1.name == 'Name2'

        players.player2.type == PlayerTypeDto.CROSS
        players.player2.name == 'Name1'

        def board = gameState.boardDto
        board.rows.size() == 5
        board.rows
                .forEach {
            row -> checkRowHasCorrectSizeAndFieldsAreEmpty(row, 5)
        }

        gameState.currentPlayer == PlayerTypeDto.CIRCLE
        !gameState.win
    }

    def 'should return errors during creating game'() {

        when: 'Creating new game'
        def result = facade.createNewGame(newGame)

        then: 'Return error and not register players and creating board'
        result == expectedResult

        where:
        newGame                              || expectedResult
        new NewGameDto('Name', 'Name', 5)    || Either.left(ErrorReason.PLAYERS_HAVE_SAME_NAME)
        new NewGameDto('', 'Name', 5)        || Either.left(ErrorReason.PLAYER_INCORRECT_NAME)
        new NewGameDto('Name', ' ', 5)       || Either.left(ErrorReason.PLAYER_INCORRECT_NAME)
        new NewGameDto('', '', 5)            || Either.left(ErrorReason.PLAYER_INCORRECT_NAME)
        new NewGameDto('Name1', 'Name2', 2)  || Either.left(ErrorReason.BOARD_INCORRECT_SIZE)
        new NewGameDto('Name1', 'Name2', 11) || Either.left(ErrorReason.BOARD_INCORRECT_SIZE)
    }

    def 'should put circle in row 0, column 1'() {

        given: 'created new game'

        facade.createNewGame(createNewGame(5))

        when: 'put circle in row 0, field 1'

        def result = facade.putCounter(new NewCounterDto(0, 1))

        then: 'should put circle and switch players'

        result.right
        def state = result.get()
        hasCounterInCords(state, 0, 1, FieldTypeDto.CIRCLE)
        state.currentPlayer == PlayerTypeDto.CROSS
        !state.win
    }

    def 'should put cross int row 2, column 3'() {

        given: 'first player has already put cirle in row 4, column 0'

        facade.createNewGame(createNewGame(5))
        facade.putCounter(new NewCounterDto(4, 0))

        when: 'put cross in row 2, column 3'

        def result = facade.putCounter(new NewCounterDto(2, 3))

        then: 'should put cross and swtich players'

        result.right
        def state = result.get()
        hasCounterInCords(state, 4, 0, FieldTypeDto.CIRCLE)
        hasCounterInCords(state, 2, 3, FieldTypeDto.CROSS)
        state.currentPlayer == PlayerTypeDto.CIRCLE
        !state.win
    }

    def 'should win after put all circles in row'() {

        given: 'first player has two circles in one row on 3x3 board'

        facade.createNewGame(createNewGame(3))
        facade.putCounter(new NewCounterDto(1, 0))
        facade.putCounter(new NewCounterDto(0, 0))
        facade.putCounter(new NewCounterDto(1, 2))
        facade.putCounter(new NewCounterDto(0, 2))

        when: 'player put last circle'

        def result = facade.putCounter(new NewCounterDto(1, 1))

        then: 'game with circle win'

        result.right
        def state = result.get()
        state.win

    }

    def 'should win after put all circles in column'() {

        given: 'first player has two circles in one row on 3x3 board'

        facade.createNewGame(createNewGame(3))
        facade.putCounter(new NewCounterDto(1, 0))
        facade.putCounter(new NewCounterDto(1, 1))
        facade.putCounter(new NewCounterDto(2, 0))
        facade.putCounter(new NewCounterDto(1, 2))

        when: 'player put last circle'

        def result = facade.putCounter(new NewCounterDto(0, 0))

        then: 'game with circle win'

        result.right
        def state = result.get()
        state.win

    }

    def 'should win after put all cross in first diagonal'() {

        given: 'first player has two circles in one row on 3x3 board'

        facade.createNewGame(createNewGame(3))
        facade.putCounter(new NewCounterDto(1, 0))
        facade.putCounter(new NewCounterDto(1, 1))
        facade.putCounter(new NewCounterDto(0, 1))
        facade.putCounter(new NewCounterDto(2, 2))
        facade.putCounter(new NewCounterDto(2, 0))

        when: 'player put last cross'

        def result = facade.putCounter(new NewCounterDto(0, 0))

        then: 'game with cross win'

        result.right
        def state = result.get()
        state.win

    }

    def 'should win after put all cross in second diagonal'() {

        given: 'first player has two circles in one row on 3x3 board'

        facade.createNewGame(createNewGame(3))
        facade.putCounter(new NewCounterDto(1, 0))
        facade.putCounter(new NewCounterDto(1, 1))
        facade.putCounter(new NewCounterDto(0, 1))
        facade.putCounter(new NewCounterDto(0, 2))
        facade.putCounter(new NewCounterDto(0, 0))

        when: 'player put last cross'

        def result = facade.putCounter(new NewCounterDto(2, 0))

        then: 'game with cross win'

        result.right
        def state = result.get()
        state.win

    }

    def 'should return error after put cross into field with circle'() {

        given: 'first player has already put circle in row 1, column 1'

        facade.createNewGame(createNewGame(3))
        facade.putCounter(new NewCounterDto(1, 1))

        when: 'player with cross wants to put it into row 1, column 1'
        def result = facade.putCounter(new NewCounterDto(1, 1))

        then: 'return error field is not empty'
        result.left == ErrorReason.FIELD_IS_NOT_EMPTY
    }

    def 'should return error after put circle into field with circle'() {

        given: 'first player has already put circle in row 1, column 1 and second player cross in on other field'

        facade.createNewGame(createNewGame(3))
        facade.putCounter(new NewCounterDto(1, 1))
        facade.putCounter(new NewCounterDto(2, 2))

        when: 'player with circle wants to put it into row 1, column 1'
        def result = facade.putCounter(new NewCounterDto(1, 1))

        then: 'return error field is not empty'
        result.left == ErrorReason.FIELD_IS_NOT_EMPTY
    }

    def 'should return errors during put counter with incorrect field'() {

        given: 'Created game with size 7'
        facade.createNewGame(createNewGame(7))

        when: 'Player wants to put counter on incorrect field'

        def result = facade.putCounter(newCounterDto)

        then: 'Return error and not register players and creating board'
        result == expectedResult

        where:
        newCounterDto             || expectedResult
        new NewCounterDto(-1, -1) || Either.left(ErrorReason.INCORRECT_CORDS)
        new NewCounterDto(7, 7)   || Either.left(ErrorReason.INCORRECT_CORDS)
        new NewCounterDto(-3, 10) || Either.left(ErrorReason.INCORRECT_CORDS)
        new NewCounterDto(9, -2)  || Either.left(ErrorReason.INCORRECT_CORDS)
    }

    def 'should return error after put counter after win'() {

        given: 'first player win game'

        facade.createNewGame(createNewGame(3))
        facade.putCounter(new NewCounterDto(1, 0))
        facade.putCounter(new NewCounterDto(0, 0))
        facade.putCounter(new NewCounterDto(1, 2))
        facade.putCounter(new NewCounterDto(0, 2))
        facade.putCounter(new NewCounterDto(1, 1))

        when: 'player put last circle'

        def result = facade.putCounter(new NewCounterDto(2, 1))

        then: 'game with circle win'

        result.left == ErrorReason.GAME_IS_OVER

    }

    private static def createNewGame(int size) {
        return new NewGameDto('Name1', 'Name2', size)
    }

    private static def checkRowHasCorrectSizeAndFieldsAreEmpty(RowDto row, int size) {
        row.fields.size() == size
        row.fields.forEach { field -> field == FieldTypeDto.EMPTY }
    }

    private static
    def hasCounterInCords(GameStateDto state, int row, int column, FieldTypeDto fieldType) {
        return state.boardDto.rows.get(row).fields.get(column) == fieldType
    }
}
