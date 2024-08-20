package com.minturtle.cs.problem1.repository

import com.minturtle.cs.problem1.entity.Reservation
import com.minturtle.cs.problem1.exception.MyDataIntegrationException
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger


@Repository
class ReservationRepository {

    private val reservationDao : MutableMap<String, Reservation> = ConcurrentHashMap()

    fun save(entity: Reservation){
        Thread.sleep(10) // 실제 DB의 Blocking
        val previousValue = reservationDao.putIfAbsent(entity.id, entity)
        if (previousValue != null) {
            throw MyDataIntegrationException()
        }
    }

    fun findById(id: String): Reservation?{
        Thread.sleep(10) // 실제 DB의 Blocking
        return reservationDao[id]
    }

    fun findAll(): Collection<Reservation>{
        Thread.sleep(100) // 실제 DB의 Blocking
        return reservationDao.values
    }

    fun deleteAll(){
        reservationDao.clear()
    }
}