package it.samuele794.scala.model

import dev.icerock.moko.resources.StringResource
import it.samuele794.scala.resources.SharedRes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
enum class AccountType(@Transient val accountName: StringResource) {
    @SerialName("NONE")
    NONE(SharedRes.strings.select_account_type),

    @SerialName("USER")
    USER(SharedRes.strings.amateur),

    @SerialName("TRAINER")
    TRAINER(SharedRes.strings.trainer)
}
