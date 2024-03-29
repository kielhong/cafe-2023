package com.widehouse.cafe.article

import com.ninjasquad.springmockk.MockkBean
import com.widehouse.cafe.article.dto.ArticleRequest
import com.widehouse.cafe.article.dto.ArticleRequestFixture
import com.widehouse.cafe.article.dto.ArticleResponseFixture
import com.widehouse.cafe.article.service.ArticleService
import com.widehouse.cafe.common.SecurityControllerTest
import com.widehouse.cafe.common.exception.DataNotFoundException
import com.widehouse.cafe.common.exception.ForbiddenException
import com.widehouse.cafe.user.model.Role
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser
import org.springframework.test.web.reactive.server.WebTestClient
import java.util.*

@WebFluxTest(ArticleController::class)
class ArticleControllerTest(
    private val webClient: WebTestClient,
    @MockkBean
    private val articleService: ArticleService,
    @MockkBean
    private val userDetailsService: UserDetailsService
) : SecurityControllerTest() {
    init {
        lateinit var username: String
        lateinit var user: UserDetails

        beforeEach {
            username = UUID.randomUUID().toString()
            user = User.withUsername(username).password("user").roles(Role.USER.value).build()

            coEvery { userDetailsService.loadUserByUsername(any()) } returns user
        }

        describe("POST /articles") {
            coEvery { articleService.create(any(), any()) } returns ArticleResponseFixture.create()

            it("게시물을 생성하고 200을 반환") {
                val request = ArticleRequestFixture.create()
                webClient
                    .mutateWith(mockUser(username))
                    .post()
                    .uri("/articles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isOk

                coVerify { articleService.create(user, any()) }
            }
        }

        describe("PUT /articles/{articleId}") {
            val articleId = 1L
            it("게시물을 수정하고 200을 반환") {
                coEvery { articleService.update(any(), any()) } returns ArticleResponseFixture.create()

                val request = ArticleRequest("cafe", 1L, "subject2", "content2")
                webClient
                    .mutateWith(mockUser())
                    .put()
                    .uri("/articles/{articleId}", articleId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isOk

                coVerify { articleService.update(articleId, any()) }
            }
        }

        describe("DELETE /articles/{articleId}") {
            val articleId = 1L
            it("게시물을 삭제하고 200을 반환") {
                coEvery { articleService.delete(any()) } just Runs

                webClient
                    .mutateWith(mockUser())
                    .delete()
                    .uri("/articles/{articleId}", articleId)
                    .exchange()
                    .expectStatus().isOk

                coVerify { articleService.delete(articleId) }
            }

            it("존재하지 않는 게시물이면 404") {
                coEvery { articleService.delete(any()) } throws DataNotFoundException("")

                webClient
                    .mutateWith(mockUser())
                    .delete()
                    .uri("/articles/{articleId}", articleId)
                    .exchange()
                    .expectStatus().isNotFound
            }

            it("작성자가 아닌 사람이 삭제할 경우 403") {
                coEvery { articleService.delete(any()) } throws ForbiddenException("")

                webClient
                    .mutateWith(mockUser())
                    .delete()
                    .uri("/articles/{articleId}", articleId)
                    .exchange()
                    .expectStatus().isForbidden
            }
        }
    }
}
