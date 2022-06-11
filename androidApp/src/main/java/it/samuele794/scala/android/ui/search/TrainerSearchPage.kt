package it.samuele794.scala.android.ui.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import dev.icerock.moko.resources.compose.stringResource
import it.samuele794.scala.model.maps.LatLng
import it.samuele794.scala.resources.SharedRes
import it.samuele794.scala.viewmodel.search.TrainerSearchVMI


@Composable
fun TrainerSearchPage(
    trainerSearchVMI: TrainerSearchVMI
) {
    val uiState by trainerSearchVMI.uiState.collectAsState()
    val cameraPositionState = rememberCameraPositionState()

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {

        val (mapRef, rangeRef, nextRef) = createRefs()

        GoogleMap(
            modifier = Modifier.constrainAs(mapRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(rangeRef.top)
            },
        ) {
            Circle(
                center = cameraPositionState.position.target,
                radius = uiState.circleRange.toDouble(),
                fillColor = MaterialTheme.colors.secondary.copy(alpha = 0.6F)
            )
        }


        Slider(
            modifier = Modifier.constrainAs(rangeRef) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            value = uiState.circleRange,
            onValueChange = {
                trainerSearchVMI.updateCircleRange(it)
            }
        )

        Button(modifier = Modifier.constrainAs(nextRef) {
            top.linkTo(mapRef.top)
            bottom.linkTo(mapRef.bottom, 16.dp)
            start.linkTo(mapRef.start)
            end.linkTo(mapRef.end)
        }, onClick = {
            trainerSearchVMI.searchTrainer(cameraPositionState.position.target.run {
                LatLng(lat = latitude, lng = longitude)
            })
        }) {
            Text(stringResource(SharedRes.strings.next))
        }
    }


}