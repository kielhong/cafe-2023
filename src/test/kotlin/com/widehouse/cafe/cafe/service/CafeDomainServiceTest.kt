package com.widehouse.cafe.cafe.service

import com.widehouse.cafe.cafe.CafeCoroutineRepository
import com.widehouse.cafe.cafe.model.CafeFixture
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.StringSpec
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK

class CafeDomainServiceTest : StringSpec() {
    private lateinit var service: CafeDomainService

    @MockK
    private lateinit var cafeRepository: CafeCoroutineRepository

    init {
        isolationMode = IsolationMode.InstancePerLeaf
        coroutineTestScope = true

        val article = CafeFixture.create()

        beforeEach {
            MockKAnnotations.init(this)

            service = CafeDomainService(cafeRepository)
        }

        afterEach {
            clearAllMocks()
        }

        "create Article" {
            coEvery { cafeRepository.save(any()) } returnsArgument 0
            // when
            service.create(article)
            // then
            coVerify { cafeRepository.save(article) }
        }
    }
}
