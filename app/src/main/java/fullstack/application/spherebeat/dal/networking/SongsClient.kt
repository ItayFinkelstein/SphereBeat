package fullstack.application.spherebeat.dal.networking

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import fullstack.application.spherebeat.BuildConfig

object SongsClient {

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            //.addInterceptor(SongsInterceptor())
            .build()
    }

    val songsApiClient: SpotifyApi by lazy {
        val retrofitClient = Retrofit.Builder()
            .baseUrl("https://api.spotify.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofitClient.create(SpotifyApi::class.java)
    }

}