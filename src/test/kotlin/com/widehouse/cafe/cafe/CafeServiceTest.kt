package com.widehouse.cafe.cafe

import com.widehouse.cafe.cafe.dto.CafeRequest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class CafeServiceTest : StringSpec() {
    private lateinit var service: CafeService

    @MockK
    lateinit var cafeRepository: CafeRepository

    lateinit var cafe: Cafe

    init {
        MockKAnnotations.init(this)

        beforeEach {
            service = CafeService(cafeRepository)

            cafe = Cafe("test", "test", "desc")
        }

        "create 카페" {
            // given
            every { cafeRepository.save(any()) } returns Mono.just(cafe)
            // when
            val request = CafeRequest("test", "test", "desc")
            val result = service.create(request)
            // then
            result
                .`as`(StepVerifier::create)
                .assertNext {
                    it.url shouldBe cafe.url
                    it.name shouldBe cafe.name
                    it.description shouldBe cafe.description
                }
                .verifyComplete()
        }

        "get a 카페" {
            // given
            every { cafeRepository.findByUrl(any()) } returns Mono.just(cafe)
            // when
            val result = service.getCafe("test")
            // then
            result
                .`as`(StepVerifier::create)
                .assertNext {
                    it.url shouldBe cafe.url
                    it.name shouldBe cafe.name
                    it.description shouldBe cafe.description
                }
                .verifyComplete()
        }
    }
}
