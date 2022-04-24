package it.samuele794.scala.android.ui.navigation

import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph
@NavGraph
annotation class OnBoardingNavGraph(
    val start: Boolean = false
)