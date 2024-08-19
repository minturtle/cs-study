package com.minturtle.cs.problem2.repository

import com.minturtle.cs.problem2.entity.Account
import org.springframework.stereotype.Repository
import java.util.concurrent.atomic.AtomicInteger

@Repository
class AccountRepository {
    private val accountDao : MutableMap<Long, Account> = HashMap()
    private val idSeq = AtomicInteger(0)

    fun save( entity: Account){
        accountDao[idSeq.incrementAndGet().toLong()] = entity
    }

    fun findById(id: Long): Account?{
        return accountDao[id]
    }

    fun findAll(): Collection<Account>{
        return accountDao.values
    }

    fun deleteAll(){
        accountDao.clear()
    }

}