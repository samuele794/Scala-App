package it.samuele794.scala.android.ui.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import it.samuele794.scala.android.R
import it.samuele794.scala.android.ui.navigation.OnBoardingNavGraph
import it.samuele794.scala.android.ui.theme.ScalaAppTheme
import it.samuele794.scala.viewmodel.onboarding.OnBoardingVMI
import it.samuele794.scala.viewmodel.onboarding.OnBoardingViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OnBoardingNavGraph
@Destination
@Composable
fun BodyDataPage(
    onBoardingViewModel: OnBoardingVMI
) {

    val uiState by onBoardingViewModel.uiState.collectAsState()
    val nextEnabled = uiState.height != null && uiState.weight != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.height?.toString() ?: "",
            label = {
                Text(text = stringResource(id = R.string.height))
            },
            onValueChange = {
                onBoardingViewModel.updateHeight(it)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            trailingIcon = {
                Text(text = stringResource(id = R.string.cm))
            },
            singleLine = true
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.weight?.toString() ?: "",
            label = {
                Text(text = stringResource(id = R.string.weight))
            },
            onValueChange = {
                onBoardingViewModel.updateWeight(it)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            trailingIcon = {
                Text(text = stringResource(id = R.string.kg))
            }
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
//                    navigator.navigate(BodyDataPageDestination)
                }
            ) { Text(text = stringResource(id = R.string.next)) }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BodyDataPagePreview() {
    ScalaAppTheme {
        BodyDataPage(object : OnBoardingVMI {
            override val uiState: StateFlow<OnBoardingViewModel.UserDataUI>
                get() = MutableStateFlow(OnBoardingViewModel.UserDataUI())

            override fun updateName(name: String) = Unit

            override fun updateSurname(surname: String) = Unit

            override fun updateHeight(height: String) = Unit

            override fun updateWeight(weight: String) = Unit

        })
    }
}