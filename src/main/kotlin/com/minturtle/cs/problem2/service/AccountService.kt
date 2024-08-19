package com.minturtle.cs.problem2.service

import com.minturtle.cs.problem2.entity.Account
import com.minturtle.cs.problem2.repository.AccountRepository
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import java.util.concurrent.atomic.AtomicInteger


@Service
class AccountService(
    private val accountRepository: AccountRepository
){

    private val idSeq = AtomicInteger(0)

    fun save(entity: Account){
        accountRepository.save(idSeq.addAndGet(1).toLong(), entity)
    }

    fun deposit(id: Long, amount: Int){
        val account = findById(id)

        account.deposit(amount)
    }

    fun findById(id : Long): Account {
        return accountRepository.findById(id) ?: throw RuntimeException()
    }

    fun findAll(): Collection<Account>{
        return accountRepository.findAll()
    }

}