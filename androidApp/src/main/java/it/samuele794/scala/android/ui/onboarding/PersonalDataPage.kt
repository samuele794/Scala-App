package it.samuele794.scala.android.ui.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptionsBuilder
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import it.samuele794.scala.android.R
import it.samuele794.scala.android.ui.destinations.BodyDataPageDestination
import it.samuele794.scala.android.ui.navigation.OnBoardingNavGraph
import it.samuele794.scala.android.ui.theme.ScalaAppTheme
import it.samuele794.scala.model.AccountType
import it.samuele794.scala.resources.SharedRes
import it.samuele794.scala.viewmodel.onboarding.OnBoardingVMI
import it.samuele794.scala.viewmodel.onboarding.OnBoardingViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterialApi::class)
@OnBoardingNavGraph(start = true)
@Destination
@Composable
fun PersonalDataPage(
    navigator: DestinationsNavigator,
    onBoardingViewModel: OnBoardingVMI
) {

    val uiState by onBoardingViewModel.uiState.collectAsState()

    val nextEnabled = onBoardingViewModel.personalDataNextEnabled()

    var dropExpanded by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        ExposedDropdownMenuBox(
            modifier = Modifier.fillMaxWidth(),
            expanded = dropExpanded,
            onExpandedChange = { dropExpanded = !dropExpanded }) {

            //TODO ADD STYLE FOR FAKE DISABLED TEXTFIELD
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = false,
                value = stringResource(id = uiState.accountType.accountName.resourceId),
                onValueChange = { },
                label = { Text(stringResource(id = SharedRes.strings.account_type.resourceId)) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropExpanded)
                }
            )

            ExposedDropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                expanded = dropExpanded,
                onDismissRequest = { dropExpanded = false }) {
                onBoardingViewModel.getAccountTypes().forEach { accountType ->
                    val name = stringResource(id = accountType.accountName.resourceId)

                    DropdownMenuItem(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onBoardingViewModel.updateAccountType(accountType)
                            dropExpanded = false
                        }
                    ) {
                        Text(text = name)
                    }
                }
            }
        }


        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.name,
            onValueChange = {
                onBoardingViewModel.updateName(it)
            },
            label = { Text(text = stringResource(id = R.string.user_name)) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.surname,
            onValueChange = {
                onBoardingViewModel.updateSurname(it)
            },
            label = { Text(text = stringResource(id = R.string.user_surname)) }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                enabled = nextEnabled,
                onClick = {
                    navigator.navigate(BodyDataPageDestination)
                }
            ) { Text(text = stringResource(id = R.string.next)) }
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PersonalDataPagePreview() {
    ScalaAppTheme {
        PersonalDataPage(
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
            onBoardingViewModel = object : OnBoardingVMI {
                override val uiState: StateFlow<OnBoardingViewModel.UserDataUI>
                    get() = MutableStateFlow(OnBoardingViewModel.UserDataUI())

                override fun updateName(name: String) = Unit

                override fun updateSurname(surname: String) = Unit

                override fun updateHeight(height: String) = Unit

                override fun updateWeight(weight: String) = Unit

                override fun personalDataNextEnabled(): Boolean = true

                override fun getAccountTypes(): Array<AccountType> = AccountType.values()
                override fun updateAccountType(accountType: AccountType) = Unit
            }
        )
    }
}