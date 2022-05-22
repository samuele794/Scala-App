package it.samuele794.scala.viewmodel.onboarding

import co.touchlab.kermit.Logger
import dev.gitlive.firebase.auth.FirebaseUser
import it.samuele794.scala.model.AccountType
import it.samuele794.scala.model.maps.Place
import it.samuele794.scala.repository.UserRepository
import it.samuele794.scala.viewmodel.base.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.*

interface OnBoardingVMI {
    val uiState: StateFlow<OnBoardingViewModel.UserDataUI>
    val accountCreateState: Flow<Boolean>

    val birthdayMinDate
        get() = Clock.System.now()
            .minus(DateTimePeriod(years = 18), TimeZone.currentSystemDefault())

    fun updateName(name: String)
    fun updateSurname(surname: String)
    fun updateHeight(height: String)
    fun updateWeight(weight: String)

    fun personalDataNextEnabled(): Boolean

    fun getAccountTypes(): Array<AccountType>
    fun updateAccountType(accountType: AccountType)
    fun updateBirthDate(localDate: LocalDate)
    fun addTrainerPlace(place: Place)
    fun removeTrainerPlace(place: Place)

    fun saveAccount()
}

class OnBoardingViewModel(
    private val logger: Logger,
    private val loggedFlow: Flow<FirebaseUser?>,
    private val userRepository: UserRepository
) : ViewModel(), OnBoardingVMI {

    private val mUiState = MutableStateFlow(UserDataUI())
    override val uiState = mUiState.asStateFlow()

    private val mAccountCreateState = Channel<Boolean>()
    override val accountCreateState = mAccountCreateState.receiveAsFlow()

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
            name.isNotBlank()
                    && surname.isNotBlank()
                    && accountType != AccountType.NONE
                    && birthDate != null
        }
    }

    override fun updateBirthDate(localDate: LocalDate) {
        viewModelScope.launch {
            mUiState.emit(uiState.value.copy(birthDate = localDate))
        }
    }

    override fun addTrainerPlace(place: Place) {
        viewModelScope.launch {
            mUiState.emit(
                uiState.value.run {
                    val trainerPlacesNew = trainerPlaces.toMutableList()
                    trainerPlacesNew.add(place)
                    uiState.value.copy(trainerPlaces = trainerPlacesNew)
                }
            )
        }
    }

    override fun removeTrainerPlace(place: Place) {
        viewModelScope.launch {
            mUiState.emit(
                uiState.value.run {
                    val trainerPlacesNew = trainerPlaces.toMutableList()
                    trainerPlacesNew.remove(place)
                    uiState.value.copy(trainerPlaces = trainerPlacesNew)
                }
            )
        }
    }

    override fun saveAccount() {
        viewModelScope.launch(Dispatchers.Default) {
            uiState.value.let { user ->
                loggedFlow.collectLatest {
                    if (it != null) {
                        when (user.accountType) {
                            AccountType.TRAINER -> {
                                userRepository.saveNewTrainerUser(
                                    it.uid,
                                    user.name,
                                    user.surname,
                                    user.birthDate!!
                                        .atStartOfDayIn(TimeZone.UTC),
                                    user.accountType,
                                    user.trainerPlaces
                                )

                            }

                            AccountType.USER -> {
                                userRepository.saveNewUser(
                                    it.uid,
                                    user.name,
                                    user.surname,
                                    user.birthDate!!
                                        .atStartOfDayIn(TimeZone.UTC),
                                    user.accountType
                                )
                            }

                            else -> Unit

                        }

                        mAccountCreateState.send(true)
                    } else {
                        //TODO SHOW ERROR
                    }
                }
            }
        }
    }

    data class UserDataUI(
        val name: String = "",
        val surname: String = "",
        val height: Int? = null,
        val weight: String? = null,
        val accountType: AccountType = AccountType.NONE,
        val birthDate: LocalDate? = null,
        val trainerPlaces: List<Place> = emptyList()
    ) {
        fun getFormattedBirthDate(): String {
            return birthDate?.toString() ?: ""
        }
    }
}