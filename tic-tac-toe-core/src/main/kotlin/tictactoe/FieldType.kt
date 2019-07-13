package tictactoe

import tictactoe.api.FieldTypeDto

enum class FieldType(val typeDto: FieldTypeDto) {

    CROSS(FieldTypeDto.CROSS),
    CIRCLE(FieldTypeDto.CIRCLE),
    EMPTY(FieldTypeDto.EMPTY);

    fun isNotEmpty() = this != EMPTY

}