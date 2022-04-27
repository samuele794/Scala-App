package it.samuele794.scala.viewmodel.auth

import co.touchlab.kermit.Logger
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.AuthCredential
import dev.gitlive.firebase.auth.auth
import it.samuele794.scala.viewmodel.base.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(
    nativeAuth: NativeAuth,
    private val logger: Logger
) : ViewModel() {

    val isLoggedFlow = Firebase.auth.authStateChanged

    init {
        viewModelScope.launch {
            nativeAuth.credentialFlow.collect {
                startFirebaseAuth(it)
            }
        }
    }

    private suspend fun startFirebaseAuth(credential: AuthCredential) =
        withContext(Dispatchers.Default + SupervisorJob()) {
            runCatching {
                Firebase.auth.signInWithCredential(credential).user!!
            }
        }

    suspend fun loginByEmailPass(email: String, password: String) =
        withContext(Dispatchers.Default + SupervisorJob()) {
            val userResult = runCatching {
                Firebase.auth.signInWithEmailAndPassword(email, password).user!!
            }

            if (userResult.isFailure) {
                logger.i("Login By Email Failed, Create Account")
                Firebase.auth.createUserWithEmailAndPassword(email, password)

                return@withContext
            }

            logger.i("Login By Email Success")

        }
}

expect class NativeAuth {
    val credentialFlow: Flow<AuthCredential>
}