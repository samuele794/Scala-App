package it.samuele794.scala.android.ui.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import it.samuele794.scala.android.ui.navigation.OnBoardingNavGraph
import it.samuele794.scala.android.ui.theme.ScalaAppTheme
import it.samuele794.scala.model.maps.LatLng
import it.samuele794.scala.model.maps.Place
import it.samuele794.scala.resources.SharedRes
import it.samuele794.scala.utils.toLatLng
import it.samuele794.scala.viewmodel.onboarding.TrainerLocationSearchViewModel
import it.samuele794.scala.viewmodel.onboarding.TrainerLocationVMI
import org.koin.androidx.compose.viewModel

@OnBoardingNavGraph
@Destination
@Composable
fun TrainerLocationSearchPage(
    resultNavigator: ResultBackNavigator<Place>
) {
    val trainerLocationViewModel: TrainerLocationVMI by viewModel<TrainerLocationSearchViewModel>()
    val cameraPositionState = rememberCameraPositionState()

    val searchUI by trainerLocationViewModel.uiState.collectAsState()
    val placeSelected = searchUI.placeSelected

    LaunchedEffect(searchUI.placeSelected) {
        val place = searchUI.placeSelected
        if (place != null) {
            cameraPositionState.move(
                CameraUpdateFactory.newLatLngZoom(
                    place.latLng.toLatLng(),
                    12F
                )
            )
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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

            GoogleMap(
                modifier = Modifier.weight(1f),
                cameraPositionState = cameraPositionState
            ) {
                if (placeSelected != null) {
                    Marker(position = placeSelected.latLng.toLatLng())
                }
            }
        }


        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            visible = placeSelected != null,
            enter = slideInVertically(), exit = slideOutVertically()
        ) {
            Button(
                modifier = Modifier
                    .padding(bottom = 16.dp),
                onClick = {
                    if (placeSelected != null) {
                        resultNavigator.navigateBack(placeSelected)
                    } else {
                        resultNavigator.navigateBack()
                    }
                }) {
                Text(text = stringResource(id = SharedRes.strings.next.resourceId))
            }
        }
    }
}

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
            singleLine = true,
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