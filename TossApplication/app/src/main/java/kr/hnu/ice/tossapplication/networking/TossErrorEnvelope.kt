package kr.hnu.ice.tossapplication.networking

import com.google.gson.annotations.SerializedName

data class TossErrorEnvelope(
    @SerializedName("error") val error: ErrorDetails
)

data class ErrorDetails(
    @SerializedName("requestId") val requestId: String,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Map<String, Any>?
)
