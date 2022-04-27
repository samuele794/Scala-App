package it.samuele794.scala.viewmodel.login

import co.touchlab.kermit.Logger
import dev.gitlive.firebase.auth.FirebaseUser
import it.samuele794.scala.repository.UserRepository
import it.samuele794.scala.viewmodel.base.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val logger: Logger,
    private val loggedFlow: Flow<FirebaseUser?>,
    private val userRepository: UserRepository
) : ViewModel() {

    private val mNavDirection = MutableStateFlow<NavDirection>(NavDirection.None)
    val navDirection = mNavDirection.asStateFlow()


    init {
        viewModelScope.launch {
            loggedFlow.collect { user ->
                if (user != null) {
                    checkOnBoardingOrLogin(user.uid)
                } else {
                    mNavDirection.emit(NavDirection.Login)
                }

            }
        }
    }

    fun resetNavigation() {
        viewModelScope.launch {
            mNavDirection.emit(NavDirection.None)
        }
    }

    private suspend fun checkOnBoardingOrLogin(userUid: String) {
        if (userRepository.userExist(userUid)) {
            userRepository.observeUserObj(userUid).collect { user ->
                if (user != null) {
                    if (user.needOnBoard) {
                        mNavDirection.emit(NavDirection.OnBoarding)
                    } else {
                        mNavDirection.emit(NavDirection.Logged)
                    }
                } else {
                    mNavDirection.emit(NavDirection.Login)
                }
            }
        } else {
            userRepository.createUser(userUid)
            mNavDirection.emit(NavDirection.OnBoarding)
        }
    }

    sealed class NavCurrent {
        object Splash : NavCurrent()
        object Login : NavCurrent()
    }

    sealed class NavDirection {
        object None : NavDirection()
        object OnBoarding : NavDirection()
        object Login : NavDirection()
        object Logged : NavDirection()
        object Error : NavDirection()
    }
}