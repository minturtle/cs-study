package com.minturtle.cs.problem1.repository

import com.minturtle.cs.problem1.entity.Reservation
import com.minturtle.cs.problem1.exception.MyDataIntegrationException
import org.springframework.stereotype.Repository
import java.util.concurrent.atomic.AtomicInteger


@Repository
class ReservationRepository {

    private val reservationDao : MutableMap<String, Reservation> = HashMap()

    fun save(entity: Reservation){
        val previousValue = reservationDao.putIfAbsent(entity.id, entity)
        if (previousValue != null) {
            throw MyDataIntegrationException()
        }
    }

    fun findById(id: String): Reservation?{
        return reservationDao[id]
    }

    fun findAll(): Collection<Reservation>{
        return reservationDao.values
    }

    fun deleteAll(){
        reservationDao.clear()
    }
}