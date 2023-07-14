package mx.org.samtech.samplestore.retrofit

import com.google.gson.annotations.SerializedName

data class Apps(
    @SerializedName("urlimage") var urlimage: String? = null,
    @SerializedName("urlscreenshot") var urlscreenshot: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("price") var price: Double? = null,
    @SerializedName("rating") var rating: Int? = null,
    @SerializedName("opinions") var opinions: ArrayList<Opinions> = arrayListOf()
)