package com.widehouse.cafe.board.dto

object BoardResponseFixture {
    fun create(id: Long = 1L) = BoardResponse(id, "test", "board$id")
}
