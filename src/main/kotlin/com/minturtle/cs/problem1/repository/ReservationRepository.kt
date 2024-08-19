package com.minturtle.cs.problem1.repository

import com.minturtle.cs.problem1.entity.Reservation
import org.springframework.stereotype.Repository
import java.util.concurrent.atomic.AtomicInteger


@Repository
class ReservationRepository {

    private val reservationDao : MutableMap<Long, Reservation> = HashMap()
    private val idSeq = AtomicInteger(0)

    fun save( entity: Reservation){
        reservationDao[idSeq.incrementAndGet().toLong()] = entity
    }

    fun findById(id: Long): Reservation?{
        return reservationDao[id]
    }

    fun findAll(): Collection<Reservation>{
        return reservationDao.values
    }

    fun deleteAll(){
        reservationDao.clear()
    }
}