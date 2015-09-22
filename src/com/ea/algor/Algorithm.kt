package com.ea.algor


/**
 * Created by taker on 2015/09/22.
 */
interface Algorithm {

    abstract protected fun crossover(
            sol1: Solution, sol2: Solution): Solution
    abstract protected fun mutation(
            sol1: Solution, sol2: Solution): Solution
    abstract protected fun selection(
            sol1: Solution, sol2: Solution): Solution
}