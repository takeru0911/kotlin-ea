package com.ea


import java.util
import java.util.*

/**
 * Created by taker on 2015/09/22.
 */
fun randomSelector(numOfrand: Int, start: Int, end: Int): Array<Int>{
    val selectedNumber = ArrayList<Int>()

    check(numOfrand < (end - start))
    while(selectedNumber.size() != numOfrand) {
        val rand = (Math.random() * (end - start)).toInt() + start
        if(!selectedNumber.contains(rand)){
            selectedNumber.add(rand)
        }
    }
    return selectedNumber.toTypedArray()
}
fun Array<Int>.with2Step(func: (n1: Int, n2: Int) -> Double): List<Double>{
    val list = ArrayList<Double>()

    check(this.size() % 2 == 0)
    for(i in 0 .. this.size() - 1){
        val index1 = i * 2
        val index2 = i * 2 + 1
        list.add(func(index1, index2))
    }

    return list
}
