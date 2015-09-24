package com.ea.algor


/**
 * Created by taker on 2015/09/22.
 */
data class Solution(val vars: Array<Double>, var fitness: Double = 0.0) {
    fun merge(otherSol: Solution, func: (Double, Double) -> Double): Array<Double> {
        val selfVars = this.vars
        val otherVars = otherSol.vars
        val mergedList: List<Double> = selfVars.merge(otherVars, func)

        return mergedList.toTypedArray()
    }
    fun mergeWithIndexed(otherSol: Solution, func: (Int, Double, Double) -> Double): Array<Double>{
        val selfVars = this.vars
        val otherVars = otherSol.vars
        val mergedArray = Array(selfVars.size()){
            0.0
        }
        check(selfVars.size() == otherVars.size())

        for(i in 0 .. selfVars.size() - 1){
            mergedArray[i] = func(i, selfVars[i], otherVars[i])
        }

        return mergedArray
    }
}