package com.widehouse.cafe.board

import com.widehouse.cafe.board.model.Board
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface BoardRepository : CoroutineCrudRepository<Board, Long> {
    fun findByCafeUrl(cafeUrl: String): Flow<Board>
}
