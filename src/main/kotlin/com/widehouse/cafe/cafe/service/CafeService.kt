package com.widehouse.cafe.cafe.service

import com.widehouse.cafe.cafe.dto.CafeRequest
import com.widehouse.cafe.cafe.dto.CafeResponse
import com.widehouse.cafe.cafe.model.Cafe
import com.widehouse.cafe.cafe.model.CafeRepository
import com.widehouse.cafe.cafe.model.CategoryRepository
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

    suspend fun update(request: CafeRequest): CafeResponse {
        val cafe = cafeDomainService.getCafeByUrl(request.url)
            ?: throw DataNotFoundException("${request.url} not found")
        val category = categoryRepository.findById(request.categoryId).block()!!

        val updatedCafe = cafeDomainService.update(
            cafe.apply {
                this.name = request.name
                this.description = request.description
                this.category = category
            }
        )
        return CafeResponse.from(updatedCafe)
    }

    suspend fun delete(url: String) {
        val cafe = cafeDomainService.getCafeByUrl(url)
            ?: throw DataNotFoundException("$url not found")
        cafeDomainService.delete(cafe)
    }
}
