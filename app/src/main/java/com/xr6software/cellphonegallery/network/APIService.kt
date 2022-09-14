package com.xr6software.cellphonegallery.network

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.xr6software.cellphonegallery.model.Cellphone
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.json.JSONArray
import org.json.JSONObject
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

    private val requestQueue = Volley.newRequestQueue(context)

    /**
     * This method returns a string response (Json) with the cellphone List from the Api web service.
     * @param callback to return response.
     * @return response String via callback.
     */
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