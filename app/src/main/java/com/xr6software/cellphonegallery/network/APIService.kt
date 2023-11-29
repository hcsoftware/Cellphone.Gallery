package com.xr6software.cellphonegallery.network

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.xr6software.cellphonegallery.model.*
import com.xr6software.cellphonegallery.network.model.ApiResponse
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 *
 * @author Hernán Carrera
 * @version "1.2"
 * @param context via Hilt Injection
 * This class provides connection with the API web service using Retrofit Library
 */

@InstallIn(SingletonComponent::class)
@Module
class APIService @Inject constructor(@ApplicationContext val context: Context) {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
        .readTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
        .writeTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
        .retryOnConnectionFailure(true)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(NetworkConstants.RETROFIT_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(CellphoneApiService::class.java)
    companion object {
        const val TIMEOUT = 10000
        const val MAX_RETRIES = 3
    }

    suspend fun getCellphonesFromApiRetrofit(): ApiResponse<List<Cellphone>> {
        return try {
            val response = apiService.getCellphones()
            ApiResponse(null, response.body())
        } catch (e: Exception) {
            ApiResponse(e.message.toString(), null)
        }
    }

    suspend fun getCellphoneFromApiRetrofit(cellphoneId: Int): ApiResponse<Cellphone> {
        return try {
            val response = apiService.getCellphone(cellphoneId)
            ApiResponse(null, response.body())
        } catch (e: Exception) {
            ApiResponse(e.message.toString(), null)
        }
    }



    private val requestQueue = Volley.newRequestQueue(context)
    private val cellphoneListUrl = NetworkConstants.URL_CELLPHONE_LIST
    private val cellphoneDetailUrl = NetworkConstants.URL_CELLPHONE_DETAIL

    /**
     * This method gets the cellphones list from ApiService.
     * @return ApiResponse with CellphoneList object / Error msg
     */
    @Deprecated("Use retrofit methods")
    suspend fun getCellphonesFromApi() : ApiResponse<List<Cellphone>> =
        suspendCoroutine { continuation ->
            var apiResponse : ApiResponse<List<Cellphone>>
            val url = cellphoneListUrl
            val jsonArrayRequest = JsonArrayRequest(
                url,
                { response: JSONArray ->
                    apiResponse = ApiResponse(null, parseResponseToCellphoneList(response.toString()))
                    continuation.resume(apiResponse)
                },
                {
                    error ->
                    apiResponse = ApiResponse(error.message.toString(), null)
                    continuation.resume(apiResponse)
                }
            )
            addToRequestQueue(jsonArrayRequest)
        }

    /**
     * This method returns a cellphone detail given an id from ApiService.
     * @return ApiResponse with CellphoneDetail object / Error msg
     */
    @Deprecated("Use retrofit methods")
    suspend fun getCellphoneFromApi(cellphoneId: Int) : ApiResponse<Cellphone> =
        suspendCoroutine { continuation ->
            var apiResponse : ApiResponse <Cellphone>
            val url = cellphoneDetailUrl + cellphoneId
            val jsonObjectRequest = JsonObjectRequest(
                url,
                { response: JSONObject ->
                    apiResponse = ApiResponse(null, parseResponseToCellphone(response.toString()))
                    continuation.resume(apiResponse)
                },
                {   error ->
                    apiResponse = ApiResponse(error.message.toString(), null)
                    continuation.resume(apiResponse)
                }
            )
            addToRequestQueue(jsonObjectRequest)
        }

    /**
     * Add retryPolicy to Request and adds to queue
     * @param request Generic Volley Request
     */
    private fun <T> addToRequestQueue(request: Request<T>){
        request.retryPolicy = DefaultRetryPolicy(
            TIMEOUT,
            MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(request)
    }


    /**
     *  @deprecated
     * This method is should be replaced by suspend functions no callback necessary.
     * This method returns a string response (Json) with the cellphone List from the Api web service.
     * @param callback to return response.
     * @return response String via callback.
     */
    @Deprecated("use getCellphonesFromApi()")
    fun getCellphones(callback: Callback<String>) {

        val url = "https://61967289af46280017e7e0c0.mockapi.io/devices"
        val jsonArrayRequest = JsonArrayRequest(
            url,
            { response: JSONArray ->
                callback.onSucces(response.toString())
            },
            {
                callback.onFailure(it)
            }
        )
        requestQueue.add(jsonArrayRequest)

    }

    /**
     *  @deprecated
     * This method is should be replaced by suspend functions no callback necessary.
     * This method returns a string response (Json) with the cellphone details from the Api web service.
     * @param cellphone cellphoneList object.
     * @param position cellphone Id for server request.
     * @param callback to return response.
     * @return response String via callback.
     */
    @Deprecated("use getCellphoneFromApi()")
    fun getCellphoneById(
        cellphone: Cellphone,
        position: Int,
        callback: Callback<String>
    ) {
        val url = "https://61967289af46280017e7e0c0.mockapi.io/devices/$position"
        val request= JsonObjectRequest(
            url,
            { response ->
                callback.onSucces(response.toString())
            },
            {
                callback.onFailure(it)
            }
        )
        requestQueue.add(request)
    }

}