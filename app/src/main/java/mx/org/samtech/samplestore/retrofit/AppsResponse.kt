package mx.org.samtech.samplestore.retrofit

import com.google.gson.annotations.SerializedName

data class AppsResponse(
    @SerializedName("iconsurl") var iconsurl: String? = null,
    @SerializedName("screenshotsurl") var screenshotsurl: String? = null,
    @SerializedName("apps") var apps: ArrayList<Apps> = arrayListOf()
)