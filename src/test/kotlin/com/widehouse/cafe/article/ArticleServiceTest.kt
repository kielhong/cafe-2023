package com.widehouse.cafe.article

import com.widehouse.cafe.article.dto.ArticleRequestFixture
import com.widehouse.cafe.article.model.Article
import com.widehouse.cafe.board.BoardRepository
import com.widehouse.cafe.board.model.BoardFixture
import com.widehouse.cafe.common.exception.DataNotFoundException
import com.widehouse.cafe.common.sequence.SequenceService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import org.springframework.test.util.ReflectionTestUtils.setField

class ArticleServiceTest : DescribeSpec() {
    private lateinit var service: ArticleService

    @MockK
    private lateinit var articleRepository: ArticleRepository

    @MockK
    private lateinit var boardRepository: BoardRepository

    @MockK
    private lateinit var sequenceService: SequenceService

    init {
        isolationMode = IsolationMode.InstancePerLeaf
        coroutineTestScope = true

        beforeEach {
            MockKAnnotations.init(this)

            service = ArticleService(articleRepository, boardRepository, sequenceService)

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
                    coEvery { articleRepository.save(any()) } returnsArgument 0
                    // when
                    val result = service.create(request)
                    // then
                    result.id shouldBe 1L
                    result.cafeUrl shouldBe request.cafeUrl
                    result.boardId shouldBe request.boardId
                    result.subject shouldBe request.subject
                    result.content shouldBe request.content
                    result.createdAt.shouldNotBeNull()
                }
            }

            context("Invalid Board") {
                it("IllegalArgumentException") {
                    coEvery { boardRepository.findById(any()) } returns null
                    // when
                    shouldThrow<IllegalArgumentException> {
                        service.create(request)
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
                    coEvery { articleRepository.findById(any()) } returns article
                    coEvery { articleRepository.deleteById(any()) } just Runs
                    // when
                    service.delete(articleId)
                    // then
                    coEvery { articleRepository.deleteById(articleId) }
                }
            }

            context("존재하지 않는 article") {
                it("throw DataNotFoundException") {
                    coEvery { articleRepository.findById(any()) } returns null
                    // when
                    shouldThrow<DataNotFoundException> {
                        service.delete(articleId)
                    }
                    coVerify(exactly = 0) { articleRepository.deleteById(articleId) }
                }
            }
        }
    }
}
