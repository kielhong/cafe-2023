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
            val url = "test"
            mongoTemplate.insert(Cafe(url, "test", "test"), "cafe").block()
            // when
            val result = cafeRepository.findByUrl(url)
            // then
            result
                .`as`(StepVerifier::create)
                .assertNext {
                    it.url shouldBe url
                }
                .verifyComplete()
        }
    }
}
