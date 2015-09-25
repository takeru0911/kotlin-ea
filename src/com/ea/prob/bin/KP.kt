package com.ea.prob.bin

import com.ea.algor.Solution
import com.ea.filterwithIndexed
import com.ea.prob.Problem
import java.util.*

/**
 * Created by taker on 2015/09/25.
 */
class KP(dimension: Int): Problem(){
    private val weight = Array(dimension){
        (Math.random() * 100).toInt()
    }
    private val value = Array(dimension){
        (Math.random() * 100).toInt()
    }
    private val capacity = weight.sum() / 2

    override fun evaluation(solution: Solution): Double{
        val vars = solution.vars

        val sumOfWeight = weight.filterwithIndexed {
            idx, v -> vars[idx] == 1.0
        }.sum()
        val sumOfValue = value.filterwithIndexed {
            idx, v -> vars[idx] == 1.0
        }.sum()

        return if(sumOfWeight > capacity){
            0.0
        }else{
            sumOfValue.toDouble()
        }
    }
}