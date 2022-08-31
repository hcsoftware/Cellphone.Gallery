package com.xr6software.cellphonegallery.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.xr6software.cellphonegallery.model.Cellphone
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

/**
 *
 * @author Hernán Carrera
 * @version "1.0"
 * @param context via Hilt Injection
 * This class provides connection with the API web service using Volley Library
 */

@InstallIn(SingletonComponent::class)
@Module
class APIService @Inject constructor(@ApplicationContext val context: Context) {

    /**
     * This method returns a string response (Json) with the cellphone List from the Api web service.
     * @param callback to return response.
     * @return response String via callback.
     */
    fun getCellphones(callback: Callback<String>) {

        var request: RequestQueue = Volley.newRequestQueue(context)
        val url = "https://61967289af46280017e7e0c0.mockapi.io/devices"

        val stringRequest: StringRequest = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                callback.onSucces(fixUnicode(response))
            },
            {
                callback.onFailure(it)
            }

        )
        request.add(stringRequest)
    }

    /**
     * This method fix unicode problem, the Api doesn't specify the right Unicode.
     * @param response String to replace wrong chars.
     * @return String.
     */
    private fun fixUnicode(response: String): String {
        return response.toString().replace("Ã³", "ó").replace("Ã©", "é")
    }

    /**
     * This method returns a string response (Json) with the cellphone details from the Api web service.
     * @param cellphone cellphoneList object.
     * @param position cellphone Id for server request.
     * @param callback to return response.
     * @return response String via callback.
     */
    fun getCellphoneById(
        cellphone: Cellphone,
        position: Int,
        callback: Callback<String>
    ) {

        var request: RequestQueue = Volley.newRequestQueue(context)
        val url = "https://61967289af46280017e7e0c0.mockapi.io/devices/$position"
        val stringRequest: StringRequest = StringRequest(

            Request.Method.GET, url,
            { response ->
                callback.onSucces(response)
            },
            {
                callback.onFailure(it)
            })
        request.add(stringRequest)

    }

}