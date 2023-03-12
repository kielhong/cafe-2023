package com.widehouse.cafe.board.dto

import com.widehouse.cafe.board.model.Board

data class BoardResponse(
    val id: Long,
    val cafeUrl: String,
    val name: String
) {
    companion object {
        fun from(board: Board) =
            BoardResponse(
                board.id,
                board.cafeUrl,
                board.name
            )
    }
}
