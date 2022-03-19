package com.xr6software.cellphonegallery.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.xr6software.cellphonegallery.model.Cellphone

//This class makes the requests to the API services and then sends the result to the viewmodel through the callback interface
class APIService {

    fun getCellphones(context: Context, callback: Callback<String>) {

        var request: RequestQueue = Volley.newRequestQueue(context)

        val url = "https://61967289af46280017e7e0c0.mockapi.io/devices"
        val stringRequest: StringRequest = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                var strResp = response.toString()
                callback.onSucces(response)
            },
            {
                callback.onFailure(it)
            })
        request.add(stringRequest)

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