package com.widehouse.cafe.board

import com.widehouse.cafe.board.dto.BoardRequestFixture
import com.widehouse.cafe.board.model.Board
import com.widehouse.cafe.board.repository.BoardRepository
import com.widehouse.cafe.common.sequence.SequenceService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import reactor.core.publisher.Mono

class BoardServiceTest : StringSpec() {
    private lateinit var service: BoardService

    @MockK
    private lateinit var boardRepository: BoardRepository

    @MockK
    private lateinit var sequenceService: SequenceService

    init {
        coroutineTestScope = true

        beforeEach {
            MockKAnnotations.init(this)

            service = BoardService(boardRepository, sequenceService)

            every { sequenceService.generateSequence(Board.SEQUENCE_NAME) } returns Mono.just(1L)
        }

        "create Board of Cafe" {
            // given
            val cafeUrl = "test"
            val request = BoardRequestFixture.create()
            coEvery { boardRepository.save(any()) } returnsArgument 0
            // when
            val result = service.create(cafeUrl, request)
            // then
            result.cafeUrl shouldBe cafeUrl
            result.name shouldBe request.name
        }
    }
}
