package mx.org.samtech.samplestore.model

import mx.org.samtech.samplestore.retrofit.Opinions

data class AppsModel(
    var appUrlImage: String,
    var appUrlScreenShoot: String,
    var appName: String,
    var appDescription: String?,
    var appPrice: Double?,
    var appRating: Int,
    var opinions: ArrayList<Opinions> = arrayListOf()
)