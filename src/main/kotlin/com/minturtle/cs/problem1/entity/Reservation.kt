package com.minturtle.cs.problem1.entity

import java.util.Objects

class Reservation(
    val id: String,
    private val dateId: Long,
    private val seatId: Long
){

    override fun equals(other: Any?): Boolean {
        if(other !is Reservation){
            return false
        }

        return this.id == other.id
    }

    override fun hashCode(): Int {
        return Objects.hash(this.id)
    }
}