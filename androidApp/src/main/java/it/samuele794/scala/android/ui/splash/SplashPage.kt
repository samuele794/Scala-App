package it.samuele794.scala.android.ui.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import it.samuele794.scala.android.ui.destinations.LoginPageDestination
import it.samuele794.scala.android.ui.destinations.PersonalDataPageDestination
import it.samuele794.scala.android.ui.destinations.SplashPageDestination
import it.samuele794.scala.viewmodel.auth.AuthViewModel
import it.samuele794.scala.viewmodel.login.LoginViewModel
import org.koin.androidx.compose.viewModel
import org.koin.core.parameter.parametersOf

@RootNavGraph(start = true)
@Destination
@Composable
fun SplashPage(
    navigator: DestinationsNavigator
) {
    val authViewModel by viewModel<AuthViewModel>()
    val loginViewModel by viewModel<LoginViewModel> { parametersOf(authViewModel.isLoggedFlow) }

    val navDirection by loginViewModel.navDirection.collectAsState()
    when (navDirection) {
        is LoginViewModel.NavDirection.Logged -> {
            //TODO Navigate to Logged
        }

        is LoginViewModel.NavDirection.OnBoarding -> {
            navigator.navigate(PersonalDataPageDestination) {
                popUpTo(SplashPageDestination) { inclusive = true }
            }
        }

        is LoginViewModel.NavDirection.Login -> {
            navigator.navigate(LoginPageDestination) {
                popUpTo(SplashPageDestination) { inclusive = true }
            }
        }
        else -> Unit
    }
}