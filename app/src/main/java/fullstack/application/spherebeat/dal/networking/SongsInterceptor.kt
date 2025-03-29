package fullstack.application.spherebeat.dal.networking

import fullstack.application.spherebeat.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class SongsInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.SPOTIFY_ACCESS_TOKEN}")
            .addHeader("accept", "application/json")
            .build()
        return chain.proceed(request)
    }
}