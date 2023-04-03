package com.widehouse.cafe.cafe.service

import com.widehouse.cafe.cafe.model.Cafe
import com.widehouse.cafe.cafe.model.CafeRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service

@Service
class CafeDomainService(
    private val cafeRepository: CafeRepository
) {
    suspend fun getCafeByUrl(url: String): Cafe? {
        return cafeRepository.findById(url)
    }

    fun getCafesByCategoryId(categoryId: Long): Flow<Cafe> {
        return cafeRepository.findByCategoryId(categoryId)
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
