package com.widehouse.cafe.article.service

import com.widehouse.cafe.article.ArticleFixture
import com.widehouse.cafe.article.ArticleRepository
import com.widehouse.cafe.article.dto.ArticleRequestFixture
import com.widehouse.cafe.article.model.Article
import com.widehouse.cafe.board.BoardRepository
import com.widehouse.cafe.board.model.BoardFixture
import com.widehouse.cafe.common.exception.DataNotFoundException
import com.widehouse.cafe.common.sequence.SequenceService
import com.widehouse.cafe.user.Role.USER
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.test.util.ReflectionTestUtils.setField

class ArticleServiceTest : DescribeSpec() {
    private lateinit var service: ArticleService

    @MockK
    private lateinit var articleDomainService: ArticleDomainService

    @MockK
    private lateinit var articleRepository: ArticleRepository

    @MockK
    private lateinit var boardRepository: BoardRepository

    @MockK
    private lateinit var sequenceService: SequenceService

    init {
        isolationMode = IsolationMode.InstancePerLeaf
        coroutineTestScope = true

        lateinit var user: UserDetails

        beforeEach {
            MockKAnnotations.init(this)

            service = ArticleService(articleDomainService, articleRepository, boardRepository, sequenceService)
            // data
            user = User.withUsername("user").password("password").roles(USER.value).build()
            // mock method
            coEvery { boardRepository.findById(any()) } returns BoardFixture.create()
            coEvery { sequenceService.generateSequence(Article.SEQUENCE_NAME) } returns 1L
        }

        afterEach {
            clearAllMocks()
        }

        describe("create") {
            val request = ArticleRequestFixture.create()

            context("정상적인 입력") {
                it("Article 생성 후 response 반환") {
                    coEvery { articleDomainService.create(any()) } returnsArgument 0
                    // when
                    val result = service.create(user, request)
                    // then
                    result.id shouldBe 1L
                    result.cafeUrl shouldBe request.cafeUrl
                    result.boardId shouldBe request.boardId
                    result.subject shouldBe request.subject
                    result.content shouldBe request.content
                    result.username shouldBe user.username
                    result.createdAt.shouldNotBeNull()
                }
            }

            context("Invalid Board") {
                it("IllegalArgumentException") {
                    coEvery { boardRepository.findById(any()) } returns null
                    // when
                    shouldThrow<IllegalArgumentException> {
                        service.create(user, request)
                    }
                }
            }
        }

        describe("update") {
            val articleId = 1L
            val request = ArticleRequestFixture.create().apply {
                setField(this, "subject", "new subject")
                setField(this, "content", "new content")
            }

            context("정상적인 입력") {
                it("Article 수정 후 response 반환") {
                    val article = ArticleFixture.create(articleId)
                    coEvery { articleRepository.findById(any()) } returns article
                    coEvery { articleRepository.save(any()) } returnsArgument 0
                    // when
                    val result = service.update(articleId, request)
                    // then
                    result.id shouldBe 1L
                    result.cafeUrl shouldBe request.cafeUrl
                    result.boardId shouldBe request.boardId
                    result.subject shouldBe request.subject
                    result.content shouldBe request.content
                }
            }

            context("존재하지 않는 article") {
                it("throw DataNotFoundException") {
                    coEvery { articleRepository.findById(any()) } returns null
                    // when
                    shouldThrow<DataNotFoundException> {
                        service.update(articleId, request)
                    }
                    coVerify(exactly = 0) { articleRepository.save(any()) }
                }
            }
        }

        describe("delete") {
            val articleId = 1L
            val article = ArticleFixture.create(articleId)

            context("존재하는 게시물") {
                it("삭제 처리") {
                    coEvery { articleDomainService.getArticleById(any()) } returns article
                    coEvery { articleDomainService.delete(any()) } just Runs
                    // when
                    service.delete(articleId)
                    // then
                    coVerify {
                        articleDomainService.getArticleById(articleId)
                        articleDomainService.delete(article)
                    }
                }
            }

            context("존재하지 않는 article") {
                it("throw DataNotFoundException") {
                    coEvery { articleDomainService.getArticleById(any()) } returns null
                    // when
                    shouldThrow<DataNotFoundException> {
                        service.delete(articleId)
                    }
                    coVerify(exactly = 0) { articleDomainService.delete(any()) }
                }
            }

            context("작성자와 다른 article") {
                it("throw ForbiddenException") {
                    user = User.withUsername("otherusername").password("user").roles(USER.value).build()
                    coEvery { articleDomainService.getArticleById(any()) } returns article
                    coEvery { articleDomainService.delete(any()) } throws AccessDeniedException("")
                    // when
                    shouldThrow<AccessDeniedException> {
                        service.delete(articleId)
                    }
                }
            }
        }
    }
}
