package it.samuele794.scala.android.ui.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavOptionsBuilder
import com.airbnb.lottie.compose.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import it.samuele794.scala.android.ui.destinations.HomePageDestination
import it.samuele794.scala.android.ui.destinations.OnBoardSavePageDestination
import it.samuele794.scala.android.ui.navigation.OnBoardingNavGraph
import it.samuele794.scala.android.ui.theme.ScalaAppTheme
import it.samuele794.scala.model.AccountType
import it.samuele794.scala.model.maps.Place
import it.samuele794.scala.resources.SharedRes
import it.samuele794.scala.viewmodel.onboarding.OnBoardingVMI
import it.samuele794.scala.viewmodel.onboarding.OnBoardingViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDate

@OnBoardingNavGraph
@Destination
@Composable
fun OnBoardSavePage(
    navigator: DestinationsNavigator,
    onBoardingViewModel: OnBoardingVMI
) {

    val saveComplete by onBoardingViewModel.accountCreateState.collectAsState(initial = false)

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(SharedRes.files.onboarding_loading.rawResId))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        speed = 0.5F
    )

    LaunchedEffect(Unit) {
        onBoardingViewModel.saveAccount()
    }

    LaunchedEffect(saveComplete) {
        if (saveComplete) {
            navigator.navigate(HomePageDestination) {
                popUpTo(OnBoardSavePageDestination) {
                    inclusive = true
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LottieAnimation(
            modifier = Modifier.align(Alignment.Center),
            composition = composition,
            progress = progress

        )
    }
}

@Preview
@Composable
fun OnBoardSavePagePreview() {
    ScalaAppTheme {
        OnBoardSavePage(
            navigator = object : DestinationsNavigator {
                override fun clearBackStack(route: String): Boolean = true

                override fun navigate(
                    route: String,
                    onlyIfResumed: Boolean,
                    builder: NavOptionsBuilder.() -> Unit
                ) = Unit

                override fun navigateUp(): Boolean = true

                override fun popBackStack(): Boolean = true

                override fun popBackStack(
                    route: String,
                    inclusive: Boolean,
                    saveState: Boolean
                ): Boolean = true
            },
            object : OnBoardingVMI {
                override val uiState: StateFlow<OnBoardingViewModel.UserDataUI>
                    get() = MutableStateFlow(OnBoardingViewModel.UserDataUI())

                override val accountCreateState: Flow<Boolean>
                    get() = MutableStateFlow(false)

                override fun updateName(name: String) = Unit

                override fun updateSurname(surname: String) = Unit

                override fun updateHeight(height: String) = Unit

                override fun updateWeight(weight: String) = Unit

                override fun personalDataNextEnabled(): Boolean = true

                override fun getAccountTypes(): Array<AccountType> = AccountType.values()
                override fun updateAccountType(accountType: AccountType) = Unit

                override fun updateBirthDate(localDate: LocalDate) = Unit
                override fun addTrainerPlace(place: Place) = Unit

                override fun removeTrainerPlace(place: Place) = Unit

                override fun saveAccount() = Unit
            })
    }
}