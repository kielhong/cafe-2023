package com.widehouse.cafe.cafe

import com.widehouse.cafe.cafe.model.Cafe
import com.widehouse.cafe.cafe.model.CafeFixture
import com.widehouse.cafe.cafe.model.CafeRepository
import com.widehouse.cafe.cafe.model.Category
import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.toList
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.ReactiveMongoTemplate

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
            val result = cafeRepository.findByUrl(cafe.url)!!
            // then
            result.url shouldBe cafe.url
        }

        "findByCategoryId" {
            // given
            val category = Category(1L, "")
            (1..5).map { mongoTemplate.insert(CafeFixture.from(it, category), "cafe").block() }
            // when
            val result = cafeRepository.findByCategoryId(category.id)
            // then
            result.toList().forAll { it.category.id shouldBe category.id }
        }
    }
}
