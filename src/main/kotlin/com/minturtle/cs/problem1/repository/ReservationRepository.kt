package com.minturtle.cs.problem1.repository

import com.minturtle.cs.problem1.entity.Reservation
import org.springframework.stereotype.Repository


@Repository
class ReservationRepository {

    private val reservationDao : MutableMap<Long, Reservation> = HashMap()

    fun save(id : Long, entity: Reservation){
        reservationDao[id] = entity
    }

    fun findById(id: Long): Reservation?{
        return reservationDao[id]
    }

    fun findAll(): Collection<Reservation>{
        return reservationDao.values
    }
}