package it.samuele794.scala.viewmodel.auth

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.AuthCredential
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.tasks.asDeferred

actual class NativeAuth(private val context: Context, private val googleToken: String) {

    private val credentialChannel: Channel<AuthCredential> = Channel()
    actual val credentialFlow = credentialChannel.receiveAsFlow()

    fun getGoogleSignIn(): Intent {
        val request = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(googleToken)
            .requestProfile()
            .build()

        val signIn = GoogleSignIn.getClient(context, request)

        return signIn.signInIntent
    }

    suspend fun onGoogleLoginResult(data: Intent) {
        val signIn = GoogleSignIn.getSignedInAccountFromIntent(data)

        val result = runCatching {
            signIn.asDeferred().await()
        }

        if (result.isSuccess) {
            val credential = GoogleAuthProvider.getCredential(result.getOrThrow().idToken, null)
            credentialChannel.send(AuthCredential(credential))
        }

    }


}