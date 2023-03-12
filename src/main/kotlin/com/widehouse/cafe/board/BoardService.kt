package com.widehouse.cafe.board

import com.widehouse.cafe.board.dto.BoardRequest
import com.widehouse.cafe.board.dto.BoardResponse
import com.widehouse.cafe.board.model.Board
import com.widehouse.cafe.common.sequence.SequenceService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BoardService(
    private val boardRepository: BoardRepository,
    private val sequenceService: SequenceService
) {
    @Transactional
    suspend fun create(cafeUrl: String, request: BoardRequest): BoardResponse {
        val id = sequenceService.generateSequence(Board.SEQUENCE_NAME).block()!!
        val board = boardRepository.save(Board(id, cafeUrl, request.name))

        return BoardResponse.from(board)
    }

    @Transactional
    suspend fun delete(cafeUrl: String, boardId: Long) {
        boardRepository.deleteById(boardId)
    }
}
