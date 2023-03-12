package com.widehouse.cafe.board

import com.widehouse.cafe.board.dto.BoardRequest
import com.widehouse.cafe.board.dto.BoardResponse
import kotlinx.coroutines.flow.Flow
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class BoardController(
    private val boardService: BoardService
) {
    @GetMapping("cafes/{cafeUrl}/boards")
    fun getBoardsByCafe(@PathVariable cafeUrl: String): Flow<BoardResponse> {
        return boardService.getBoardsByCafe(cafeUrl)
    }

    @PostMapping("cafes/{cafeUrl}/boards")
    suspend fun create(
        @PathVariable cafeUrl: String,
        @RequestBody request: BoardRequest
    ): BoardResponse {
        return boardService.create(cafeUrl, request)
    }

    @DeleteMapping("cafes/{cafeUrl}/boards/{id}")
    suspend fun delete(
        @PathVariable cafeUrl: String,
        @PathVariable id: Long
    ) {
        boardService.delete(cafeUrl, id)
    }
}
