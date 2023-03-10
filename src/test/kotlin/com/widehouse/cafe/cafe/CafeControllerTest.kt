package com.widehouse.cafe.cafe

import com.ninjasquad.springmockk.MockkBean
import com.widehouse.cafe.cafe.dto.CafeRequestFixture
import com.widehouse.cafe.cafe.dto.CafeResponseFixture
import com.widehouse.cafe.common.exception.DataNotFoundException
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import io.mockk.verify
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@WebFluxTest(CafeController::class)
class CafeControllerTest(
    private val webClient: WebTestClient
) : DescribeSpec() {
    override fun extensions() = listOf(SpringExtension)

    @MockkBean
    private lateinit var cafeService: CafeService

    init {
        describe("get /cafes/{url}") {
            val url = "test"
            val response = CafeResponseFixture.create()
            every { cafeService.getCafe(any()) } returns Mono.just(response)

            it("개별 카페를 반환, 200 OK") {
                webClient.get()
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
                every { cafeService.getCafe(any()) } returns Mono.error { DataNotFoundException("$url not found") }
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
            every { cafeService.getCafesByCategoryId(any()) } returns Flux.fromIterable(list)

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
            every { cafeService.create(any()) } returns Mono.just(response)

            val request = CafeRequestFixture.create()
            it("카페를 생성하고 200을 반환") {
                webClient.post()
                    .uri("/cafes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isOk

                verify { cafeService.create(any()) }
            }
        }

        describe("put /cafes") {
            it("존재하는 카페일 때, 카페를 수정하고 200을 반환") {
                val response = CafeResponseFixture.create()
                every { cafeService.update(any()) } returns Mono.just(response)

                val request = CafeRequestFixture.create()
                webClient.put()
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
                every { cafeService.update(any()) } returns Mono.error(DataNotFoundException(""))

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
                every { cafeService.delete(any()) } returns Mono.empty()

                webClient.delete()
                    .uri("/cafes/{url}", "test")
                    .exchange()
                    .expectStatus().isOk
            }

            it("존재하지 않는 카페 url 일때, 404 반환") {
                every { cafeService.delete(any()) } returns Mono.error(DataNotFoundException(""))

                webClient.delete()
                    .uri("/cafes/{url}", "test")
                    .exchange()
                    .expectStatus().isNotFound
            }
        }
    }
}
