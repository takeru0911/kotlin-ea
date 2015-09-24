package com.ea.algor.ga

import com.ea.algor.Algorithm
import com.ea.algor.Solution
import com.ea.prob.Problem

/**
 * Created by taker on 2015/09/24.
 */

fun makeCrossoverMask(maskLength: Int): Array<Int>{
    val mask = Array(maskLength){
        0
    }
    for(i in 0 .. maskLength - 1){
        mask[i] = if(Math.random() > 0.5){
            0
        }else{
            1
        }
    }

    return mask
}

class GA(problem: Problem,prop: PropertyGA): Algorithm{
    var curEvaluatedIndex = -1
    private var crossoverMask: Array<Int>? = null

    /**
     * uniform crossover
     */
    val crossoverOperation: (Solution, Solution) -> Solution = {
        parent1, parent2 ->
        val crossoveredVars = parent1.mergeWithIndexed(parent2, {
            idx, v1, v2 ->
            if (crossoverMask!![idx] == 0) {
                v1
            } else {
                v2
            }
        })
        Solution(crossoveredVars)
    }

    override fun crossover(parent1: Solution, parent2: Solution): Solution{
        return crossoverOperation(parent1, parent2)
    }
    /*
     * bit flip
     */
    val mutateOperation: (Solution) -> Solution = {
        target ->
        val mutatedVars = target.vars.map {
            v ->
            if(prop.CR > Math.random()){
                Math.abs(v - 1)
            }else{
                v
            }
        }.toTypedArray()

        Solution(mutatedVars)
    }
    override fun mutation(target: Solution): Solution{
        return mutateOperation(target)
    }

    /**
     * replace
     */
    val selectionOperation: (Solution, Solution) -> Solution = {
        base, trial ->
        trial
    }

    override fun selection(base: Solution, trial: Solution): Solution{
        return selectionOperation(base, trial)
    }
}