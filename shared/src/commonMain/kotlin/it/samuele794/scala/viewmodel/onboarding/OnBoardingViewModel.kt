package it.samuele794.scala.viewmodel.onboarding

import co.touchlab.kermit.Logger
import it.samuele794.scala.model.AccountType
import it.samuele794.scala.viewmodel.base.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

interface OnBoardingVMI {
    val uiState: StateFlow<OnBoardingViewModel.UserDataUI>

    fun updateName(name: String)
    fun updateSurname(surname: String)
    fun updateHeight(height: String)
    fun updateWeight(weight: String)

    fun personalDataNextEnabled(): Boolean

    fun getAccountTypes(): Array<AccountType>
    fun updateAccountType(accountType: AccountType)
    fun updateBirthDate(localDate: LocalDate)
}

class OnBoardingViewModel(
    private val logger: Logger
) : ViewModel(), OnBoardingVMI {

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
            viewModelScope.launch {
                mUiState.emit(uiState.value.copy(height = height.toInt()))
            }
        }
    }

    override fun updateWeight(weight: String) {
        runCatching {
            var mWeight = weight

            if (weight.contains(","))
                mWeight = weight.replace(",", ".")

            if (mWeight.count { it == '.' } > 1)
                mWeight = mWeight.reversed().replaceFirst(".", "").reversed()

            mWeight.split(".").apply {
                if (size > 1) {
                    val digitPart = last()
                    if (digitPart.count() > 1) {
                        mWeight = first() + "." + digitPart.substring(0..1)
                    }
                }
            }

            viewModelScope.launch {
                mUiState.emit(uiState.value.copy(weight = mWeight))
            }
        }
    }

    override fun updateAccountType(accountType: AccountType) {
        viewModelScope.launch {
            mUiState.emit(uiState.value.copy(accountType = accountType))
        }
    }

    override fun getAccountTypes(): Array<AccountType> = AccountType.values()

    override fun personalDataNextEnabled(): Boolean {
        return uiState.value.run {
            name.isNotBlank() && surname.isNotBlank() &&
                    accountType != AccountType.NONE
        }
    }

    override fun updateBirthDate(localDate: LocalDate) {
        viewModelScope.launch {
            mUiState.emit(uiState.value.copy(birthDate = localDate))
        }
    }

    data class UserDataUI(
        val name: String = "",
        val surname: String = "",
        val height: Int? = null,
        val weight: String? = null,
        val accountType: AccountType = AccountType.NONE,
        val birthDate: LocalDate? = null
    ) {
        fun getFormattedBirthDate(): String {
            return birthDate?.toString() ?: ""
        }
    }
}