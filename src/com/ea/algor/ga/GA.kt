package com.ea.algor.ga

import com.ea.algor.Algorithm
import com.ea.algor.Solution
import com.ea.prob.Problem

/**
 * Created by taker on 2015/09/24.
 */

class GA(problem: Problem,prop: PropertyGA): Algorithm{
    var curEvaluatedIndex = -1

    /**
     * uniform crossover
     */
    val crossoverOperation: (Solution, Solution) -> Solution = {

    }

    fun crossover(parent1: Solution, parent2: Solution): Solution{

    }

    fun mutation(target: Solution): Solution{

    }

    fun selection(base: Solution, trial: Solution): Solution{

    }
}