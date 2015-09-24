package com.ea.algor.ga

/**
 * Created by taker on 2015/09/24.
 */
data class PropertyGA(
        val functionevaluations: Int,
        val numOfPopulation: Int,
        val numOfDimension: Int,
        val nuOfElite: Int,
        val CR: Double,
        val mutationRate: Double
)