package it.samuele794.scala.android.ui.onboarding

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import it.samuele794.scala.android.ui.destinations.TrainerLocationSearchPageDestination
import it.samuele794.scala.android.ui.navigation.OnBoardingNavGraph
import it.samuele794.scala.android.ui.theme.ScalaAppTheme


@OptIn(ExperimentalMaterialApi::class)
@OnBoardingNavGraph
@Destination
@Composable
fun TrainerLocationOperationPage(
    navigator: DestinationsNavigator
) {

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = {
            navigator.navigate(TrainerLocationSearchPageDestination)
        }) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "")
        }
    }) {
        //TODO ADD LOCATION
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun TrainerLocationOperationPagePreview() {
    ScalaAppTheme {
//        TrainerLocationOperationPage(
//            navigator = object : DestinationsNavigator {
//                override fun clearBackStack(route: String): Boolean = true
//
//                override fun navigate(
//                    route: String,
//                    onlyIfResumed: Boolean,
//                    builder: NavOptionsBuilder.() -> Unit
//                ) = Unit
//
//                override fun navigateUp(): Boolean = true
//
//                override fun popBackStack(): Boolean = true
//
//                override fun popBackStack(
//                    route: String,
//                    inclusive: Boolean,
//                    saveState: Boolean
//                ): Boolean = true
//            },
//            onBoardingViewModel = object : OnBoardingVMI {
//                override val uiState: StateFlow<OnBoardingViewModel.UserDataUI>
//                    get() = MutableStateFlow(OnBoardingViewModel.UserDataUI())
//
//                override fun updateName(name: String) = Unit
//
//                override fun updateSurname(surname: String) = Unit
//
//                override fun updateHeight(height: String) = Unit
//
//                override fun updateWeight(weight: String) = Unit
//
//                override fun personalDataNextEnabled(): Boolean = true
//
//                override fun getAccountTypes(): Array<AccountType> = AccountType.values()
//                override fun updateAccountType(accountType: AccountType) = Unit
//
//                override fun updateBirthDate(localDate: LocalDate) = Unit
//
//                override fun getLocations(name: String) {
//                    TODO("Not yet implemented")
//                }
//            }
//        )
    }
}