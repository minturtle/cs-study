package com.minturtle.cs.problem1.service

import com.minturtle.cs.problem1.entity.Reservation
import com.minturtle.cs.problem1.repository.ReservationRepository
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import kotlin.RuntimeException

@Service
class ReservationService(
    private val reservationRepository: ReservationRepository
) {
    private val idSeq = AtomicInteger(0)
    private val reservationTable : MutableMap<String, Boolean> = ConcurrentHashMap()

    fun save(dateId : Long, seatId: Long){
        val id = "$dateId.$seatId"
        val newEntity = Reservation(id, dateId, seatId)

        if (reservationTable.putIfAbsent(id, true) == null) {
            findAndSave(newEntity)

            return
        }

        throw RuntimeException()
    }

    @Synchronized fun findAndSave(entity: Reservation) {
        if (findById(entity.id) == null) {
            reservationRepository.save(entity)

            return
        }

        throw RuntimeException("이미 예약된 좌석입니다");
    }

    fun findById(id: String): Reservation? {
        return reservationRepository.findById(id)
    }

    fun findAll(): Collection<Reservation> {
        return reservationRepository.findAll()
    }
}