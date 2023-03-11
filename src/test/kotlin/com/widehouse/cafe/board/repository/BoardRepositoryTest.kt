package com.widehouse.cafe.board.repository

import com.widehouse.cafe.board.model.Board
import com.widehouse.cafe.cafe.CafeService
import com.widehouse.cafe.common.sequence.SequenceService
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import reactor.test.StepVerifier

@DataMongoTest
class BoardRepositoryTest(
    private val mongoTemplate: ReactiveMongoTemplate,
    private val boardRepository: BoardRepository
) : StringSpec({
    "findByCafeUrl" {
        // given
        (1L..2L)
            .map { mongoTemplate.insert(Board(it,"test", "board"), "board").block() }
        // when
        val result = boardRepository.findByCafeUrl("test")
        // then
        result
            .`as`(StepVerifier::create)
            .expectNextCount(2)
            .verifyComplete()
    }
})
