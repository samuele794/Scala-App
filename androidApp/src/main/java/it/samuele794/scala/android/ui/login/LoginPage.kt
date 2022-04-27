package it.samuele794.scala.android.ui.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.common.SignInButton
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import it.samuele794.scala.android.BuildConfig
import it.samuele794.scala.android.R
import it.samuele794.scala.android.ui.destinations.LoginPageDestination
import it.samuele794.scala.android.ui.destinations.PersonalDataPageDestination
import it.samuele794.scala.android.ui.theme.ScalaAppTheme
import it.samuele794.scala.viewmodel.auth.AuthViewModel
import it.samuele794.scala.viewmodel.auth.NativeAuth
import it.samuele794.scala.viewmodel.login.LoginViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import org.koin.androidx.compose.viewModel
import org.koin.core.parameter.parametersOf

@Destination
@Composable
fun LoginPage(
    navigator: DestinationsNavigator
) {
    val coroutineScope = rememberCoroutineScope()


    val authViewModel by viewModel<AuthViewModel>()
    val loginViewModel by viewModel<LoginViewModel> { parametersOf(authViewModel.isLoggedFlow) }
    val nativeAuth = get<NativeAuth>()

    val navDirection by loginViewModel.navDirection.collectAsState()
    when (navDirection) {
        is LoginViewModel.NavDirection.Logged -> {
            //TODO Navigate to Logged
        }

        is LoginViewModel.NavDirection.OnBoarding -> {
            navigator.navigate(PersonalDataPageDestination) {
                popUpTo(LoginPageDestination) { inclusive = true }
            }
        }
        else -> Unit
    }

    val activityResult =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val intentData = it.data
            coroutineScope.launch {
                if (intentData != null) {
                    nativeAuth.onGoogleLoginResult(intentData)
                }
            }

        }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_fitness_center_24),
            contentDescription = "Logo"
        )

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            factory = {
                SignInButton(it).apply {
                    setOnClickListener {
                        loginViewModel.resetNavigation()
                        activityResult.launch(nativeAuth.getGoogleSignIn())
                    }
                }
            }
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {
                    loginViewModel.resetNavigation()
                    // TODO Add Apple Login Function
                },
            elevation = 4.dp
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier.padding(8.dp),
                    painter = painterResource(id = R.drawable.ic_apple_logo_black),
                    contentDescription = "Apple Logo"
                )

                Text(
                    fontWeight = FontWeight.Bold,
                    text = stringResource(id = R.string.apple_login)
                )
            }
        }

        if (BuildConfig.DEBUG) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable {
                        coroutineScope.launch {
                            loginViewModel.resetNavigation()
                            authViewModel.loginByEmailPass(
                                BuildConfig.EMAIL_TEST_AN,
                                BuildConfig.PASSWORD_TEST_AN
                            )
                        }
                    },
                elevation = 4.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        modifier = Modifier.padding(8.dp),
                        imageVector = Icons.Filled.Email,
                        contentDescription = "Apple Logo"
                    )

                    Text(
                        fontWeight = FontWeight.Bold,
                        text = "Anonimo"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginPagePreview() {
    ScalaAppTheme {
//        LoginPage()
    }
}