package com.minturtle.cs.problem2

import com.minturtle.cs.problem2.entity.Account
import com.minturtle.cs.problem2.repository.AccountRepository
import com.minturtle.cs.problem2.service.AccountService
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.platform.commons.logging.Logger
import org.junit.platform.commons.logging.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors


@SpringBootTest
class AccountServiceTest{

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var accountRepository: AccountRepository

    private val log: Logger = LoggerFactory.getLogger(AccountService::class.java)


    @BeforeEach
    fun setUp() {
        accountRepository.deleteAll()
    }

    @Test
    fun `동시에 계좌에 접근할 시, 결제 후 남은 금액이 올바르게 남아 있어야 한다`(){
        val id = 1L
        val initBalance = 100000
        val threadSize = 100
        val depositSizePerThread = 100

        val account = Account(initBalance)
        accountService.save(account)

        val executors =  Executors.newFixedThreadPool(threadSize)
        val doneSignal = CountDownLatch(threadSize)

        repeat(threadSize){
            executors.execute{
                runCatching {
                    accountService.deposit(id, depositSizePerThread)
                }.run {
                    doneSignal.countDown()
                    log.info{"${Thread.currentThread().id} completed"}
                }
            }
        }

        doneSignal.await()
        executors.shutdown()

        val actual = accountService.findById(id)

        assertThat(actual.getBalance()).isEqualTo(initBalance - (depositSizePerThread * threadSize))
    }

}