package tictactoe

import io.vavr.collection.List

data class BoardSequences(val sequences: List<List<FieldCords>>, val rowSequences: List<List<FieldCords>>)