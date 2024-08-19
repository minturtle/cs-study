package com.minturtle.cs.problem1.service

import com.minturtle.cs.problem1.entity.Reservation
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap


@Component
class ReservationCacheService {

    private val cache : MutableMap<String, Reservation> = ConcurrentHashMap()

    fun add(key: String, entity: Reservation): Boolean{
        return cache.putIfAbsent(key, entity)?.let{ false } ?: true
    }

    fun getAll(): Collection<Reservation>{
        return cache.values
    }

    fun get(key: String): Reservation?{
        return cache[key]
    }

    fun remove(key: String){
        cache.remove(key)
    }

    fun clearAll(){
        cache.clear()
    }
}