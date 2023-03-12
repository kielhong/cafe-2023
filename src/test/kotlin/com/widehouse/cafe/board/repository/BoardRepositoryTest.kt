package com.widehouse.cafe.board.repository

import com.widehouse.cafe.board.model.Board
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.count
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.ReactiveMongoTemplate

@DataMongoTest
class BoardRepositoryTest(
    private val mongoTemplate: ReactiveMongoTemplate,
    private val boardRepository: BoardRepository
) : StringSpec() {
    init {
        coroutineTestScope = true

        "findByCafeUrl" {
            // given
            (1L..2L)
                .map { mongoTemplate.insert(Board(it, "test", "board"), "board").block() }
            // when
            val result = boardRepository.findByCafeUrl("test")
            // then
            result.count() shouldBe 2
        }
    }
}
