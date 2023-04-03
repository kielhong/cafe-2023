package com.widehouse.cafe.cafe.service

import com.widehouse.cafe.cafe.model.CafeFixture
import com.widehouse.cafe.cafe.model.CafeRepository
import com.widehouse.cafe.cafe.model.Category
import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.toList

class CafeDomainServiceTest : StringSpec() {
    private val cafeRepository = mockk<CafeRepository>()

    private val service = CafeDomainService(cafeRepository)

    init {
        coroutineTestScope = true

        val cafe = CafeFixture.create()

        "get cafe by url" {
            coEvery { cafeRepository.findById(any()) } returns cafe
            // when
            val result = service.getCafeByUrl("test")
            // then
            result shouldBe cafe
        }

        "get cafes by category" {
            val category = Category(1L, "category")
            val cafes = (1..2).map { CafeFixture.from(it, category) }
            coEvery { cafeRepository.findByCategoryId(any()) } returns cafes.asFlow()
            // when
            val result = service.getCafesByCategoryId(1L)
            // then
            result.count() shouldBe 2
            result.toList().forAll { it.category.id shouldBe category.id }
        }

        "create Cafe" {
            coEvery { cafeRepository.save(any()) } returnsArgument 0
            // when
            service.create(cafe)
            // then
            coVerify { cafeRepository.save(cafe) }
        }

        "update Cafe" {
            coEvery { cafeRepository.save(any()) } returnsArgument 0
            // when
            service.update(cafe)
            // then
            coVerify { cafeRepository.save(cafe) }
        }

        "delete Cafe" {
            coEvery { cafeRepository.delete(any()) } just Runs
            // when
            service.delete(cafe)
            // then
            coVerify { cafeRepository.delete(cafe) }
        }
    }
}
