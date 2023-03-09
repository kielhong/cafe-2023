package com.widehouse.cafe.cafe

import com.widehouse.cafe.cafe.dto.CafeRequest
import com.widehouse.cafe.cafe.dto.CafeResponse
import com.widehouse.cafe.common.exception.DataNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
class CafeService(
    private val cafeRepository: CafeRepository
) {
    @Transactional(readOnly = true)
    fun getCafe(url: String): Mono<CafeResponse> {
        return cafeRepository.findByUrl(url)
            .map { CafeResponse.from(it) }
            .switchIfEmpty(Mono.error(DataNotFoundException("$url not found")))
    }

    @Transactional
    fun create(request: CafeRequest): Mono<CafeResponse> {
        return cafeRepository.save(request.toModel())
            .map { CafeResponse.from(it) }
    }

    @Transactional
    fun update(request: CafeRequest): Mono<CafeResponse> {
        return cafeRepository.findByUrl(request.url)
            .flatMap {
                it.update(request)
                cafeRepository.save(it)
            }.map {
                CafeResponse.from(it)
            }
            .switchIfEmpty(Mono.error(DataNotFoundException("${request.url} not found")))
    }
}
