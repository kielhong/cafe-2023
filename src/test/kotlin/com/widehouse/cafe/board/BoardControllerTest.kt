package com.widehouse.cafe.board

import com.ninjasquad.springmockk.MockkBean
import com.widehouse.cafe.board.dto.BoardRequestFixture
import com.widehouse.cafe.board.dto.BoardResponseFixture
import com.widehouse.cafe.common.SecurityControllerTest
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import kotlinx.coroutines.flow.asFlow
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest(BoardController::class)
class BoardControllerTest(
    private val webClient: WebTestClient,
    @MockkBean
    private val boardService: BoardService
) : SecurityControllerTest() {
    init {
        beforeEach {
            coroutineTestScope = true
        }

        describe("GET /cafes/{cafeUrl}/boards") {
            it("get cafe list") {
                // given
                val cafeUrl = "test"
                val responses = (1L..2L).map { BoardResponseFixture.create() }
                coEvery { boardService.getBoardsByCafe(any()) } returns responses.asFlow()
                // when
                webClient.get()
                    .uri("/cafes/{cafeUrl}/boards", cafeUrl)
                    .exchange()
                    .expectStatus().isOk
                    .expectBody()
                    .jsonPath("$.size()").isEqualTo(2)
            }
        }

        describe("POST /cafes/{cafeUrl}/boards") {
            it("create cafe") {
                // given
                val cafeUrl = "test"
                val response = BoardResponseFixture.create()
                coEvery { boardService.create(any(), any()) } returns response
                // when
                val request = BoardRequestFixture.create()
                webClient.post()
                    .uri("/cafes/{cafeUrl}/boards", cafeUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isOk
                    .expectBody()
                    .jsonPath("$.cafeUrl").isEqualTo(cafeUrl)
                    .jsonPath("$.name").isEqualTo(response.name)

                coVerify {
                    boardService.create(
                        cafeUrl,
                        withArg { it.name shouldBe request.name }
                    )
                }
            }
        }

        describe("DELETE /cafes/{cafeUrl}/boards/{id}") {
            it("delete cafe") {
                // given
                val cafeUrl = "test"
                val boardId = 1L
                coEvery { boardService.delete(any(), any()) } just Runs

                webClient.delete()
                    .uri("/cafes/{cafeUrl}/boards/{id}", cafeUrl, boardId)
                    .exchange()
                    .expectStatus().isOk
            }
        }
    }
}
