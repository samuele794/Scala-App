package it.samuele794.scala.viewmodel.search

import it.samuele794.scala.model.maps.LatLng
import it.samuele794.scala.repository.PlaceRepository
import it.samuele794.scala.viewmodel.base.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface TrainerSearchVMI {
    val uiState: StateFlow<TrainerSearchViewModel.TrainerSearchUI>

    fun updateCircleRange(range: Float)
    fun searchTrainer(center: LatLng)
}

class TrainerSearchViewModel(private val placeRepository: PlaceRepository) : ViewModel(), TrainerSearchVMI {

    private val mUiState = MutableStateFlow(TrainerSearchUI())
    override val uiState: StateFlow<TrainerSearchUI> =
        mUiState.asStateFlow()

    override fun updateCircleRange(range: Float) {
        viewModelScope.launch {
            mUiState.emit(
                uiState.value.copy(circleRange = range)
            )
        }
    }

    override fun searchTrainer(center: LatLng) {
        viewModelScope.launch(Dispatchers.Default) {
            placeRepository.getPlacesByBounds(center)
        }
    }


    data class TrainerSearchUI(
        val circleRange: Float = 1F,
    )

}