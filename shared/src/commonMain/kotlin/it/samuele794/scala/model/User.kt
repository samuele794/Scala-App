package it.samuele794.scala.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("name")
    val name: String = "",
    @SerialName("surname")
    val surname: String = "",
    @SerialName("birthDate")
    val birthDate: Instant? = null,
    @SerialName("accountType")
    val accountType: AccountType = AccountType.USER,
    @SerialName("needOnBoard")
    val needOnBoard: Boolean = true,
    @SerialName("trainerPlaces")
    val trainerPlaces: List<String> = emptyList()
)