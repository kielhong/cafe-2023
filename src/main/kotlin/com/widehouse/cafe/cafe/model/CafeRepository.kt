package com.widehouse.cafe.cafe.model

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface CafeRepository : ReactiveMongoRepository<Cafe, String> {
    fun findByUrl(url: String): Mono<Cafe>
    fun findByCategoryId(categoryId: Long): Flux<Cafe>
}
