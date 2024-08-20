package com.minturtle.cs.problem1

import com.minturtle.cs.problem1.repository.ReservationRepository
import com.minturtle.cs.problem1.service.ReservationService
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

import org.assertj.core.api.Assertions.*
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import java.lang.RuntimeException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

@SpringBootTest
class ReservationServiceTest{

    @Autowired
    private lateinit var reservationService: ReservationService

    @Autowired
    private lateinit var reservationRepository: ReservationRepository

    @BeforeEach
    fun setUp() {
        reservationRepository.deleteAll()
    }

    @Test
    fun `동시에 Reservation 객체가 여러개 요청이 오더라도, 최초의 요청을 제외하곤 모두 실패한다`(){

        val reservationLists = listOf(
            Pair(1L, 2L),
            Pair(3L, 4L),
            Pair(5L, 6L)
        )

        val threadSize = 100
        val executors = Executors.newFixedThreadPool(threadSize)
        val doneSignal = CountDownLatch(threadSize)

        val successCount = AtomicInteger(0)
        val failedCount = AtomicInteger(0)

        val idx = AtomicInteger(0)

        repeat(threadSize){
            executors.execute {
                val idx = idx.incrementAndGet()

                try{
                    val pair = reservationLists[idx % reservationLists.size]
                    reservationService.save(pair.first, pair.second)
                    successCount.incrementAndGet()
                }catch (e : RuntimeException){
                    failedCount.incrementAndGet()
                }finally {
                    doneSignal.countDown()
                }
            }
        }

        doneSignal.await()
        executors.shutdown()

        assertThat(successCount.get()).isEqualTo(reservationLists.size)
        assertThat(failedCount.get()).isEqualTo(threadSize - reservationLists.size)
        assertThat(reservationService.findAll()).extracting("dateId", "seatId").containsExactlyInAnyOrder(
            Tuple.tuple(reservationLists[0].first, reservationLists[0].second),
            Tuple.tuple(reservationLists[1].first, reservationLists[1].second),
            Tuple.tuple(reservationLists[2].first, reservationLists[2].second),
        )
    }


}