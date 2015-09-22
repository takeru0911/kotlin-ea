package com.ea.algor


/**
 * Created by taker on 2015/09/22.
 */
data class Solution(val vars: Array<Double>, var fitness: Double){
    fun merge(otherSol: Solution, func: (Double, Double) -> Double): Array<Double>{
        val selfVars = this.vars
        val otherVars = otherSol.vars

        val mergedList: List<Double> = selfVars.merge(otherVars, func)
        return mergedList.toTypedArray()
    }
}