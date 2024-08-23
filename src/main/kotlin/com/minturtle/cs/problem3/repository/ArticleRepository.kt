package com.minturtle.cs.problem3.repository

import com.minturtle.cs.problem3.entity.Article
import com.minturtle.cs.problem3.exception.Problem3DataIntegrityViolationException
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap


@Repository
class ArticleRepository {

    private val articleDao : MutableMap<Long, Article> = ConcurrentHashMap()


    fun save(article : Article){
        articleDao.putIfAbsent(article.articleNo, article)
            ?.let {
                throw Problem3DataIntegrityViolationException()
            }
    }

    fun findKeys(): Set<Long> {
        return articleDao.keys
    }

    fun findById(id: Long) : Article?{
        return articleDao[id]
    }

}