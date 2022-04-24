package it.samuele794.scala.viewmodel.onboarding

import it.samuele794.scala.viewmodel.base.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface OnBoardingVMI {
    val uiState: StateFlow<OnBoardingViewModel.UserDataUI>

    fun updateName(name: String)
    fun updateSurname(surname: String)
    fun updateHeight(height: String)
    fun updateWeight(weight: String)
}

class OnBoardingViewModel : ViewModel(), OnBoardingVMI {

    private val mUiState = MutableStateFlow(UserDataUI())
    override val uiState = mUiState.asStateFlow()

    override fun updateName(name: String) {
        viewModelScope.launch {
            mUiState.emit(uiState.value.copy(name = name))
        }
    }

    override fun updateSurname(surname: String) {
        viewModelScope.launch {
            mUiState.emit(uiState.value.copy(surname = surname))
        }
    }

    override fun updateHeight(height: String) {
        runCatching {
            //TODO Change this check logic for better ui usability

            var mHeight = height
            if (height.contains(","))
                mHeight = height.replace(",", ".")

            val isFormatted = UserDataUI.DIGIT_TRUNCATE.matchEntire(mHeight)
                ?.groupValues?.last()

            if (isFormatted != null) {
                viewModelScope.launch {
                    mUiState.emit(uiState.value.copy(height = isFormatted.toDouble()))
                }
            }
        }
    }

    override fun updateWeight(weight: String) {
        runCatching {
            //TODO Change this check logic for better ui usability

            var mWeight = weight
            if (weight.contains(","))
                mWeight = weight.replace(",", ".")

            val isFormatted = UserDataUI.DIGIT_TRUNCATE.matchEntire(mWeight)
                ?.groupValues?.last()

            if (isFormatted != null) {
                viewModelScope.launch {
                    mUiState.emit(uiState.value.copy(weight = isFormatted.toDouble()))
                }
            }
        }
    }

    data class UserDataUI(
        val name: String = "",
        val surname: String = "",
        val height: Double? = null,
        val weight: Double? = null
    ) {

        companion object {
            private const val NUMBER_2_DIGIT_TRUNCATE = "^(\\d+(?:.\\d{1,2})?).*"

            internal val DIGIT_TRUNCATE = Regex(NUMBER_2_DIGIT_TRUNCATE)
        }
    }
}