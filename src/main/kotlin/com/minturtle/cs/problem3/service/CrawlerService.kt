package com.minturtle.cs.problem3.service

import com.minturtle.cs.problem3.repository.ArticleRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue


@Service
class CrawlerService(
    private val crawler: ArticleCrawler,
    private val articleRepository: ArticleRepository
){

    private val log : Logger = LoggerFactory.getLogger(CrawlerService::class.java)

    fun run(){
        val producerThreadPool = Executors.newFixedThreadPool(1)
        val consumerThreadPool = Executors.newFixedThreadPool(3)
        val queue = LinkedBlockingQueue<Long>()
        val latch = CountDownLatch(1)

        producerThreadPool.execute {
            while (true){
                val crawlList = crawler.crawlList()
                crawlList.asSequence()
                    .map { extractId(it) }
                    .filterNotNull()
                    .filter{ articleRepository.findById(it) == null }.forEach {
                        queue.add(it)
                        log.info("Produce Complete ! {}", it)
                }
                Thread.sleep(1000) // 리스트 크롤링은 1초에 한번만 수행
            }
        }

        consumerThreadPool.execute {
            while(true){
                runCatching {
                    val articleNo = queue.take()

                    articleRepository.save(crawler.crawlDetail(articleNo))
                    log.info("Comsumer Complete ! {}", articleNo)
                }.onFailure { log.warn("Failed: {}", it) }

            }
        }

        latch.await()
    }

    private fun extractId(uri: String): Long? {
        val regex = "/news/(\\d+)$".toRegex()
        val matchResult = regex.find(uri)
        return matchResult?.groupValues?.get(1)?.toLong()
    }

}