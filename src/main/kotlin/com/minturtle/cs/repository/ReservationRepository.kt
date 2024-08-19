package com.minturtle.cs.repository

import com.minturtle.cs.entity.Reservation
import org.springframework.stereotype.Repository
import java.awt.List


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