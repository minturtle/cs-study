package com.minturtle.cs.problem3.service

import com.minturtle.cs.problem3.entity.Article
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random


@Component
class ArticleCrawler {

    var lastArticleNum = AtomicInteger(10)

    fun crawlList(size: Int = 5): List<String>{
        // CRAWL SOMETHING ...
        Thread.sleep(100)
        lastArticleNum.addAndGet(Random.nextInt(10, 21))
        val articleNumberList = ((lastArticleNum.get() - size)..lastArticleNum.get()).toList()

        return articleNumberList.map { "/news/$it" }
    }

    fun crawlDetail(articleNo : Long): Article {
        // CRAWL SOMETHING ...
        Thread.sleep(100)

        return Article(
            articleNo,
            "title$articleNo",
            "author$articleNo",
            "content$articleNo"
        )
    }

}