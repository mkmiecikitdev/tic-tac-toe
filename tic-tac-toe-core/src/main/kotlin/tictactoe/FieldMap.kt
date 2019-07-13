package tictactoe

import io.vavr.collection.List
import io.vavr.collection.Map
import tictactoe.api.RowDto

data class FieldMap(private val fieldMap: Map<FieldCords, FieldType>) {

    fun putCounter(fieldCords: FieldCords, fieldType: FieldType) = FieldMap(fieldMap.put(fieldCords, fieldType))

    fun getField(fieldCords: FieldCords) = fieldMap.get(fieldCords)

    fun dto(sequences: List<List<FieldCords>>) =
            getSequences(sequences)
                    .map { mapRowToDto(it) }
                    .map { it.toJavaList() }
                    .map { RowDto(it) }
                    .toJavaList()

    fun isWin(currentType: FieldType, sequences: List<List<FieldCords>>) =
            getSequences(sequences)
                    .find { isWinSequence(it, currentType) }
                    .isDefined

    fun getSequences(sequences: List<List<FieldCords>>) = sequences.map { getSingleSequence(it) }

    private fun getSingleSequence(singleSequence: List<FieldCords>) = singleSequence.flatMap { fieldMap.get(it) }

    private fun isWinSequence(fieldsSequence: List<FieldType>, currentType: FieldType) =
            fieldsSequence
                    .find { it != currentType }
                    .isEmpty

    private fun mapRowToDto(row: List<FieldType>) =
            row
                    .map { it.typeDto }

}