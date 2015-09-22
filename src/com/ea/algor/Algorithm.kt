package com.ea.algor


/**
 * Created by taker on 2015/09/22.
 */
interface Algorithm {

    abstract fun crossover(
            sol1: Solution, sol2: Solution): Solution
    abstract fun mutation(
            target: Solution): Solution
    abstract fun selection(
            target: Solution, trial: Solution): Solution
}