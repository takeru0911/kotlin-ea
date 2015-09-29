package com.ea.algor.ga

import com.ea.algor.Algorithm
import com.ea.algor.Solution
import com.ea.createBinarySolutionByRandom
import com.ea.prob.Problem
import com.ea.prob.bin.KP
import com.ea.prob.real.Sphere
import com.ea.randomSelector
import com.ea.toStr
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
 * fitness Ç™0ÇÃèÍçáê‚ëŒëIëÇ≥ÇÍÇ»Ç¢ÇÃÇ≈Ç∑Ç◊ÇƒÇÃå¬ëÃÇÃfitnessÇ…+0.1Ç∑ÇÈÇ±Ç∆Ç≈âÒîÇ∑ÇÈ
 */
fun makeRouletteByFitness(parents: Array<Solution>): Array<Double>{
    val sum = parents.map {
        solution -> solution.fitness + 0.1
    }.sum()
    val rouletteTable = Array(parents.size()){
        (parents[it].fitness + 0.1) / sum
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
                if(v == 0.0) 1.0 else 0.0
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

        var count = 0
        check((prop.numOfPopulation * prop.CR) % 2.0 == 0.0)
        var best = Double.MIN_VALUE
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
                crossoverMask = makeCrossoverMask(prop.numOfDimension)
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
                best = when{
                    child1.fitness > best -> child1.fitness
                    child2.fitness > best -> child2.fitness
                    else -> best
                }


            }
            val selectedIndex = selectSurviveSolutions((prop.numOfPopulation - numOfCrossover).toInt())
            selectedIndex.forEach {
                survivor.add(parentSolutions[it])
            }
            parentSolutions = survivor.toTypedArray()

            println(best)
        }
    }
}
fun main(args: Array<String>){

    val property = PropertyGA(
            functionevaluations = 30000,
            numOfPopulation = 30,
            numOfDimension = 250,
            CR = 0.6,
            mutationRate = 0.05
    )
    val problem = KP(property.numOfDimension)
    val solver = GA(problem, property)
    solver.run()
}