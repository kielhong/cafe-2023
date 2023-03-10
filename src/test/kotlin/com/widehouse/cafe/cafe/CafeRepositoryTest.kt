package com.widehouse.cafe.cafe

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import reactor.test.StepVerifier

@DataMongoTest
class CafeRepositoryTest(
    private val mongoTemplate: ReactiveMongoTemplate,
    private val cafeRepository: CafeRepository
) : StringSpec() {
    init {
        "findByUrl" {
            // given
            val cafe = CafeFixture.create()
            mongoTemplate.insert(cafe, "cafe").block()
            // when
            val result = cafeRepository.findByUrl(cafe.url)
            // then
            result
                .`as`(StepVerifier::create)
                .assertNext {
                    it.url shouldBe cafe.url
                }
                .verifyComplete()
        }

        "findByCategoryId" {
            // given
            val categoryId = 1L
            (1..5).map { mongoTemplate.insert(CafeFixture.from(it, categoryId), "cafe").block() }
            // when
            val result = cafeRepository.findByCategoryId(categoryId)
            // then
            result
                .`as`(StepVerifier::create)
                .thenConsumeWhile { it.categoryId == categoryId }
                .verifyComplete()
        }
    }
}
