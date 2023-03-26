package com.widehouse.cafe.cafe.service

import com.widehouse.cafe.cafe.model.CafeCoroutineRepository
import com.widehouse.cafe.cafe.model.CafeFixture
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class CafeDomainServiceTest : StringSpec() {
    private val cafeRepository = mockk<CafeCoroutineRepository>()

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
    }
}
