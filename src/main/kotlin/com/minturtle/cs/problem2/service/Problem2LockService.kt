package com.minturtle.cs.problem2.service

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock


object Problem2LockService {

    private val locks : MutableMap<Long, ReentrantLock> = ConcurrentHashMap()

    fun acquire(key: Long): ReentrantLock{
        val lock = ReentrantLock(true)

        return locks.putIfAbsent(key, lock) ?: lock
    }

}