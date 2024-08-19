package com.minturtle.cs.problem2.repository

import com.minturtle.cs.problem2.entity.Account
import org.springframework.stereotype.Repository

@Repository
class AccountRepository {
    private val accountDao : MutableMap<Long, Account> = HashMap()


    fun save(id : Long, entity: Account){
        accountDao[id] = entity
    }

    fun findById(id: Long): Account?{
        return accountDao[id]
    }

    fun findAll(): Collection<Account>{
        return accountDao.values
    }

}