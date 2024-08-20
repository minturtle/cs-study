package com.minturtle.cs.problem1.service

import com.minturtle.cs.problem1.entity.Reservation
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.TemporalAmount
import java.util.stream.Stream
import javax.xml.transform.stream.StreamResult

class ReservationCacheServiceTest{

    private val reservationCacheService = ReservationCacheService()


    @Test
    fun `캐시를 저장하고 조회할 수 있다`(){
        val reservation = Reservation("id", 1L, 2L)

        reservationCacheService.add(
            reservation.id,
            reservation
        )

        assertThat(reservationCacheService.get(reservation.id))
            .isEqualTo(reservation)
    }

    @Test
    fun `캐시의 ttl이 만료된 경우 null을 리턴한다`(){
        val reservation = Reservation("id", 1L, 2L)

        val startTime = LocalDateTime.MIN

        reservationCacheService.add(
            reservation.id,
            reservation,
            startAt = startTime
        )

        assertThat(reservationCacheService.get(reservation.id))
            .isNull()

    }

    @ParameterizedTest
    @MethodSource("getTTLAndExpectedResult")
    fun `캐시의 TTL을 설정할 수 있다`(ttl: TemporalAmount, expected: Boolean){
        val reservation = Reservation("id", 1L, 2L)

        val addDateTime = LocalDateTime.now().minus(Duration.ofMinutes(30))

        reservationCacheService.add(
            reservation.id,
            reservation,
            ttl,
            addDateTime
        )

        if(expected){
            assertThat(reservationCacheService.get(reservation.id)).isNotNull()
            return
        }

        assertThat(reservationCacheService.get(reservation.id)).isNull()



    }

    companion object{
        @JvmStatic
        private fun getTTLAndExpectedResult(): Stream<Arguments> {
            return Stream.of(
                Arguments.arguments(Duration.ofMinutes(1), false),
                Arguments.arguments(Duration.ofHours(1), true),
                Arguments.arguments(Duration.ofDays(1), true),
            )
        }
    }

}