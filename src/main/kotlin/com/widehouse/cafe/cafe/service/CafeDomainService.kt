package com.widehouse.cafe.cafe.service

import com.widehouse.cafe.cafe.model.Cafe
import com.widehouse.cafe.cafe.model.CafeCoroutineRepository
import org.springframework.stereotype.Service

@Service
class CafeDomainService(
    private val cafeRepository: CafeCoroutineRepository
) {
    suspend fun getCafeByUrl(url: String): Cafe? {
        return cafeRepository.findById(url)
    }

    suspend fun create(cafe: Cafe): Cafe {
        return cafeRepository.save(cafe)
    }

    suspend fun update(cafe: Cafe): Cafe {
        return cafeRepository.save(cafe)
    }

    suspend fun delete(cafe: Cafe) {
        cafeRepository.delete(cafe)
    }
}
