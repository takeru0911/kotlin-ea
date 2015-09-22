package com.ea.algor.de

import com.ea.algor.Algorithm
import com.ea.algor.Solution
import com.ea.prob.Problem
import com.ea.prob.Sphere
import com.ea.randomSelector
import com.ea.revVarBoundary
import com.ea.with2Step
import java.util.*

/**
 * Created by taker on 2015/09/22.
 */
open class DE(val problem: Problem, val prop: PropertyDE) :Algorithm{
    private var curEvaluateIndex: Int = -1;

    inline private val createSolutionByRandom: () -> Solution = {
        val vars = Array(prop.numOfDimension){
            Math.random() * (prop.varRangeMax[it] - prop.varRangeMin[it]) + prop.varRangeMin[it]
        }
        val revVars = vars.mapIndexed { idx, v ->
            revVarBoundary(v, prop.varRangeMax[idx], prop.varRangeMin[idx])
        }.toTypedArray()

        Solution(revVars);

    }
    inline private var trialSolutions: Array<Solution> = Array(prop.numOfPopulations){
        createSolutionByRandom()
    }
    //temp initialization by random
    inline private var bestSolutions: Array<Solution> = Array(prop.numOfPopulations){
        createSolutionByRandom()
    }

   inline var createMutationVector: () -> Solution = {
       ->
       var selectedVector = randomSelector(prop.numOfDiffSol * 2, 0, prop.numOfPopulations)
       var selectedBaseVector = -1
       do {
           selectedVector = randomSelector(prop.numOfDiffSol * 2, 0, prop.numOfPopulations)
           selectedBaseVector = (Math.random() * prop.numOfPopulations).toInt()
       }while(selectedVector.contains(curEvaluateIndex) && selectedVector.contains(selectedBaseVector))

       val selectedBaseVars = bestSolutions[selectedBaseVector].vars
       val mutantVar = Array(prop.numOfDimension){
            val index = it
            val sum = selectedVector.with2Step {
                idx1, idx2 ->
                val rightVecValue = bestSolutions[idx1].vars[index]
                val leftVecValue = bestSolutions[idx2].vars[index]
                rightVecValue - leftVecValue
            }.sum()
            sum * prop.F + selectedBaseVars[index]

       }
       Solution(mutantVar)
    }


    /**
     *basic DE/2/bin crossover
     */
    inline var crossoverOperation: (sol1: Solution, sol2: Solution) -> Solution = {
        sol1, sol2 ->
        val selectedCrossoverPoint = (Math.random() * prop.numOfDimension).toInt()
        val crossoveredArray = sol1.merge(sol2, {
            v1, v2 ->
            if(prop.CR > Math.random()){
                v2
            }else{
                v1
            }
        })
        crossoveredArray[selectedCrossoverPoint] = sol2.vars[selectedCrossoverPoint]

        val revVars = crossoveredArray.mapIndexed { idx, v ->
            revVarBoundary(v, prop.varRangeMax[idx], prop.varRangeMin[idx])
        }.toTypedArray()

        Solution(revVars)
    }

    override fun crossover(target: Solution, sol2: Solution): Solution{
        return crossoverOperation(target, sol2)
    }

    override fun mutation(trial: Solution): Solution{
        val mutant = createMutationVector()

        return mutant
    }

    override fun selection(sol1: Solution, sol2: Solution): Solution{
        return if(sol1.fitness < sol2.fitness){
            sol1
        }else{
            sol2
        }
    }

    fun run(){
        //init
        trialSolutions.forEach { it.fitness = problem.evaluation(it) }
        bestSolutions = trialSolutions.clone()
        var best = Double.MAX_VALUE;
        var count = 0;

        while(count < prop.functionEvaluations){
            bestSolutions.forEachIndexed { idx, solution ->
                curEvaluateIndex = idx
                val mutant = mutation(solution)
                val trialVector = crossover(solution, mutant)
                trialVector.fitness = problem.evaluation(trialVector)
                trialSolutions[idx] = trialVector
            }
            trialSolutions.forEachIndexed { idx, solution ->
                bestSolutions[idx] = selection(bestSolutions[idx], solution)
                if(best > solution.fitness) {
                    best = solution.fitness
                }
                count++
            }
            println(best)
        }
        println(count)
    }
}

fun main(args: Array<String>){

    val property = PropertyDE(
            numOfDimension = 3,
            numOfDiffSol = 1,
            numOfPopulations = 5,
            CR = 0.1,
            F = 0.9,
            functionEvaluations = 40000,
            varRangeMax = Array(3){
                5.12
            },
            varRangeMin = Array(3){
                -5.12
            }
    )

    val de = DE(Sphere(), property);
    de.run()
}