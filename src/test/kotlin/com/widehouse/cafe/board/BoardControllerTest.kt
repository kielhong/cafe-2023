package com.widehouse.cafe.board

import com.ninjasquad.springmockk.MockkBean
import com.widehouse.cafe.board.dto.BoardRequestFixture
import com.widehouse.cafe.board.dto.BoardResponseFixture
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.coEvery
import io.mockk.coVerify
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest(BoardController::class)
class BoardControllerTest(
    private val webClient: WebTestClient
) : StringSpec() {
    override fun extensions() = listOf(SpringExtension)

    @MockkBean
    private lateinit var boardService: BoardService

    init {
        coroutineTestScope = true

        "POST /cafes/{cafeUrl}/boards" {
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
                .jsonPath("$.name").isEqualTo("board")

            coVerify { boardService.create(cafeUrl, any()) }
        }
    }
}
