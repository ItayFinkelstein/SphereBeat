package fullstack.application.spherebeat.dal.networking

import android.util.Log
import fullstack.application.spherebeat.BuildConfig
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import android.util.Base64

object SpotifyAuth {
    private const val CLIENT_ID = BuildConfig.API_KEY
    private const val CLIENT_SECRET = BuildConfig.API_SECRET
    private const val SPOTIFY_TOKEN_URL = BuildConfig.SPOTIFY_TOKEN_URL
    var accessToken: String? = null

    fun fetchAccessToken(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val client = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("grant_type", "client_credentials")
            .build()

        val credentials = "$CLIENT_ID:$CLIENT_SECRET"
        val encodedCredentials = Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

        val request = Request.Builder()
            .url(SPOTIFY_TOKEN_URL)
            .addHeader("Authorization", "Basic $encodedCredentials")
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError(e.message ?: "Request failed")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let {
                    val json = JSONObject(it)
                    Log.d("SpotifyAuth", "Response: $json")
                    accessToken = json.getString("access_token")
                    onSuccess(accessToken!!)
                } ?: onError("Failed to parse response")
            }
        })
    }
}
