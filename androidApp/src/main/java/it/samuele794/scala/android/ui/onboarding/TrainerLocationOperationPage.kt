package it.samuele794.scala.android.ui.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import it.samuele794.scala.android.ui.destinations.TrainerLocationSearchPageDestination
import it.samuele794.scala.android.ui.navigation.OnBoardingNavGraph
import it.samuele794.scala.android.ui.theme.ScalaAppTheme
import it.samuele794.scala.model.maps.Place
import it.samuele794.scala.utils.toLatLng
import it.samuele794.scala.viewmodel.onboarding.OnBoardingVMI
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@OnBoardingNavGraph
@Destination
@Composable
fun TrainerLocationOperationPage(
    navigator: DestinationsNavigator,
    resultRecipient: ResultRecipient<TrainerLocationSearchPageDestination, Place>,
    onBoardingVMI: OnBoardingVMI
) {
    val uiState by onBoardingVMI.uiState.collectAsState()
    var showCasePlace by remember {
        mutableStateOf<Place?>(null)
    }

    val state = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    ) {
        if (it == ModalBottomSheetValue.Hidden) {
            showCasePlace = null
        }
        true
    }

    val coroutineScope = rememberCoroutineScope()

    resultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Value -> {
                onBoardingVMI.addTrainerPlace(result.value)
            }
            else -> Unit
        }
    }

    ModalBottomSheetLayout(
        sheetState = state,
        sheetContent = {
            showCasePlace?.let { PlaceBottomSheet(it) }
        }
    ) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    navigator.navigate(TrainerLocationSearchPageDestination())
                }) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "")
                }
            }
        ) {
            PlaceList(
                trainerPlaces = uiState.trainerPlaces,
                itemClicked = {
                    coroutineScope.launch {
                        showCasePlace = it
                        state.show()
                    }
                },
                onRemoveClicked = {
                    onBoardingVMI.removeTrainerPlace(it)
                }
            )
        }
    }


}

@Composable
fun PlaceBottomSheet(showCasePlace: Place) {
    Box(modifier = Modifier.fillMaxHeight(0.2f)) {
        GoogleMap(
            cameraPositionState = CameraPositionState(
                CameraPosition.fromLatLngZoom(
                    showCasePlace.latLng.toLatLng(),
                    12f
                )
            ),
            uiSettings = MapUiSettings(
                compassEnabled = false,
                indoorLevelPickerEnabled = false,
                mapToolbarEnabled = false,
                myLocationButtonEnabled = false,
                rotationGesturesEnabled = false,
                scrollGesturesEnabled = false,
                scrollGesturesEnabledDuringRotateOrZoom = false,
                tiltGesturesEnabled = false,
                zoomControlsEnabled = false,
                zoomGesturesEnabled = false,
            )
        ) {
            Marker(
                position = showCasePlace.latLng.toLatLng(),
                title = showCasePlace.placeName
            )
        }
    }
}

@Composable
fun PlaceList(
    trainerPlaces: List<Place>,
    itemClicked: (Place) -> Unit,
    onRemoveClicked: (Place) -> Unit
) {
    LazyColumn {
        items(trainerPlaces) {
            Row(
                modifier = Modifier
                    .clickable {
                        itemClicked(it)
                    }
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    modifier = Modifier.weight(0.9f),
                    text = it.placeName
                )
                Icon(
                    modifier = Modifier
                        .weight(0.1F)
                        .clickable {
                            onRemoveClicked(it)
                        },
                    imageVector = Icons.Filled.Clear,
                    contentDescription = ""
                )
            }
        }
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