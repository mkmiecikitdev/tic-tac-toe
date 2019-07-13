package tictactoe

import errorhandling.ErrorReason
import io.vavr.control.Validation
import tictactoe.api.BoardDto
import tictactoe.api.NewCounterDto

data class Board(private val fieldMap: FieldMap, private val boardSequences: BoardSequences) {

    fun dto() = BoardDto(fieldMap.dto(boardSequences.rowSequences))

    fun tryPutCounter(fieldType: FieldType, newCounterDto: NewCounterDto) =
            validate(FieldCords(newCounterDto.row, newCounterDto.column))
                    .map { fieldMap.putCounter(it, fieldType) }
                    .map { Board(it, boardSequences) }
                    .toEither()

    fun isWin(fieldType: FieldType) = fieldMap.isWin(fieldType, boardSequences.sequences)

    private fun validate(fieldCords: FieldCords) =
            fieldMap.getField(fieldCords)
                    .map {
                        if(it.isNotEmpty()) Validation.invalid<ErrorReason, FieldCords>(ErrorReason.FIELD_IS_NOT_EMPTY)
                        else Validation.valid(fieldCords)
                    }
                    .getOrElse{ Validation.invalid<ErrorReason, FieldCords>(ErrorReason.INCORRECT_CORDS) }


}