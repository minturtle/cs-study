package com.minturtle.cs.problem1.service

import com.minturtle.cs.problem1.entity.Reservation
import com.minturtle.cs.problem1.exception.MyDataIntegrationException
import com.minturtle.cs.problem1.repository.ReservationRepository
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import java.util.concurrent.atomic.AtomicInteger


@Service
class ReservationService(
    private val reservationRepository: ReservationRepository,
    private val cacheManager: ReservationCacheService
) {

    fun save(dateId : Long, seatId: Long){
        val id = createReservationId(dateId, seatId)

        if(findById(id) != null){
            throw RuntimeException()
        }

        val newEntity = Reservation(
            id,
            dateId,
            seatId
        )

        try{
            reservationRepository.save(newEntity)
            cacheManager.add(newEntity.id, newEntity)
        }catch (e: MyDataIntegrationException){
            throw RuntimeException()
        }

    }

    fun findById(id : String): Reservation? {
        var tmp = cacheManager.get(id)

        if(tmp != null){
            return tmp
        }

        tmp = reservationRepository.findById(id)
        if(tmp != null){
            cacheManager.add(id, tmp)
            return tmp
        }

        return null
    }

    fun findAll(): Collection<Reservation> {
        return reservationRepository.findAll()
    }

    private fun createReservationId(dateId : Long, seatId: Long): String{
        return "RESERVATION_${dateId}_${seatId}"
    }
}