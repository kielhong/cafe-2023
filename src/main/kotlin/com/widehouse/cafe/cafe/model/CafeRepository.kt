package com.widehouse.cafe.cafe.model

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface CafeRepository : CoroutineCrudRepository<Cafe, String> {
    suspend fun findByUrl(url: String): Cafe?
    fun findByCategoryId(categoryId: Long): Flow<Cafe>
}
