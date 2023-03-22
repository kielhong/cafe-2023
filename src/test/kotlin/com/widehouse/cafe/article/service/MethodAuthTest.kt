package com.widehouse.cafe.article.service

import com.widehouse.cafe.article.ArticleFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.MockKAnnotations
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.test.context.support.WithMockUser

@SpringBootTest
@WithMockUser(username = "user")
class MethodAuthTest(
    service: ArticleDomainService
) : StringSpec() {
    override fun extensions() = listOf(SpringExtension)

    init {
        beforeEach {
            MockKAnnotations.init(this)
        }

        "auth" {
            val article = ArticleFixture.create(username = "user")

            service.delete(article)
        }

        "auth fail" {
            val article = ArticleFixture.create(username = "notuser")

            shouldThrow<AccessDeniedException> {
                service.delete(article)
            }
        }
    }
}
