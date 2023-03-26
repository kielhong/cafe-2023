package com.widehouse.cafe.cafe.model

import com.widehouse.cafe.cafe.model.Cafe
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface CafeCoroutineRepository : CoroutineCrudRepository<Cafe, String>
