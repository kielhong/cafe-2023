package com.widehouse.cafe.cafe

import com.widehouse.cafe.cafe.model.Category
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface CategoryRepository : ReactiveMongoRepository<Category, Long>
