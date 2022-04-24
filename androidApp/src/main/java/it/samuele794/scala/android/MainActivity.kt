package it.samuele794.scala.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.utils.contains
import it.samuele794.scala.android.ui.NavGraphs
import it.samuele794.scala.android.ui.theme.ScalaAppTheme
import it.samuele794.scala.viewmodel.onboarding.OnBoardingVMI
import it.samuele794.scala.viewmodel.onboarding.OnBoardingViewModel
import org.koin.androidx.compose.getViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScalaAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        dependenciesContainerBuilder = {
                            if (NavGraphs.onBoarding.contains(destination)) {
                                val parentEntry = remember {
                                    navController.getBackStackEntry(NavGraphs.onBoarding.route)
                                }
                                dependency(getViewModel<OnBoardingViewModel>(owner = parentEntry) as OnBoardingVMI)
                            }
                        }
                    )
                }
            }
        }
    }
}

