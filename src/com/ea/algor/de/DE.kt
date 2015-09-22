package com.ea.algor.de

import com.ea.algor.Algorithm
import com.ea.algor.Solution
import com.ea.prob.Problem
import com.ea.randomSelector
import com.ea.with2Step

/**
 * Created by taker on 2015/09/22.
 */
open class DE(val problem: Problem, val prop: PropertyDE) :Algorithm{
    inline private var trialSolutions: Array<Solution> = Array(prop.numOfPopulations){
        createSolutionByRandom()
    }
    //temp initialization by random
    inline private var bestSolutions: Array<Solution> = Array(prop.numOfPopulations){
        createSolutionByRandom()
    }

    inline private val createSolutionByRandom: () -> Solution = {
        var vars = Array(prop.numOfDimension){
            Math.random()
        }
        vars.forEachIndexed {idx, v ->
            revVarBoundary(v, prop.varRangeMax[idx], prop.varRangeMin[idx])
        }
        Solution(vars, 0.0);

    }

    inline private val revVarBoundary: (v: Double, max: Double, min: Double) -> Double = {
        v, max, min ->
        when{
            v > max -> max
            v < min -> min
            else -> v
        }
    }
   inline var createMutationVector: () -> Solution = {
        ->
        val selectedVector = randomSelector(prop.numOfDiffSol, 0, prop.numOfPopulations)
        var mutantVar = Array(prop.numOfDimension){

            var index = it
            val sum = selectedVector.with2Step {
                idx1, idx2 ->
                val rightVecValue = bestSolutions[idx1].vars[index]
                val leftVecValue = bestSolutions[idx2].vars[index]
                rightVecValue - leftVecValue
            }.sum()

            sum * prop.F
        }
       Solution(mutantVar, 0.0)
    }


    /**
     *basic DE/2/bin crossover
     */
    inline var crossoverOperation: (sol1: Solution, sol2: Solution) -> Solution = {
        sol1, sol2 ->
        val crossoveredArray = sol1.merge(sol2, {
            v1, v2 ->
            if(prop.CR > Math.random()){
                v1
            }else{
                v2
            }
        })
        Solution(crossoveredArray, 0.0)
    }


    override fun crossover(target: Solution, sol2: Solution): Solution{
        return crossoverOperation(target, sol2)
    }

    override fun mutation(trial: Solution, mutant: Solution): Solution{
        var vars: Array<Double> = trial.merge(mutant,{
            v1, v2 ->
            v1 - v2
        })
        return Solution(vars, 0.0)
    }

    override fun selection(sol1: Solution, sol2: Solution): Solution{
        return if(sol1.fitness > sol2.fitness){
            sol1
        }else{
            sol2
        }
    }

}