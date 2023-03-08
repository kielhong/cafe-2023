package com.widehouse.cafe.cafe

import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface CafeRepository : ReactiveMongoRepository<Cafe, String>
