package fullstack.application.spherebeat.dal.networking

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface SpotifyApi {
    @GET("v1/search")
    fun searchTracks(
        @Query("q") query: String?,
        @Query("type") type: String?,
        @Header("Authorization") token: String?
    ): Call<SpotifyResponse>
}
