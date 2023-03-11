package com.widehouse.cafe.board.repository

import com.widehouse.cafe.board.model.Board
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface BoardRepository : ReactiveMongoRepository<Board, String> {
    fun findByCafeUrl(cafeUrl: String): Flux<Board>
}