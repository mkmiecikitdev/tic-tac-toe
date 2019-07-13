package tictactoe

import tictactoe.api.PlayerTypeDto

enum class PlayerType(val typeDto: PlayerTypeDto, val fieldType: FieldType) {

    CIRCLE(PlayerTypeDto.CIRCLE, FieldType.CIRCLE),

    CROSS(PlayerTypeDto.CROSS, FieldType.CROSS);

    fun opposite() = if (this == CIRCLE) CROSS else CIRCLE

}