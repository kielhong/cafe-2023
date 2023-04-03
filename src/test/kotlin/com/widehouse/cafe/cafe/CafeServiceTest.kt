package com.widehouse.cafe.cafe

import com.widehouse.cafe.cafe.dto.CafeRequestFixture
import com.widehouse.cafe.cafe.model.Cafe
import com.widehouse.cafe.cafe.model.CafeFixture
import com.widehouse.cafe.cafe.model.Category
import com.widehouse.cafe.cafe.model.CategoryRepository
import com.widehouse.cafe.cafe.service.CafeDomainService
import com.widehouse.cafe.cafe.service.CafeService
import com.widehouse.cafe.common.exception.DataNotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.toList
import org.springframework.test.util.ReflectionTestUtils
import reactor.core.publisher.Mono

class CafeServiceTest : DescribeSpec() {
    private val cafeDomainService = mockk<CafeDomainService>()
    private val categoryRepository = mockk<CategoryRepository>()

    private val service = CafeService(cafeDomainService, categoryRepository)

    init {
        isolationMode = IsolationMode.InstancePerLeaf

        val cafe = CafeFixture.create()

        beforeEach {
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
                result.url shouldBe cafe.url
                result.name shouldBe cafe.name
                result.description shouldBe cafe.description

                coVerify { cafeDomainService.getCafeByUrl("test") }
            }

            it("카페가 없으면 DataNotFoundException") {
                // given
                coEvery { cafeDomainService.getCafeByUrl(any()) } returns null
                // when
                shouldThrow<DataNotFoundException> {
                    service.getCafe("test")
                }
            }
        }

        describe("get 카페 by 카테고리Id") {
            val category = Category(1L, "")
            val list = (1..2).map { CafeFixture.from(it, category) }

            it("카테고리별 카페를 반환") {
                coEvery { cafeDomainService.getCafesByCategoryId(any()) } returns list.asFlow()
                // when
                val result = service.getCafesByCategoryId(category.id)
                // then
                result.toList().forAll { it.category.id shouldBe category.id }
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
            val request = CafeRequestFixture.create().apply {
                ReflectionTestUtils.setField(this, "name", "update name")
                ReflectionTestUtils.setField(this, "description", "update description")
            }

            it("카페 정보 변경하고 변경된 카페 반환") {
                // given
                val category = Category(request.categoryId, "")
                every { categoryRepository.findById(ofType(Long::class)) } returns Mono.just(category)
                coEvery { cafeDomainService.update(any()) } returnsArgument 0
                // when
                val result = service.update(request)
                // then
                result.url shouldBe cafe.url
                result.name shouldBe request.name
                result.description shouldBe request.description
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
