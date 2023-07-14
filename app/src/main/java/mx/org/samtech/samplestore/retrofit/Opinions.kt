package mx.org.samtech.samplestore.retrofit

import com.google.gson.annotations.SerializedName

data class Opinions(
    @SerializedName("username") var username: String? = null,
    @SerializedName("opinion") var opinion: String? = null
)