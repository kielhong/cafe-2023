package com.widehouse.cafe.article

import com.ninjasquad.springmockk.MockkBean
import com.widehouse.cafe.article.dto.ArticleRequestFixture
import com.widehouse.cafe.article.dto.ArticleResponseFixture
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.coEvery
import io.mockk.coVerify
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest(ArticleController::class)
class ArticleControllerTest(
    private val webClient: WebTestClient
) : DescribeSpec() {
    override fun extensions() = listOf(SpringExtension)

    @MockkBean
    private lateinit var articleService: ArticleService

    init {
        describe("POST /articles") {
            coEvery { articleService.create(any()) } returns ArticleResponseFixture.create()

            it("게시물을 생성하고 200을 반환") {
                val request = ArticleRequestFixture.create()
                webClient.post()
                    .uri("/articles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isOk

                coVerify { articleService.create(any()) }
            }
        }
    }
}
