package com.widehouse.cafe.common.sequence

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.context.annotation.Import

@DataMongoTest
@Import(SequenceService::class)
class SequenceServiceTest(
    private val sequenceService: SequenceService
) : StringSpec() {
    init {
        coroutineTestScope = true

        "generate auto generated id" {
            val result = mutableListOf<Long>()
            repeat(10) {
                val seq = sequenceService.generateSequence("sequence")
                result.add(seq)
            }
            // then
            result shouldContainExactly (1L..10L)
        }

        "generate id first time" {
            val seq = sequenceService.generateSequence("first")
            // then
            seq shouldBe 1L
        }
    }
}
