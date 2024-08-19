package com.minturtle.cs.problem1

import com.minturtle.cs.problem1.entity.Reservation
import com.minturtle.cs.problem1.repository.ReservationRepository
import com.minturtle.cs.problem1.service.ReservationCacheService
import com.minturtle.cs.problem1.service.ReservationService
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

import org.assertj.core.api.Assertions.*
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

    @Autowired
    private lateinit var cacheService: ReservationCacheService

    @BeforeEach
    fun setUp() {
        reservationRepository.deleteAll()
        cacheService.clearAll()
    }
    @Test
    fun `Reservation 객체를 저장하고 조회할 수 있다`(){
        val id = 1L
        val dateId = 2L
        val seatId = 3L

        val entity = Reservation(dateId, seatId)

        reservationService.save(entity)


        val actual = reservationService.findAll()

        assertThat(actual).extracting( "dateId", "seatId")
            .containsExactly( tuple(dateId, seatId))
    }


    @Test
    fun `동시에 Reservation 객체가 여러개 요청이 오더라도, 최초의 요청을 제외하곤 모두 실패한다`(){

        val reservationLists = listOf(
            Reservation(1L, 2L),
            Reservation(3L, 4L),
            Reservation(5L, 6L)
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
                    val reservation = reservationLists[idx % reservationLists.size]
                    reservationService.save(reservation)
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

        assertThat(reservationService.findAll()).containsExactlyInAnyOrderElementsOf(reservationLists)
    }

    @Test
    fun `캐시에 저장된 Reservation 객체를 DB에 저장할 수 있다`(){
        val reservation = Reservation(1L, 2L)

        reservationService.save(reservation)

        reservationService.toPermanent(reservation)

        assertThat(reservationRepository.findAll()).contains(reservation)
        assertThat(cacheService.getAll()).doesNotContain(reservation)
    }


}