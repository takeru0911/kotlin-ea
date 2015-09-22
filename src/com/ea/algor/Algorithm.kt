package com.ea.algor


/**
 * Created by taker on 2015/09/22.
 */
interface Algorithm {

    fun run(){
        mutation()
        crossover()
        selection()
    }
    abstract protected fun crossover()
    abstract protected fun mutation()
    abstract protected fun selection()
}