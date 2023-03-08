package com.widehouse.cafe.cafe

import com.widehouse.cafe.cafe.dto.CafeRequest
import com.widehouse.cafe.cafe.dto.CafeResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CafeService(
    private val cafeRepository: CafeRepository
) {
    fun create(cafeRequest: CafeRequest): Mono<CafeResponse> {
        return cafeRepository.insert(CafeRequest.toModel(cafeRequest))
            .map { CafeResponse.from(it) }
    }
}
