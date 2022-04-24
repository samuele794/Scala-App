package it.samuele794.scala.android.ui.splash

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import it.samuele794.scala.android.ui.destinations.LoginPageDestination

@RootNavGraph(start = true)
@Destination
@Composable
fun SplashPage(
    navigator: DestinationsNavigator
) {
    navigator.navigate(LoginPageDestination)
}