package com.ea


import com.ea.algor.Solution
import java.util
import java.util.*

/**
 * Created by taker on 2015/09/22.
 */

/**
 * d•¡–³‚µ‚Åƒ‰ƒ“ƒ_ƒ€‚É”CˆÓ‚Ì”‚ÌInt‚Ì”z—ñ‚ğ•Ô‚·
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
fun <T> Array<T>.with2Step(func: (n1: T, n2: T) -> Double): List<Double>{
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

fun getWhichValueByRand(v1: Double, v2: Double, compareToRand: Double): Double{
    return if(compareToRand > Math.random()){
        v1
    }else{
        v2
    }
}

fun createSolutionByRandom(boundsOfMax: Array<Double>, boudsOfMin: Array<Double>): Solution{
    return Solution(Array(boundsOfMax.size()) {
        randomToDouble(boudsOfMin[it], boundsOfMax[it])
    })
}

fun createBinarySolutionByRandom(dimension: Int): Solution{
    return Solution(Array(dimension) {
        if(Math.random() > 0.5){
            0.0
        }else{
            1.0
        }
    })
}

fun <T> Array<T>.filterwithIndexed(func: (index: Int, value: T) -> Boolean): List<T>{
    val filteredList = ArrayList<T>()
    for(i in 0 .. this.size() - 1){
        if(func(i, this[i])){
            filteredList.add(this[i])
        }
    }

    return filteredList
}

fun <T> Array<T>.toStr(): String{
    return this.joinToString(",", "[", "]")
}