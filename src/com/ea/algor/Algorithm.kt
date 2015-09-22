package com.ea.algor


/**
 * Created by taker on 2015/09/22.
 */
interface Algorithm {

    fun crossover(
            trial: Solution, mutant: Solution): Solution
    fun mutation(
            trial: Solution): Solution
    fun selection(
            base: Solution, trial: Solution): Solution
}