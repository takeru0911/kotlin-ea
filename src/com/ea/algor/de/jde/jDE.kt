package com.ea.algor.de.jde

import com.ea.*
import com.ea.algor.Solution
import com.ea.algor.de.DE
import com.ea.algor.de.PropertyDE
import com.ea.prob.Sphere;

/**
 * Created by taker on 2015/09/23.
 */

fun main(args: Array<String>){
    val problem = Sphere()
    val propertyDE = PropertyDE(
            numOfDimension = 50,
            numOfDiffSol = 1,
            numOfPopulations = 100,
            CR = 1.0,
            F = 1.0,
            functionEvaluations = 400000,
            varRangeMax = Array(50) {
                5.12
            },
            varRangeMin = Array(50) {
                -5.12
            })

    val solver = DE(problem, propertyDE);

    var CRs: Array<Double> = Array(propertyDE.numOfPopulations){
        Math.random()
    }
    var Fs: Array<Double> = Array(propertyDE.numOfPopulations){
        Math.random()
    }
    val jdeCrossoverOperation: (Solution, Solution) -> Solution = {
        sol1, sol2 ->
        val selectedCrossoverPoint = randomToInt(0, propertyDE.numOfDimension)
        val crossoveredArray = sol1.merge(
                sol2,
                {
                    v1, v2 ->
                    getWhichValueByRand(v1, v2, CRs[solver.curEvaluateIndex])
                }
        )
        CRs[solver.curEvaluateIndex] = if(Math.random() < 0.1) {
            Math.random()
        }else{
            CRs[solver.curEvaluateIndex]
        }
        crossoveredArray[selectedCrossoverPoint] = sol2.vars[selectedCrossoverPoint]

        val revVars = crossoveredArray.mapIndexed {
            idx,
            v ->
            revVarBoundary(v, propertyDE.varRangeMax[idx], propertyDE.varRangeMin[idx])
        }.toTypedArray()

        Solution(revVars)
    }

    var jdeMutate: (Solution, Solution) -> Solution = {
        trial, mutant->
        val mutatedVars = trial.merge(mutant, {
            v1, v2 ->
            v1 + v2 * Fs[solver.curEvaluateIndex]
        })
        Fs[solver.curEvaluateIndex] = if(Math.random() < 0.1) {
            Math.random()
        }else{
            Fs[solver.curEvaluateIndex]
        }

        Solution(mutatedVars)
    }

    solver.mutateSolution = jdeMutate
    solver.crossoverOperation = jdeCrossoverOperation
    solver.run()
}