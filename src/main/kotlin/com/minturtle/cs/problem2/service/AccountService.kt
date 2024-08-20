package com.minturtle.cs.problem2.service

import com.minturtle.cs.problem2.entity.Account
import com.minturtle.cs.problem2.repository.AccountRepository
import org.springframework.stereotype.Service
import java.lang.RuntimeException


@Service
class AccountService(
    private val accountRepository: AccountRepository
){
    fun save(entity: Account){
        accountRepository.save(entity)
    }

    fun deposit(id: Long, amount: Int){
        val lock = Problem2LockService.acquire(id)

        lock.lock()

        val account = findById(id)

        account.deposit(amount)
        lock.unlock()
    }

    fun findById(id : Long): Account {
        return accountRepository.findById(id) ?: throw RuntimeException()
    }

    fun findAll(): Collection<Account>{
        return accountRepository.findAll()
    }

}