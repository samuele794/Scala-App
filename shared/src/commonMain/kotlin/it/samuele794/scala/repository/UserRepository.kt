package it.samuele794.scala.repository

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.firestore
import it.samuele794.scala.model.AccountType
import it.samuele794.scala.model.User
import it.samuele794.scala.model.maps.Place
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant

interface UserRepository {

    suspend fun userExist(userUid: String): Boolean
    suspend fun createUser(userUid: String)
    suspend fun updateNameSurname(userUid: String, name: String, surname: String)
    fun observeUserDocument(userUid: String): Flow<DocumentSnapshot>
    fun observeUserObj(userUid: String): Flow<User?>

    suspend fun saveNewTrainerUser(
        userUid: String,
        name: String,
        surname: String,
        birthDate: Instant,
        accountType: AccountType,
        trainerPlaces: List<Place>
    )

    suspend fun saveNewUser(
        userUid: String,
        name: String,
        surname: String,
        birthDate: Instant,
        accountType: AccountType
    )
}

class UserRepositoryImpl(
    private val firebaseAuth: Flow<FirebaseUser?> = Firebase.auth.authStateChanged,
    private val placeRepository: PlaceRepository
) :
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

    override suspend fun saveNewTrainerUser(
        userUid: String,
        name: String,
        surname: String,
        birthDate: Instant,
        accountType: AccountType,
        trainerPlaces: List<Place>
    ) {
        val placesRef = trainerPlaces.map {
            placeRepository.addPlace(it)
                .path
        }

        val user = getUser(userUid)
            .copy(
                name = name,
                surname = surname,
                birthDate = birthDate,
                accountType = accountType,
                needOnBoard = false,
                trainerPlaces = placesRef
            )

        setUser(
            userUid,
            user
        )
    }

    override suspend fun saveNewUser(
        userUid: String,
        name: String,
        surname: String,
        birthDate: Instant,
        accountType: AccountType
    ) {
        val user = getUser(userUid)
            .copy(
                name = name,
                surname = surname,
                birthDate = birthDate,
                accountType = accountType,
                needOnBoard = false
            )

        setUser(
            userUid,
            user
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
            .data(User.serializer())
    }

    companion object {
        const val USER_COLLECTION = "users"
    }
}