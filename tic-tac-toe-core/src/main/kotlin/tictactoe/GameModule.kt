package tictactoe

import java.util.*

class GameModule {

    fun createFacade(gameHandler: GameHandler) = GameFacade(GameCreator(BoardSequencesCreator(), realRandomPlayerTypeGenerator), gameHandler)

    fun createFacade() = GameFacade(GameCreator(BoardSequencesCreator(), fixedRandomPlayerTypeGenerator), InMemoryGameHandler())

    companion object {

        private val realRandomPlayerTypeGenerator = { if (Random().nextBoolean()) PlayerType.CROSS else PlayerType.CIRCLE }

        private val fixedRandomPlayerTypeGenerator = { PlayerType.CROSS }

    }

}