package tictactoe

import io.vavr.collection.List
import io.vavr.collection.Stream

class BoardSequencesCreator {

    fun create(size: Int): BoardSequences {

        var rowSequences = List.empty<List<FieldCords>>()
        var columnSequences = List.empty<List<FieldCords>>()
        var diagonalSequences = List.empty<List<FieldCords>>()

        var diagonal1 = List.empty<FieldCords>()
        var diagonal2 = List.empty<FieldCords>()

        Stream.ofAll(0 until size)
                .forEach { row ->
                    run {

                        diagonal1 = diagonal1.append(FieldCords(row, row))
                        diagonal2 = diagonal2.append(FieldCords(row, size - 1 - row))

                        var rowSequence = List.empty<FieldCords>()
                        var columnSequence = List.empty<FieldCords>()

                        Stream.ofAll(0 until size)
                                .forEach { column ->
                                    rowSequence = rowSequence.append(FieldCords(row, column))
                                    columnSequence = columnSequence.append(FieldCords(column, row))
                                }
                        rowSequences = rowSequences.append(rowSequence)
                        columnSequences = columnSequences.append(columnSequence)
                    }
                }

        diagonalSequences = diagonalSequences.append(diagonal1).append(diagonal2)

        return BoardSequences(rowSequences.pushAll(columnSequences).pushAll(diagonalSequences), rowSequences)
    }

}