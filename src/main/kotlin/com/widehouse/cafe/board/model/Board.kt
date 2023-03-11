package com.widehouse.cafe.board.model

import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("board")
class Board(
    val id: Long,
    @Indexed
    val cafeUrl: String,
    val name: String,
) {
    companion object {
        @Transient
        const val SEQUENCE_NAME = "board_sequence"
    }
}
