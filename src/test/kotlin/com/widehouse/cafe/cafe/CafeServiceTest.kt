package com.widehouse.cafe.cafe

import com.widehouse.cafe.cafe.dto.CafeRequestFixture
import com.widehouse.cafe.cafe.model.Cafe
import com.widehouse.cafe.cafe.model.CafeFixture
import com.widehouse.cafe.cafe.model.Category
import com.widehouse.cafe.common.exception.DataNotFoundException
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class CafeServiceTest : DescribeSpec() {
    private lateinit var service: CafeService

    @MockK
    private lateinit var cafeRepository: CafeRepository

    @MockK
    private lateinit var categoryRepository: CategoryRepository

    init {
        isolationMode = IsolationMode.InstancePerLeaf
        MockKAnnotations.init(this)

        lateinit var cafe: Cafe

        beforeEach {
            service = CafeService(cafeRepository, categoryRepository)

            cafe = CafeFixture.create()
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

        describe("get 카페 by 카테고리Id") {
            val category = Category(1L, "")
            val list = (1..2).map { CafeFixture.from(it, category) }

            it("카테고리별 카페를 반환") {
                every { cafeRepository.findByCategoryId(any()) } returns Flux.fromIterable(list)

                service.getCafesByCategoryId(category.id)
                    .`as`(StepVerifier::create)
                    .thenConsumeWhile { it.category.id == category.id }
                    .verifyComplete()
            }
        }

        describe("create 카페") {
            val request = CafeRequestFixture.create()

            val category = Category(request.categoryId, "")
            every { categoryRepository.findById(ofType(Long::class)) } returns Mono.just(category)

            it("생성된 카페를 반환") {
                // given
                val createdCafe = Cafe(request.url, request.name, request.description, Category(request.categoryId, ""))
                every { cafeRepository.save(any()) } returns Mono.just(createdCafe)
                // when
                val result = service.create(request)
                // then
                result
                    .`as`(StepVerifier::create)
                    .assertNext {
                        it.url shouldBe request.url
                        it.name shouldBe request.name
                        it.description shouldBe request.description
                        it.category.id shouldBe request.categoryId
                    }
                    .verifyComplete()
            }
        }

        describe("update 카페") {
            val request = CafeRequestFixture.create()

            it("카페 정보 변경하고 변경된 카페 반환") {
                // given
                val category = Category(request.categoryId, "")
                every { categoryRepository.findById(ofType(Long::class)) } returns Mono.just(category)
                val updatedCafe = Cafe("test", request.name, request.description, Category(request.categoryId, ""))
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
