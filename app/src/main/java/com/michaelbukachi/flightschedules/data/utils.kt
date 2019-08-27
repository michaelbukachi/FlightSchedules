package com.michaelbukachi.flightschedules.data

import com.michaelbukachi.flightschedules.BuildConfig
import com.michaelbukachi.flightschedules.data.api.ApiService
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.threeten.bp.LocalDateTime
import retrofit2.HttpException
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val origRequest = chain.request()

        val token = Auth.accessToken
        val tokenType = Auth.tokenType.capitalize()

        if (token.isEmpty()) {
            return chain.proceed(origRequest)
        }

        val newRequest = origRequest.newBuilder()
            .header("Authorization", "$tokenType $token")
            .build()

        return chain.proceed(newRequest)
    }
}

class TokenAuthenticator @Inject constructor(private val apiService: Lazy<ApiService>) : Authenticator {

//    private lateinit var apiService: ApiService

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code == 401) {
            if (response.request.url.toString().contains("oauth/token")) {
                return null
            }
            val token = getNewToken()
            if (token.isEmpty()) {
                return null
            }
            return response.request.newBuilder().header("Authorization", token).build()
        }

        return null
    }

    private fun getNewToken(): String = runBlocking {
        Auth.clear()
        val payload = mutableMapOf(
            "client_id" to BuildConfig.LUFTHANSA_KEY,
            "client_secret" to BuildConfig.LUTFHANSA_SECRET,
            "grant_type" to "client_credentials"
        )
        try {
            Timber.i("Fetching new access token")
            val luftService = apiService.get().luftService
            val response = luftService.getAccessToken(payload)
            var now = LocalDateTime.now()
            now = now.plusSeconds(response.expiresIn.toLong())
            Auth.accessToken = response.accessToken
            Auth.tokenType = response.tokenType
            Auth.expiresAt = now.format(Auth.formatter)
            return@runBlocking "${response.tokenType.capitalize()} ${response.accessToken}"
        } catch (e: HttpException) {
            Timber.e("Unable to fetch token")
            Timber.e(e.response()!!.errorBody()!!.string())
            Timber.e(e)
            return@runBlocking ""
        }
    }

}

class TimeoutInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            chain.proceed(chain.request())
        } catch (e: Exception) {
            when (e) {
                is SocketTimeoutException, is UnknownHostException -> {
                    Timber.e(e)

                    val body = "{\"status\": \"error\", \"message\": \"Timeout has occurred\"}"
                        .toResponseBody("application/json".toMediaType())

                    Response.Builder()
                        .request(chain.request())
                        .protocol(Protocol.HTTP_1_1)
                        .code(500)
                        .body(body)
                        .message("Unable to connect")
                        .build()
                }
                else -> throw e
            }

        }
    }

}
