package com.widehouse.cafe.board

import com.widehouse.cafe.board.dto.BoardRequestFixture
import com.widehouse.cafe.board.model.Board
import com.widehouse.cafe.common.sequence.SequenceService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.count
import reactor.core.publisher.Mono

class BoardServiceTest : StringSpec() {
    private lateinit var service: BoardService

    @MockK
    private lateinit var boardRepository: BoardRepository

    @MockK
    private lateinit var sequenceService: SequenceService

    init {
        coroutineTestScope = true

        val cafeUrl = "test"

        beforeEach {
            MockKAnnotations.init(this)

            service = BoardService(boardRepository, sequenceService)

            every { sequenceService.generateSequence(Board.SEQUENCE_NAME) } returns Mono.just(1L)
        }

        "get Boards by Cafe" {
            val boards = (1L..2L).map { Board(it, cafeUrl, "board$it") }
            coEvery { boardRepository.findByCafeUrl(cafeUrl) } returns boards.asFlow()
            // when
            val result = service.getBoardsByCafe(cafeUrl)
            // then
            result.count() shouldBe 2
        }

        "create Board of Cafe" {
            // given
            val request = BoardRequestFixture.create()
            coEvery { boardRepository.save(any()) } returnsArgument 0
            // when
            val result = service.create(cafeUrl, request)
            // then
            result.cafeUrl shouldBe cafeUrl
            result.name shouldBe request.name
        }

        "delete Board" {
            // given
            val boardId = 1L
            coEvery { boardRepository.deleteById(any()) } just Runs
            // when
            service.delete(cafeUrl, boardId)
            // then
            coVerify { boardRepository.deleteById(boardId) }
        }
    }
}
