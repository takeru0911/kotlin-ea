package com.ea.algor.de

import com.ea.algor.Algorithm
import com.ea.algor.Solution
import com.ea.prob.Problem

/**
 * Created by taker on 2015/09/22.
 */
open class DE(problem: Problem) :Algorithm{
    private val CR: Double = 0.1
    //extention function list<Double> to array<Double>
    fun List<Double>.asArray(): Array<Double>  {
        var array: Array<Double> = Array<Double>(this.size()){
            0.0
        }
        for((idx, value) in this.withIndex()){
            array[idx] = value;
        }
        return array
    }
    //basic DE/2/bin crossover
    private val basicCrossover: (sol1: Solution, sol2: Solution) -> Solution = {
        (sol1, sol2) ->
        val var1 = sol1.vars
        val var2 = sol2.vars
        val crossovered: List<Double> =  var1.merge(var2){
            (v1, v2) ->
            if(0.1 > Math.random()){
                v1
            }else{
                v2
            }
        }
        Solution(crossovered.asArray(), 0.0)
    }

    override protected fun crossover(sol1: Solution, sol2: Solution): Solution{
        return this.crossover(sol1, sol2, this.basicCrossover);
    }
    private fun crossover(sol1: Solution, sol2: Solution, func: (sol1: Solution, sol2: Solution) -> Solution): Solution{
        return func(sol1, sol2)
    }

    override protected  fun mutation(sol1: Solution, sol2: Solution): Solution{

    }
    override protected  fun selection(sol1: Solution, sol2: Solution): Solution{

    }

}