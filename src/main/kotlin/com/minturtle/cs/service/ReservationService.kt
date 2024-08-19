package com.minturtle.cs.service

import com.minturtle.cs.entity.Reservation
import com.minturtle.cs.repository.ReservationRepository
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import java.util.concurrent.atomic.AtomicInteger


@Service
class ReservationService(
    private val reservationRepository: ReservationRepository
) {
    private val idSeq = AtomicInteger(0)

    fun save(entity: Reservation){
        reservationRepository.save(idSeq.addAndGet(1).toLong(), entity)
    }

    fun findById(id : Long): Reservation{
        return reservationRepository.findById(id) ?: throw RuntimeException()
    }

    fun findAll(): Collection<Reservation> {
        return reservationRepository.findAll()
    }
}