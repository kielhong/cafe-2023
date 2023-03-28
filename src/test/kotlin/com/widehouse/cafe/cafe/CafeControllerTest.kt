package com.widehouse.cafe.cafe

import com.ninjasquad.springmockk.MockkBean
import com.widehouse.cafe.cafe.dto.CafeRequestFixture
import com.widehouse.cafe.cafe.dto.CafeResponseFixture
import com.widehouse.cafe.cafe.service.CafeService
import com.widehouse.cafe.common.SecurityControllerTest
import com.widehouse.cafe.common.exception.DataNotFoundException
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import kotlinx.coroutines.flow.asFlow
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest(CafeController::class)
class CafeControllerTest(
    private val webClient: WebTestClient,
    @MockkBean
    private val cafeService: CafeService
) : SecurityControllerTest() {
    init {
        describe("get /cafes/{url}") {
            val url = "test"
            val response = CafeResponseFixture.create()
            coEvery { cafeService.getCafe(any()) } returns response

            it("개별 카페를 반환, 200 OK") {
                webClient
                    .get()
                    .uri("/cafes/{url}", url)
                    .exchange()
                    .expectStatus().isOk
                    .expectBody()
                    .jsonPath("$.url").isEqualTo(response.url)
                    .jsonPath("$.name").isEqualTo(response.name)
                    .jsonPath("$.description").isEqualTo(response.description)
            }

            it("존재 하지 않는 카페, 404 Not Found") {
                // given
                coEvery { cafeService.getCafe(any()) } throws DataNotFoundException("$url not found")
                // then
                webClient.get()
                    .uri("/cafes/{url}", url)
                    .exchange()
                    .expectStatus().isNotFound
            }
        }

        describe("get /cafes by categoryId") {
            val categoryId = 1L
            val list = (1..2).map { CafeResponseFixture.from(it) }
            coEvery { cafeService.getCafesByCategoryId(any()) } returns list.asFlow()

            it("개별 카페를 반환, 200 OK") {
                webClient.get()
                    .uri("/cafes?categoryId={categoryId}", categoryId)
                    .exchange()
                    .expectStatus().isOk
                    .expectBody()
                    .jsonPath("$[0].url").isEqualTo("test1")
            }
        }

        describe("post /cafes") {
            val response = CafeResponseFixture.create()
            coEvery { cafeService.create(any()) } returns response

            val request = CafeRequestFixture.create()
            it("카페를 생성하고 200을 반환") {
                webClient.post()
                    .uri("/cafes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isOk

                coVerify { cafeService.create(any()) }
            }
        }

        describe("put /cafes") {
            it("존재하는 카페일 때, 카페를 수정") {
                val response = CafeResponseFixture.create()
                coEvery { cafeService.update(any()) } returns response

                val request = CafeRequestFixture.create()
                webClient
                    .put()
                    .uri("/cafes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isOk
                    .expectBody()
                    .jsonPath("$.name").isEqualTo(response.name)
                    .jsonPath("$.description").isEqualTo(response.description)
            }

            it("존재하지 않는 카페 url 일때, 404 반환") {
                coEvery { cafeService.update(any()) } throws DataNotFoundException("")

                val request = CafeRequestFixture.create()
                webClient.put()
                    .uri("/cafes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isNotFound
            }
        }

        describe("delete /cafes") {
            it("존재하는 카페일 때, 카페를 삭제하고 200을 반환") {
                coEvery { cafeService.delete(any()) } just Runs

                webClient.delete()
                    .uri("/cafes/{url}", "test")
                    .exchange()
                    .expectStatus().isOk
            }

            it("존재하지 않는 카페 url 일때, 404 반환") {
                coEvery { cafeService.delete(any()) } throws DataNotFoundException("")

                webClient.delete()
                    .uri("/cafes/{url}", "test")
                    .exchange()
                    .expectStatus().isNotFound
            }
        }
    }
}
