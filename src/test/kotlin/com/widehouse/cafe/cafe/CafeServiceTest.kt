package com.widehouse.cafe.cafe

import com.widehouse.cafe.cafe.dto.CafeRequestFixture
import com.widehouse.cafe.cafe.model.Cafe
import com.widehouse.cafe.cafe.model.CafeFixture
import com.widehouse.cafe.cafe.model.CafeRepository
import com.widehouse.cafe.cafe.model.Category
import com.widehouse.cafe.cafe.model.CategoryRepository
import com.widehouse.cafe.cafe.service.CafeDomainService
import com.widehouse.cafe.cafe.service.CafeService
import com.widehouse.cafe.common.exception.DataNotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.verify
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class CafeServiceTest : DescribeSpec() {
    private lateinit var service: CafeService

    @MockK
    private lateinit var cafeDomainService: CafeDomainService

    @MockK
    private lateinit var cafeRepository: CafeRepository

    @MockK
    private lateinit var categoryRepository: CategoryRepository

    init {
        isolationMode = IsolationMode.InstancePerLeaf
        MockKAnnotations.init(this)

        lateinit var cafe: Cafe

        beforeEach {
            service = CafeService(cafeDomainService, cafeRepository, categoryRepository)

            cafe = CafeFixture.create()
            every { cafeRepository.findByUrl(any()) } returns Mono.just(cafe)
            coEvery { cafeDomainService.getCafeByUrl(any()) } returns cafe
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
                coEvery { cafeDomainService.create(any()) } returns createdCafe
                // when
                val result = service.create(request)
                // then
                result.url shouldBe request.url
                result.name shouldBe request.name
                result.description shouldBe request.description
                result.category.id shouldBe request.categoryId
            }
        }

        describe("update 카페") {
            val request = CafeRequestFixture.create()

            it("카페 정보 변경하고 변경된 카페 반환") {
                // given
                val category = Category(request.categoryId, "")
                every { categoryRepository.findById(ofType(Long::class)) } returns Mono.just(category)
                val updatedCafe = Cafe("test", request.name, request.description, Category(request.categoryId, ""))
                coEvery { cafeDomainService.update(any()) } returnsArgument 0
                // when
                val result = service.update(request)
                // then
                result.url shouldBe cafe.url
                result.name shouldBe updatedCafe.name
                result.description shouldBe updatedCafe.description
            }

            it("없는 카페이면 DataNotFoundException") {
                // given
                coEvery { cafeDomainService.getCafeByUrl(any()) } returns null
                // when
                shouldThrow<DataNotFoundException> {
                    service.update(request)
                }
            }
        }

        describe("delete 카페") {
            val url = "test"
            it("카페 정보 삭제") {
                // given
                coEvery { cafeDomainService.getCafeByUrl(any()) } returns cafe
                coEvery { cafeDomainService.delete(any()) } just Runs
                // when
                service.delete(url)
                // then
                coVerify { cafeDomainService.delete(cafe) }
            }

            it("없는 카페이면 DataNotFoundException") {
                // given
                coEvery { cafeDomainService.getCafeByUrl(any()) } returns null
                // when
                shouldThrow<DataNotFoundException> {
                    service.delete(url)
                }
                // then
                coVerify(exactly = 0) { cafeDomainService.delete(any()) }
            }
        }
    }
}
