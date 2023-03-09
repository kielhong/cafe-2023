package com.widehouse.cafe.cafe

import com.widehouse.cafe.cafe.dto.CafeRequest
import com.widehouse.cafe.common.exception.DataNotFoundException
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class CafeServiceTest : DescribeSpec() {
    private lateinit var service: CafeService

    @MockK
    private lateinit var cafeRepository: CafeRepository

    private lateinit var cafe: Cafe

    init {
        beforeEach {
            MockKAnnotations.init(this)

            service = CafeService(cafeRepository)

            cafe = Cafe("test", "test", "desc")
            every { cafeRepository.findByUrl(any()) } returns Mono.just(cafe)
        }

        afterEach {
            clearAllMocks()
        }

        describe("get a 카페") {
            it("카페를 반환") {
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
            it("카페가 없으면 DataNotFoundException") {
                // given
                every { cafeRepository.findByUrl(any()) } returns Mono.empty()
                // when
                val result = service.getCafe("test")
                // then
                result
                    .`as`(StepVerifier::create)
                    .expectError(DataNotFoundException::class.java)
                    .verify()
            }
        }

        describe("create 카페") {
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

        describe("update 카페") {
            val request = CafeRequest("test", "test2", "desc2")
            it("카페 정보 변경하고 변경된 카페 반환") {
                // given
                val updatedCafe = Cafe("test", request.name, request.description)
                every { cafeRepository.save(any()) } returns Mono.just(updatedCafe)
                // when
                val result = service.update(request)
                // then
                result
                    .`as`(StepVerifier::create)
                    .assertNext {
                        it.url shouldBe cafe.url
                        it.name shouldBe updatedCafe.name
                        it.description shouldBe updatedCafe.description
                    }
                    .verifyComplete()
            }

            it("없는 카페이면 DataNotFoundException") {
                // given
                every { cafeRepository.findByUrl(any()) } returns Mono.empty()
                // when
                val result = service.update(request)
                // then
                result
                    .`as`(StepVerifier::create)
                    .expectError(DataNotFoundException::class.java)
                    .verify()
            }
        }

        describe("delete 카페") {
            val url = "test"
            it("카페 정보 삭제") {
                // given
                every { cafeRepository.delete(any()) } returns Mono.empty()
                // when
                val result = service.delete(url)
                // then
                result
                    .`as`(StepVerifier::create)
                    .verifyComplete()
            }

            it("없는 카페이면 DataNotFoundException") {
                // given
                every { cafeRepository.findByUrl(any()) } returns Mono.empty()
                // when
                val result = service.delete(url)
                // then
                result
                    .`as`(StepVerifier::create)
                    .expectError(DataNotFoundException::class.java)
                    .verify()
                verify(exactly = 0) { cafeRepository.delete(any()) }
            }
        }
    }
}
