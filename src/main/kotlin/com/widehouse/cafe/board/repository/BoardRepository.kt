package com.widehouse.cafe.board.repository

import com.widehouse.cafe.board.model.Board
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface BoardRepository : CoroutineCrudRepository<Board, String> {
    suspend fun findByCafeUrl(cafeUrl: String): Flow<Board>
}
