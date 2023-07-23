package com.widehouse.cafe.user.model

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.ReactiveMongoTemplate

@DataMongoTest
class UserRepositoryTest(
    private val mongoTemplate: ReactiveMongoTemplate,
    private val userRepository: UserRepository
) : StringSpec() {
    init {
        "findByUsername" {
            // given
            val username = "user"
            val savedUser = mongoTemplate.insert(User(username, "password", emptySet()), "user").block()!!
            // when
            val result = userRepository.findByUsername(username)
            // then
            result shouldNotBe null
            result?.username shouldBe savedUser.username
            result?.password shouldBe savedUser.password
        }
    }
}
