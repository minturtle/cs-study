package com.minturtle.cs.service

import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class LockService {

    private val lockMap: MutableMap<String, Boolean> = ConcurrentHashMap();

    fun acquire(lock: String): Boolean{
        // putIfAbsent는 기존에 값이 있으면 기존 값을, 아니면 null을 리턴함.
        return lockMap.putIfAbsent(lock, false) ?: true
    }

    fun release(lock: String){
        lockMap.put(lock, true)
    }
}