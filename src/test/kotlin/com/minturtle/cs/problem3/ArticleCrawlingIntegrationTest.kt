package com.minturtle.cs.problem3

import com.minturtle.cs.problem3.repository.ArticleRepository
import com.minturtle.cs.problem3.service.ArticleCrawler
import com.minturtle.cs.problem3.service.CrawlerService
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.time.Duration.Companion.seconds

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@SpringBootTest
class ArticleCrawlingIntegrationTest {

    @Autowired
    private lateinit var crawlerService: CrawlerService

    @Autowired
    private lateinit var articleRepository: ArticleRepository

    @Autowired
    private lateinit var articleCrawler: ArticleCrawler

    @Test
    fun `30초 동안 크롤링을 실시해 DB에 아티클을 저장할 수 있다`() {
        val executor = Executors.newSingleThreadExecutor()

        try {
            val future = executor.submit {
                crawlerService.run()
            }
            Thread.sleep(30_000)
            future.cancel(true)


            val actual = articleRepository.findKeys()
            assertThat(actual).containsAll((10L..articleCrawler.lastArticleNum.get()).toList())
        } finally {
            executor.shutdownNow()
            executor.awaitTermination(5, TimeUnit.SECONDS)
        }
    }


}