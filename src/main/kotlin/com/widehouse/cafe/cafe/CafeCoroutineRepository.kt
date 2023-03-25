package com.widehouse.cafe.cafe

import com.widehouse.cafe.cafe.model.Cafe
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface CafeCoroutineRepository : CoroutineCrudRepository<Cafe, String>
