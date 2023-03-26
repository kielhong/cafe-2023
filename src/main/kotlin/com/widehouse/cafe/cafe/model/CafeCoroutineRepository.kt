package com.widehouse.cafe.cafe.model

import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface CafeCoroutineRepository : CoroutineCrudRepository<Cafe, String>
