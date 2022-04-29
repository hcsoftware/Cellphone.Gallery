package com.xr6software.cellphonegallery.network

import android.content.Context
import android.net.NetworkRequest
import com.android.volley.Header
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.xr6software.cellphonegallery.model.Cellphone
import android.text.Html
import android.util.Log
import java.nio.charset.Charset


//This class makes the requests to the API services and then sends the result to the viewmodel through the callback interface
class APIService {

    fun getCellphones(context: Context, callback: Callback<String>) {

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

    //This method fix unicode problem, the api doesn't specify the right Unicode.
    fun fixUnicode(response : String) : String {
        return response.toString().replace("Ã³","ó").replace("Ã©","é")
    }

    fun getCellphoneById(
        context: Context,
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