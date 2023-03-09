package com.widehouse.cafe.cafe

import com.widehouse.cafe.cafe.dto.CafeRequest
import com.widehouse.cafe.cafe.dto.CafeResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
class CafeService(
    private val cafeRepository: CafeRepository
) {
    @Transactional
    fun create(cafeRequest: CafeRequest): Mono<CafeResponse> {
        return cafeRepository.save(CafeRequest.toModel(cafeRequest))
            .map { CafeResponse.from(it) }
    }

    @Transactional(readOnly = true)
    fun getCafe(url: String): Mono<CafeResponse> {
        return cafeRepository.findByUrl(url)
            .map { CafeResponse.from(it) }
    }
}
