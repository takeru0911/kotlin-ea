package com.ea.algor.de

import com.ea.*
import com.ea.algor.Algorithm
import com.ea.algor.Solution
import com.ea.prob.Problem
import com.ea.prob.Sphere

import java.util.*

/**
 * Created by taker on 2015/09/22.
 */
open class DE(val problem: Problem, val prop: PropertyDE) : Algorithm {
    private var curEvaluateIndex: Int = -1;

    private val trialSolutions: Array<Solution> = Array(prop.numOfPopulations) {
        createSolutionByRandom(prop.varRangeMin, prop.varRangeMax)
    }
    //temp initialization by random
    private var bestSolutions: Array<Solution> = Array(prop.numOfPopulations) {
        createSolutionByRandom(prop.varRangeMin, prop.varRangeMax)
    }

    inline var createMutationVector: () -> Solution = {
        ->
        var selectedVector = randomSelector(prop.numOfDiffSol * 2, 0, prop.numOfPopulations)
        var selectedBaseVector = -1
        //TODO: ‚à‚Á‚Æ‚¢‚¢‘‚«•û‚È‚¢‚Ì‚©
        do {
            selectedVector = randomSelector(prop.numOfDiffSol * 2, 0, prop.numOfPopulations)
            selectedBaseVector = randomToInt(0, prop.numOfPopulations);
        } while (selectedVector.contains(curEvaluateIndex) && selectedVector.contains(selectedBaseVector))

        val selectedBaseVars = bestSolutions[selectedBaseVector].vars
        //TODO: ˆÈ‰ºØ‚è—£‚·H
        val mutantVar = Array(prop.numOfDimension) {
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
    inline var crossoverOperation: (trial: Solution, mutant: Solution) -> Solution = {
        sol1, sol2 ->
        val selectedCrossoverPoint = randomToInt(0, prop.numOfDimension)
        val crossoveredArray = sol1.merge(
                sol2,
                {
                    v1, v2 ->
                    getWhichValueByRand(v1, v2, prop.CR)
                }
        )
        crossoveredArray[selectedCrossoverPoint] = sol2.vars[selectedCrossoverPoint]

        val revVars = crossoveredArray.mapIndexed {
            idx,
            v -> revVarBoundary(v, prop.varRangeMax[idx], prop.varRangeMin[idx])
        }.toTypedArray()

        Solution(revVars)
    }

    override fun crossover(trial: Solution, mutant: Solution): Solution {
        return crossoverOperation(trial, mutant)
    }

    override fun mutation(trial: Solution): Solution {
        val mutant = createMutationVector()
        return mutant
    }

    override fun selection(base: Solution, trial: Solution): Solution {
        return if (base.fitness < trial.fitness) {
            base
        } else {
            trial
        }
    }

    fun run() {
        //init
        trialSolutions.forEach { it.fitness = problem.evaluation(it) }
        bestSolutions = trialSolutions.clone()
        var best = Double.MAX_VALUE;
        var count = 0;

        while (count < prop.functionEvaluations) {
            bestSolutions.forEachIndexed { idx, solution ->
                curEvaluateIndex = idx
                val mutant = mutation(solution)
                val trialVector = crossover(solution, mutant)
                trialVector.fitness = problem.evaluation(trialVector)
                trialSolutions[idx] = trialVector
            }
            trialSolutions.forEachIndexed { idx, solution ->
                bestSolutions[idx] = selection(bestSolutions[idx], solution)
                if (best > solution.fitness) {
                    best = solution.fitness
                }
                count++
            }
            println(best)
        }
        println(count)
    }
}

fun main(args: Array<String>) {

    val property = PropertyDE(
            numOfDimension = 3,
            numOfDiffSol = 1,
            numOfPopulations = 5,
            CR = 0.1,
            F = 0.9,
            functionEvaluations = 40000,
            varRangeMax = Array(3) {
                5.12
            },
            varRangeMin = Array(3) {
                -5.12
            }
    )

    val de = DE(Sphere(), property);
    de.run()
}