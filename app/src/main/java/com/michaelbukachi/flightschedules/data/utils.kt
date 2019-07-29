package com.michaelbukachi.flightschedules.data

import com.michaelbukachi.flightschedules.data.api.Auth
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import timber.log.Timber
import java.net.SocketTimeoutException


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

class TimeoutInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            chain.proceed(chain.request())
        } catch (e: SocketTimeoutException) {
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
    }

}
