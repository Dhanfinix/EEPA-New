package com.dhandev.eepa.divkit

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import retrofit2.http.GET

object DivKitApi {

    private const val baseUrl = "https://imam-syafi.github.io/sdui/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    val service: DivKitApiService by lazy {
        retrofit.create()
    }
}

interface DivKitApiService {

    @GET("api/news.json")
    suspend fun getNewsUi(): String
}
