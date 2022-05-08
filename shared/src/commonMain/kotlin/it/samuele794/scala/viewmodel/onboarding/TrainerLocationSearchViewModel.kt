package it.samuele794.scala.viewmodel.onboarding

import co.touchlab.kermit.Logger
import it.samuele794.scala.model.maps.Place
import it.samuele794.scala.repository.GoogleMapsRepository
import it.samuele794.scala.viewmodel.base.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface TrainerLocationVMI {
    val uiState: StateFlow<TrainerLocationSearchViewModel.TrainerLocationUi>

    fun getLocations(name: String)
    fun setPlaceSelected(place: Place?)
}

class TrainerLocationSearchViewModel(
    private val logger: Logger,
    private val googleMapsRepository: GoogleMapsRepository
) : ViewModel(), TrainerLocationVMI {

    private var locationSearchJob: Job? = null

    private val mUiState = MutableStateFlow(TrainerLocationUi())
    override val uiState = mUiState.asStateFlow()


    override fun getLocations(name: String) {
        locationSearchJob?.cancel()
        viewModelScope.launch {
            mUiState.emit(uiState.value.copy(searchedTerm = name))

            if (name.isBlank()) {
                setPlaceSelected(null)
                mUiState.emit(uiState.value.copy(trainerLocationResult = emptyList()))
                return@launch
            }
        }

        locationSearchJob = viewModelScope.launch(Dispatchers.Default) {
            delay(500)

            logger.i("Location Search active $isActive")

            if (isActive) {
                logger.i("Location Search : $name")
                runCatching {
                    val locationList = googleMapsRepository.getPlaces(name)

                    logger.i("Location Founded : ${locationList.size}")

                    mUiState.emit(uiState.value.copy(trainerLocationResult = locationList))
                }.onFailure {
                    logger.e("Error Fetch Places", it)
                }
            }


        }
    }

    override fun setPlaceSelected(place: Place?) {
        viewModelScope.launch {
            mUiState.emit(uiState.value.copy(placeSelected = place))
        }
    }

    data class TrainerLocationUi(
        val searchedTerm: String = "",
        val trainerLocationResult: List<Place> = emptyList(),
        val placeSelected: Place? = null
    )
}