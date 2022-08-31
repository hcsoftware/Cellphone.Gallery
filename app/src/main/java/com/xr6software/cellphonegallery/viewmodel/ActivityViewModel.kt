package com.xr6software.cellphonegallery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.xr6software.cellphonegallery.model.Cellphone
import com.xr6software.cellphonegallery.model.CellphoneDetail
import com.xr6software.cellphonegallery.network.APIService
import com.xr6software.cellphonegallery.network.Callback
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
@author Hernán Carrera
@version 1.0
This class handles the data from the activity so it's separated from the view.
The main methods requests to the api service the responses, format the results, and then save the data in the liveData Objects.
 */

@HiltViewModel
class ActivityViewModel @Inject constructor(private val apiService: APIService) : ViewModel() {

    private var cellphones = MutableLiveData<ArrayList<Cellphone>>()
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    private var cellphone: MutableLiveData<CellphoneDetail> = MutableLiveData()
    private var apiConnectionError: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * This method calls the Api Service for the Cellphones List and load the info in the liveData Cellphone List via parseResponse method.
     */
    fun getCellphonesFromAPI() {

        isLoading.value = true;
        apiService.getCellphones(object : Callback<String> {
            override fun onSucces(result: String?) {
                parseResponseToArray(result!!)
                isLoading.value = false;
                apiConnectionError.value = false
            }

            override fun onFailure(exception: Exception) {
                isLoading.value = false;
                apiConnectionError.value = true
            }
        })

    }

    /**
     * This method calls the Api Service for the Cellphone Details from and id an a cellphone obj. Then load the info in the liveData Cellphone List via parseResponse method.
     * @param cellphone cellphone object for request.
     * @param position cellphone Id for request.
     */
    fun getCellphoneByIdFromAPI(cellphone: Cellphone, position: Int) {

        apiService.getCellphoneById(cellphone, position, object : Callback<String> {
            override fun onSucces(result: String?) {
                parseResponseToCellphone(result!!)
            }

            override fun onFailure(exception: Exception) {
                println(exception)
            }
        })

    }

    fun parseResponseToArray(strResponse: String) {
        cellphones.value = Gson().fromJson(strResponse, Array<Cellphone>::class.java)
            .toList() as ArrayList<Cellphone>
    }

    fun parseResponseToCellphone(strResponse: String) {
        cellphone.value = Gson().fromJson(strResponse, CellphoneDetail::class.java)
    }

    fun getCellphones(): LiveData<ArrayList<Cellphone>> {
        return cellphones
    }

    fun getLoading(): MutableLiveData<Boolean> {
        return isLoading
    }

    fun getCellphone(): LiveData<CellphoneDetail> {
        return cellphone
    }

    fun getApiConnectionError(): LiveData<Boolean> {
        return apiConnectionError
    }

}