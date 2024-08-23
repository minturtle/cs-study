package com.minturtle.cs.problem3.repository

import com.minturtle.cs.problem3.entity.Article
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap


@Repository
class ArticleRepository {

    private val articleDao : MutableMap<Long, Article> = ConcurrentHashMap()


    fun save(article : Article){
        articleDao[article.articleNo] = article
    }

    fun findKeys(): Set<Long> {
        return articleDao.keys
    }

}