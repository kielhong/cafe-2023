package com.widehouse.cafe.common.sequence

import com.widehouse.cafe.board.model.Board
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.context.annotation.Import

@DataMongoTest
@Import(SequenceService::class)
class SequenceServiceTest(
    private val sequenceService: SequenceService
) : StringSpec() {
    init {
        "generate auto generated id" {
            val result = mutableListOf<Long>()
            repeat(10) {
                val seq = sequenceService.generateSequence(Board.SEQUENCE_NAME).block()!!
                result.add(seq)
            }
            // then
            result shouldContainExactly (1L..10L)
        }
    }
}
