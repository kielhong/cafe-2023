package com.widehouse.cafe.cafe

import com.widehouse.cafe.cafe.dto.CafeRequest
import com.widehouse.cafe.cafe.dto.CafeResponse
import com.widehouse.cafe.cafe.model.Cafe
import com.widehouse.cafe.cafe.service.CafeDomainService
import com.widehouse.cafe.common.exception.DataNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
@Transactional
class CafeService(
    private val cafeDomainService: CafeDomainService,
    private val cafeRepository: CafeRepository,
    private val categoryRepository: CategoryRepository
) {
    @Transactional(readOnly = true)
    fun getCafe(url: String): Mono<CafeResponse> {
        return cafeRepository.findByUrl(url)
            .map { CafeResponse.from(it) }
            .switchIfEmpty(Mono.error(DataNotFoundException("$url not found")))
    }

    @Transactional(readOnly = true)
    fun getCafesByCategoryId(categoryId: Long): Flux<CafeResponse> {
        return cafeRepository.findByCategoryId(categoryId)
            .map { CafeResponse.from(it) }
    }

    suspend fun create(request: CafeRequest): CafeResponse {
        val cafe = categoryRepository.findById(request.categoryId)
            .map { Cafe(request.url, request.name, request.description, it) }
            .block()!!
        val savedCafe = cafeDomainService.create(cafe)
        return CafeResponse.from(savedCafe)
    }

    fun update(request: CafeRequest): Mono<CafeResponse> {
        return cafeRepository.findByUrl(request.url)
            .switchIfEmpty(Mono.error(DataNotFoundException("${request.url} not found")))
            .flatMap {
                val category = categoryRepository.findById(request.categoryId).block()!!
                it.name = request.name
                it.description = request.description
                it.category = category
                cafeRepository.save(it)
            }
            .map { CafeResponse.from(it) }
    }

    fun delete(url: String): Mono<Void> {
        return cafeRepository.findByUrl(url)
            .switchIfEmpty(Mono.error(DataNotFoundException("$url not found")))
            .flatMap { cafeRepository.delete(it) }
    }
}
