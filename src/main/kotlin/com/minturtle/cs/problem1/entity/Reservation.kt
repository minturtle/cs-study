package com.minturtle.cs.problem1.entity

import java.util.Objects

class Reservation(
    private val dateId: Long,
    private val seatId: Long
){

    override fun equals(other: Any?): Boolean {
        if(other !is Reservation){
            return false
        }

        return this.dateId == other.dateId && this.seatId == other.seatId
    }

    override fun hashCode(): Int {
        return Objects.hash(this.dateId, this.seatId)
    }
}