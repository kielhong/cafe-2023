package com.widehouse.cafe.common.sequence

import com.widehouse.cafe.board.model.Board
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.context.annotation.ComponentScan
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

@DataMongoTest
@ComponentScan(basePackages = ["com.widehouse.cafe"])
class SequenceServiceTest(
    private val sequenceService: SequenceService
) : StringSpec({
    "generate auto generated id" {
        val list = (1..10)
            .map { sequenceService.generateSequence(Board.SEQUENCE_NAME).block()!! }
        // then
        list shouldContainExactly (1L..10L)
    }
})
