package com.minturtle.cs.problem1.service

import com.minturtle.cs.problem1.entity.Reservation
import com.minturtle.cs.problem1.repository.ReservationRepository
import org.springframework.stereotype.Service
import java.lang.RuntimeException


@Service
class ReservationService(
    private val reservationRepository: ReservationRepository,
    private val cache: ReservationCacheService
) {


    companion object{
        private val RESERVATION_CACHE_PREFIX = "RESERVATION_"

    }

    fun save(entity: Reservation){
        val key = RESERVATION_CACHE_PREFIX + entity.hashCode()

        if (reservationRepository.findAll().contains(entity)){
            throw RuntimeException()
        }
        if(cache.add(key, entity)){
            return
        }
        throw RuntimeException()
    }

    fun toPermanent(entity: Reservation){
        val key = RESERVATION_CACHE_PREFIX + entity.hashCode()
        val cacheValue = cache.get(key) ?: throw RuntimeException()

        reservationRepository.save(cacheValue)
        cache.remove(key)
    }



    fun findById(id : Long): Reservation {
        return reservationRepository.findById(id) ?: throw RuntimeException()
    }

    fun findAll(): Collection<Reservation> {
        return reservationRepository.findAll() + cache.getAll()
    }
}