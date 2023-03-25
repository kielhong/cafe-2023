package com.widehouse.cafe.cafe.service

import com.widehouse.cafe.cafe.CafeCoroutineRepository
import com.widehouse.cafe.cafe.model.Cafe
import org.springframework.stereotype.Service

@Service
class CafeDomainService(
    private val cafeRepository: CafeCoroutineRepository
) {
    suspend fun create(cafe: Cafe): Cafe {
        return cafeRepository.save(cafe)
    }
}
