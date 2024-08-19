package com.minturtle.cs.problem2.entity

import java.lang.RuntimeException

class Account(
    private var balance : Int
){

    fun deposit(amount: Int){
        if(balance < amount){
            throw RuntimeException()
        }
        Thread.sleep(10)
        balance -= amount
    }

    fun getBalance(): Int{
        return balance
    }
}