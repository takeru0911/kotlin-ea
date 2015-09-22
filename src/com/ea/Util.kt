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
    for(i in 0 .. (this.size() / 2) - 1){
        val index1 = i * 2
        val index2 = i * 2 + 1
        list.add(func(this[index1], this[index2]))
    }

    return list
}
fun randomToInt(min: Int, max: Int): Int{
    return (Math.random() * (max - min)).toInt() + min
}
fun randomToDouble(min: Double, max: Double): Double{
    return (Math.random() * (max - min)) + min
}
inline val revVarBoundary: (v: Double, max: Double, min: Double) -> Double = {
    v, max, min ->
    when {
        v > max -> max
        v < min -> min
        else -> v
    }
}


