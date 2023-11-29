package com.xr6software.cellphonegallery.network

import com.xr6software.cellphonegallery.model.Cellphone
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CellphoneApiService {
    @GET(NetworkConstants.RETROFIT_URL_CELLPHONE_LIST)
    suspend fun getCellphones(): Response<List<Cellphone>>

    @GET("${NetworkConstants.RETROFIT_URL_CELLPHONE_DETAIL}{id}")
    suspend fun getCellphone(@Path("id") cellphoneId: Int): Response<Cellphone>
}