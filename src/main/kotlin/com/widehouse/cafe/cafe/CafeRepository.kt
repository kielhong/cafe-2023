package com.widehouse.cafe.cafe

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface CafeRepository : ReactiveMongoRepository<Cafe, String> {
    fun findByUrl(url: String): Mono<Cafe>
}
