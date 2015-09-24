package com.ea.algor.ga

import com.ea.algor.Algorithm
import com.ea.algor.Solution
import com.ea.createBinarySolutionByRandom
import com.ea.prob.Problem
import com.ea.randomSelector
import java.util
import java.util.*

/**
 * Created by taker on 2015/09/24.
 */

fun makeCrossoverMask(maskLength: Int): Array<Int> {
    val mask = Array(maskLength) {
        0
    }
    for (i in 0..maskLength - 1) {
        mask[i] = if (Math.random() > 0.5) {
            0
        } else {
            1
        }
    }

    return mask
}

/**
 * make roulette table for crossover.
 */
fun makeRouletteByFitness(parents: Array<Solution>): Array<Double>{
    val sum = parents.map {
        solution -> solution.fitness
    }.sum()
    val rouletteTable = Array(parents.size()){
        parents[it].fitness / sum
    }

    return rouletteTable
}

/**
 * get crossover solution by roulette
 */

fun decideCrossoverSolutionByRoulette(rouletteTable: Array<Double>): Int {
    val darts = Math.random()
    var sum = 0.0
    var seletecteIndex = -1
    for(i in 0 .. rouletteTable.size() - 1){
        sum += rouletteTable[i]
        if(sum > darts){
            seletecteIndex = i
            break
        }
    }
    return seletecteIndex
}
class GA(val problem: Problem,val prop: PropertyGA) : Algorithm {
    var curEvaluatedIndex = -1
    private var crossoverMask: Array<Int>? = null
    //evaluate solution
    var trialSolutions = Array(prop.numOfPopulation) {
        createBinarySolutionByRandom(prop.numOfDimension)
    }
    //parent solution
    var parentSolutions = Array(prop.numOfPopulation) {
        createBinarySolutionByRandom(prop.numOfDimension)
    }


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

    override fun crossover(parent1: Solution, parent2: Solution): Solution {
        return crossoverOperation(parent1, parent2)
    }

    /*
     * bit flip
     */
    val mutateOperation: (Solution) -> Solution = {
        target ->
        val mutatedVars = target.vars.map {
            v ->
            if (prop.CR > Math.random()) {
                Math.abs(v - 1)
            } else {
                v
            }
        }.toTypedArray()

        Solution(mutatedVars)
    }

    override fun mutation(target: Solution): Solution {
        return mutateOperation(target)
    }

    /**
     * replace
     */
    val selectionOperation: (Solution, Solution) -> Solution = {
        base, trial ->
        trial
    }

    override fun selection(base: Solution, trial: Solution): Solution {
        return selectionOperation(base, trial)
    }

    /**
     * selecting survive solutions.
     * this method is random selection.
     */
    val selectSurviveSolutions: (numOfSuvivor: Int) -> Array<Int> = {
        numOfSurvivor ->
        randomSelector(numOfSurvivor, 0, prop.numOfPopulation)
    }
    fun run() {
        var best = Double.MAX_VALUE
        var count = 0
        check((prop.numOfPopulation * prop.CR) % 2.0 == 0.0)

        parentSolutions.forEachIndexed { idx, solution ->
            curEvaluatedIndex = idx
            solution.fitness = problem.evaluation(solution)
        }
        count += prop.numOfPopulation

        while(count < prop.functionevaluations) {
            val rouletteTable = makeRouletteByFitness(parentSolutions)
            val numOfCrossover = prop.numOfPopulation * prop.CR
            //ê∂ë∂å¬ëÃ
            val survivor: ArrayList<Solution> = ArrayList<Solution>()

            for(i in 0 .. (numOfCrossover / 2 - 1).toInt() ){
                val parent1 = decideCrossoverSolutionByRoulette(rouletteTable);
                var parent2 = parent1
                //ÇQÇ¬ÇÃêeÇÕàŸÇ»ÇÈêeÇ∆Ç∑ÇÈ
                while(parent1 == parent2){
                    parent2 = decideCrossoverSolutionByRoulette(rouletteTable)
                }
                makeCrossoverMask(prop.numOfDimension)
                //crossover
                var child1 = crossover(parentSolutions[parent1], parentSolutions[parent2])
                var child2 = crossover(parentSolutions[parent2], parentSolutions[parent1])
                //mutation
                child1 = mutation(child1)
                child2 = mutation(child2)
                //evaluate
                child1.fitness = problem.evaluation(child1)
                child2.fitness = problem.evaluation(child2)
                //crossover
                survivor.add(selection(parentSolutions[parent1], child1))
                survivor.add(selection(parentSolutions[parent2], child2))
                count += 2
            }
            val selectedIndex = selectSurviveSolutions((prop.numOfPopulation - numOfCrossover).toInt())

            selectedIndex.forEach {
                survivor.add(parentSolutions[it])
            }
            parentSolutions = survivor.toTypedArray()
        }
    }
}