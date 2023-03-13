package com.widehouse.cafe.article

import com.widehouse.cafe.article.model.Article
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ArticleRepository : CoroutineCrudRepository<Article, Long>
