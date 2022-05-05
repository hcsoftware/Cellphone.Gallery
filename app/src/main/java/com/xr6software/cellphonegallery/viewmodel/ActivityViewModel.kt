package com.xr6software.cellphonegallery.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.xr6software.cellphonegallery.model.Cellphone
import com.xr6software.cellphonegallery.model.CellphoneDetail
import com.xr6software.cellphonegallery.network.APIService
import com.xr6software.cellphonegallery.network.Callback
import java.lang.Exception

//This class handles the data from the activity so it's separated from the view.
//the main methods requests to the api service the responses, format the results, and then save the data in the liveData Objects.
class ActivityViewModel : ViewModel() {

    private var cellphones = MutableLiveData<ArrayList<Cellphone>>()
    private var isLoading : MutableLiveData<Boolean> = MutableLiveData()
    private var cellphone : MutableLiveData<CellphoneDetail> = MutableLiveData()
    private var apiConnectionError : MutableLiveData<Boolean> = MutableLiveData()

    fun getCellphonesFromAPI(context : Context){

        isLoading.value = true;
        APIService().getCellphones(context, object : Callback<String>{
            override fun onSucces(result: String?) {
                parseResponseToArray(result!!)
                isLoading.value = false;
                apiConnectionError.value = false
            }

            override fun onFailure(exception: Exception) {
                //Show error message
                isLoading.value = false;
                apiConnectionError.value = true
            }
        })

    }

    fun getCellphoneByIdFromAPI(context : Context, cellphone: Cellphone, position: Int) {

        APIService().getCellphoneById(context, cellphone, position , object : Callback<String>{
            override fun onSucces(result: String?) {
                parseResponseToCellphone(result!!)
            }

            override fun onFailure(exception: Exception) {
                //println(exception)
            }
        })

    }

    fun parseResponseToArray(strResponse: String) {
        cellphones.value = Gson().fromJson(strResponse, Array<Cellphone>::class.java).toList() as ArrayList<Cellphone>
    }

    fun parseResponseToCellphone(strResponse: String) {
        cellphone.value = Gson().fromJson(strResponse,CellphoneDetail::class.java)
    }

    fun getCellphones() : LiveData<ArrayList<Cellphone>> {
        return cellphones
    }

    fun getLoading() : MutableLiveData<Boolean> {
        return isLoading
    }

    fun getCellphone() : LiveData<CellphoneDetail> {
        return cellphone
    }

    fun getApiConnectionError() : LiveData<Boolean> {
        return apiConnectionError
    }

}