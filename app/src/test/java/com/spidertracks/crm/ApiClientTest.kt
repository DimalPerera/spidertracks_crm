package com.spidertracks.crm
import com.spidertracks.crm.api.ApiClient
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

class ApiClientTest {

    @Test
    fun `test ApiClient`() {
        val retrofit: Retrofit = ApiClient.retrofit
        assert(retrofit.baseUrl().toString() == ApiClient.BASE_URL)
        assert(retrofit.callFactory() == ApiClient.httpClient)
    }
}