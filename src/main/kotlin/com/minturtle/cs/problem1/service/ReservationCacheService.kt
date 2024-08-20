package com.minturtle.cs.problem1.service

import com.minturtle.cs.problem1.entity.Reservation
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.TemporalAmount
import java.util.concurrent.ConcurrentHashMap

@Component
class ReservationCacheService {

    private val cache: MutableMap<String, Pair<LocalDateTime, Reservation>> = ConcurrentHashMap()

    fun get(key: String): Reservation?{
        if(cache[key]?.first?.isBefore(LocalDateTime.now()) == true){
            cache.remove(key)
            return null
        }
        return cache[key]?.second
    }

    fun add(
        key: String,
        value: Reservation,
        ttl: TemporalAmount = Duration.ofHours(1),
        startAt: LocalDateTime = LocalDateTime.now()
    ) : Boolean{
        val pair = Pair(startAt.plus(ttl), value)
        return cache.putIfAbsent(key, pair)?.let { return false } ?: true
    }
}