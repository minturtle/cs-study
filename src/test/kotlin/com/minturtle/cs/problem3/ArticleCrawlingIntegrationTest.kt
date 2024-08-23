package com.minturtle.cs.problem3

import com.minturtle.cs.problem3.repository.ArticleRepository
import com.minturtle.cs.problem3.service.ArticleCrawler
import com.minturtle.cs.problem3.service.CrawlerService
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.time.Duration.Companion.seconds

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

@SpringBootTest
class ArticleCrawlingIntegrationTest {

    @Autowired
    private lateinit var crawlerService: CrawlerService

    @Autowired
    private lateinit var articleRepository: ArticleRepository

    @Autowired
    private lateinit var articleCrawler: ArticleCrawler

    @Test
    fun `60초 동안 크롤링을 실시해 DB에 아티클을 저장할 수 있다`() = runTest {
        withTimeout(60.seconds) {
            launch {
                crawlerService.run()
            }
        }.join()

        val actual = articleRepository.findKeys()

        assertThat(actual).containsAll((10L..articleCrawler.lastArticleNum.get()).toList())
    }


}