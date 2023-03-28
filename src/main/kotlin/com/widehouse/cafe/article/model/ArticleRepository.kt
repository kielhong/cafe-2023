package com.widehouse.cafe.article.model

import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ArticleRepository : CoroutineCrudRepository<Article, Long>
