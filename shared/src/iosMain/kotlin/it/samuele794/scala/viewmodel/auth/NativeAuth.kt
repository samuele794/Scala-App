package it.samuele794.scala.viewmodel.auth

import dev.gitlive.firebase.auth.AuthCredential
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

actual class NativeAuth {
    //TODO CHANGE THIS FOR LOGIN LOGIC
    actual val credentialFlow: Flow<AuthCredential> = Channel<AuthCredential>().receiveAsFlow()

    init {

    }

}