package it.samuele794.scala.android.ui.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.ramcosta.composedestinations.annotation.Destination
import it.samuele794.scala.android.ui.navigation.OnBoardingNavGraph
import it.samuele794.scala.android.ui.theme.ScalaAppTheme
import it.samuele794.scala.model.maps.LatLng
import it.samuele794.scala.model.maps.Place
import it.samuele794.scala.resources.SharedRes
import it.samuele794.scala.utils.toLatLng
import it.samuele794.scala.viewmodel.onboarding.OnBoardingVMI
import it.samuele794.scala.viewmodel.onboarding.TrainerLocationSearchViewModel
import it.samuele794.scala.viewmodel.onboarding.TrainerLocationVMI
import org.koin.androidx.compose.viewModel

@OnBoardingNavGraph
@Destination
@Composable
fun TrainerLocationSearchPage(
    onBoardingViewModel: OnBoardingVMI
) {
    val trainerLocationViewModel: TrainerLocationVMI by viewModel<TrainerLocationSearchViewModel>()
    //TODO SAVE ZOOM STATE
    val zoomSelected by rememberSaveable {
        mutableStateOf(13f)
    }

//    val uiState by onBoardingViewModel.uiState.collectAsState()
    val searchUI by trainerLocationViewModel.uiState.collectAsState()
    val placeSelected = searchUI.placeSelected

    Column(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(rememberScrollState(), orientation = Orientation.Vertical)
    ) {
        SearchField(
            modifier = Modifier.weight(1f),
            searchAddressResult = searchUI.trainerLocationResult,
            searchTerm = searchUI.searchedTerm,
            placeSelected = placeSelected,
            onPlaceSelected = {
                trainerLocationViewModel.setPlaceSelected(it)
            },
            onTextUpdated = {
                trainerLocationViewModel.getLocations(it)
            }
        )

        if (searchUI.placeSelected != null) {
            Row(
                modifier = Modifier
                    .weight(0.1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(0.2f),
                    text = "${searchUI.searchRange} " + stringResource(id = SharedRes.strings.label_km.resourceId)
                )
                Slider(
                    modifier = Modifier.weight(0.8f),
                    value = searchUI.searchRange.toFloat(),
                    onValueChange = {
                        trainerLocationViewModel.updateRange(it.toInt())
                    },
                    valueRange = 1f..30f
                )
            }
        }


        GoogleMap(
            modifier = Modifier.weight(1f),
            cameraPositionState = if (placeSelected != null) {
                CameraPositionState(
                    position = CameraPosition.fromLatLngZoom(
                        placeSelected.latLng.toLatLng(),
                        zoomSelected
                    )
                )
            } else {
                rememberCameraPositionState()
            }
        ) {
            if (placeSelected != null) {
                Circle(
                    center = placeSelected.latLng.toLatLng(),
                    fillColor = MaterialTheme.colors.primary.copy(alpha = 0.5f),
                    strokeColor = MaterialTheme.colors.secondary,
                    radius = searchUI.searchRange.times(1000)
                        .toDouble()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SearchField(
    modifier: Modifier = Modifier,
    searchAddressResult: List<Place>,
    searchTerm: String,
    placeSelected: Place? = null,
    onPlaceSelected: (Place) -> Unit,
    onTextUpdated: (String) -> Unit
) {

    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = searchTerm,
            onValueChange = {
                onTextUpdated(it)
            },
            label = { Text(stringResource(id = SharedRes.strings.search_place.resourceId)) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(searchAddressResult) {
                Row(
                    modifier = Modifier
                        .clickable {
                            onPlaceSelected(it)
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
                    if (it.placeId == placeSelected?.placeId) {
                        Icon(
                            modifier = Modifier
                                .weight(0.1F)
                                .padding(horizontal = 8.dp),
                            imageVector = Icons.Filled.Check,
                            contentDescription = ""
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun SearchFieldPreview() {
    ScalaAppTheme {
        var text by remember {
            mutableStateOf("")
        }

        var placeSelected by remember {
            mutableStateOf<Place?>(null)
        }

        SearchField(
            modifier = Modifier,
            searchAddressResult = buildList {
                repeat(20) {
                    add(
                        Place(
                            "abc",
                            "Cia Come Sta",
                            "Via Di Quack",
                            LatLng(20.0, 9.0)
                        )
                    )
                }
            },
            searchTerm = text,
            onTextUpdated = {
                text = it
            },
            onPlaceSelected = {
                placeSelected = it
            },
            placeSelected = placeSelected
        )
    }
}