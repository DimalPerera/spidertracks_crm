package com.spidertracks.crm.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

class ApiClient {
    /**
    Companion object of the class.
    Declares a constant BASE_URL that is used as the base url of the network requests.
    Declares httpClient using OkHttpClient.Builder, adding an interceptor of HeaderInterceptor
    Declares retrofit as a lazy property that creates a Retrofit object with the BASE_URL,
    adding a JacksonConverterFactory, a RxJava3CallAdapterFactory and the httpClient
     */
    companion object {

        const val BASE_URL = "https://xvy4yik9yk.us-west-2.awsapprunner.com/"

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor())
            .build()

        val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(httpClient)
                .build()
        }

        val service: ApiService by lazy {
            retrofit.create(ApiService::class.java)
        }
    }
}

internal class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .build()

        val requestTime = System.nanoTime()

        Log.i(
            "HTTP",
            java.lang.String.format(
                "Sending request %s on %s%n%s",
                request.url, chain.connection(), request.headers
            )
        )
        val response = chain.proceed(request)

        val responseTime = System.nanoTime()

        Log.i(
            "HTTP",
            String.format(
                "Received response for %s in %.1fms%n%s%n%s%n%s",
                response.request.url,
                (responseTime - requestTime) / 1e6,
                response.headers,
                response.toString(),
                response.peekBody(20480).string()
            )
        )
        return response
    }
}