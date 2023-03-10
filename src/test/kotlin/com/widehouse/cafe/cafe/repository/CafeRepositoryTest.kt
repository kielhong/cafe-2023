package com.widehouse.cafe.cafe.repository

import com.widehouse.cafe.cafe.model.Cafe
import com.widehouse.cafe.cafe.model.CafeFixture
import com.widehouse.cafe.cafe.model.Category
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
            val cafe = Cafe("test", "test", "desc", Category(1L, ""))
            mongoTemplate.insert(cafe, "cafe").block()
            // when
            val result = cafeRepository.findByUrl(cafe.url)
            // then
            result
                .`as`(StepVerifier::create)
                .assertNext { it.url shouldBe cafe.url }
                .verifyComplete()
        }

        "findByCategoryId" {
            // given
            val category = Category(1L, "")
            (1..5).map { mongoTemplate.insert(CafeFixture.from(it, category), "cafe").block() }
            // when
            val result = cafeRepository.findByCategoryId(category.id)
            // then
            result
                .`as`(StepVerifier::create)
                .thenConsumeWhile { it.category.id == category.id }
                .verifyComplete()
        }
    }
}
