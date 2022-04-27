package it.samuele794.scala.repository

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.firestore
import it.samuele794.scala.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface UserRepository {

    suspend fun userExist(userUid: String): Boolean
    suspend fun createUser(userUid: String)
    suspend fun updateNameSurname(userUid: String, name: String, surname: String)
    fun observeUserDocument(userUid: String): Flow<DocumentSnapshot>
    fun observeUserObj(userUid: String): Flow<User?>
}

class UserRepositoryImpl(private val firebaseAuth: Flow<FirebaseUser?> = Firebase.auth.authStateChanged) :
    UserRepository {

    override fun observeUserDocument(userUid: String): Flow<DocumentSnapshot> {
        return Firebase.firestore.collection(USER_COLLECTION)
            .document(userUid)
            .snapshots
    }

    override fun observeUserObj(userUid: String): Flow<User?> {
        return observeUserDocument(userUid)
            .map {
                try {
                    it.data(User.serializer())
                } catch (ex: Exception) {
                    null
                }
            }
    }

    override suspend fun userExist(userUid: String): Boolean {
        return Firebase.firestore.collection(USER_COLLECTION)
            .document(userUid)
            .get()
            .exists
    }

    override suspend fun createUser(userUid: String) {
        setUser(userUid, User())
    }

    override suspend fun updateNameSurname(userUid: String, name: String, surname: String) {
        setUser(
            userUid,
            getUser(userUid)
                .copy(name = name, surname = surname)
        )
    }

    private suspend fun setUser(userUid: String, user: User) {
        Firebase.firestore.collection(USER_COLLECTION)
            .document(userUid)
            .set(User.serializer(), user)
    }

    private suspend fun getUser(userUid: String): User {
        return Firebase.firestore.collection(USER_COLLECTION)
            .document(userUid)
            .get()
            .data()
    }

    companion object {
        private const val USER_COLLECTION = "users"
    }
}