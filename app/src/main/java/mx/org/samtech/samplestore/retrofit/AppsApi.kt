package mx.org.samtech.samplestore.retrofit

import retrofit2.Response
import retrofit2.http.GET

interface AppsApi {
    @GET("v1/3c78963c-2bb4-4d01-9878-62130b9ff9c2 ")
    suspend fun getApps() : Response<AppsResponse>
}