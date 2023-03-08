package com.widehouse.cafe.cafe

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.widehouse.cafe.cafe.dto.CafeRequest
import com.widehouse.cafe.cafe.dto.CafeResponse
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@WebFluxTest(value = [CafeController::class])
class CafeControllerTest(@Autowired val webClient: WebTestClient) : DescribeSpec() {
    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var mapper: ObjectMapper

    @MockkBean
    private lateinit var cafeService: CafeService

    init {
        describe("post /cafes") {
            val response = CafeResponse("test", "test", "desc")
            every { cafeService.create(any()) } returns Mono.just(response)

            val request = CafeRequest("test", "test", "desc")

            it("카페를 생성하고 200을 반환") {
                webClient.post()
                    .uri("/cafes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isOk
                    .expectBody()
                    .jsonPath("$.url").isEqualTo(response.url)
                    .jsonPath("$.name").isEqualTo(response.name)
            }
        }
    }
}
