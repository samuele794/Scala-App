package it.samuele794.scala.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("name")
    val name: String = "",
    @SerialName("surname")
    val surname: String = "",
    @SerialName("accountType")
    val accountType: AccountType = AccountType.USER,
    @SerialName("needOnBoard")
    val needOnBoard: Boolean = true
)