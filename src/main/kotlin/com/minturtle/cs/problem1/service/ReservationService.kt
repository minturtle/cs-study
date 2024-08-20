package com.minturtle.cs.problem1.service

import com.minturtle.cs.problem1.entity.Reservation
import com.minturtle.cs.problem1.repository.ReservationRepository
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import java.util.concurrent.atomic.AtomicInteger


@Service
class ReservationService(
    private val reservationRepository: ReservationRepository
) {

    private val idSeq = AtomicInteger(0)

    fun save(dateId : Long, seatId: Long){
        val newEntity = Reservation(idSeq.incrementAndGet().toString(), dateId, seatId)

        reservationRepository.save(newEntity)
    }

    fun findById(id : String): Reservation {
        return reservationRepository.findById(id) ?: throw RuntimeException()
    }

    fun findAll(): Collection<Reservation> {
        return reservationRepository.findAll()
    }
}