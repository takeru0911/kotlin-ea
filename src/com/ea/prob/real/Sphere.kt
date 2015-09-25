package com.ea.prob.real

import com.ea.algor.Solution
import com.ea.prob.Problem

/**
 * Created by taker on 2015/09/22.
 */
class Sphere(): Problem(){
    override fun evaluation(solution: Solution): Double{
        val vars = solution.vars;
        return vars.map { x -> x * x }
        .sum();
    }
}