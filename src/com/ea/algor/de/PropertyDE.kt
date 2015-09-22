package com.ea.algor.de

/**
 * Created by taker on 2015/09/22.
 */
data class PropertyDE(
        val CR: Double = 1.0,
        val F: Double = 0.0,
        val numOfDiffSol: Int = 1,
        val functionEvaluations: Int,
        val numOfDimension: Int,
        val numOfPopulations: Int,
        val varRangeMax: Array<Double>,
        val varRangeMin: Array<Double>
)