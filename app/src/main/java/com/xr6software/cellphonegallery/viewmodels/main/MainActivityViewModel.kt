package com.xr6software.cellphonegallery.viewmodels.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.xr6software.cellphonegallery.model.Cellphone
import com.xr6software.cellphonegallery.network.APIService
import com.xr6software.cellphonegallery.network.Callback
import com.xr6software.cellphonegallery.repositories.CellphoneRepositoryImp
import com.xr6software.cellphonegallery.repositories.model.RepositoryStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
@author Hernán Carrera
@version 1.2
This class handles the data from the activity so it's separated from the view.
Connects to the repository and handles UiState LiveData to update activity.
 */

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val apiService: APIService,
    private val cellphoneRepositoryImp: CellphoneRepositoryImp)
    : ViewModel() {

    private val uiFragmentState = MutableLiveData<UiFragmentState>()
    fun getUiFragmentState() = uiFragmentState

    sealed class UiFragmentState {
        object Loading : UiFragmentState()
        data class Error(val error: String) : UiFragmentState()
        data class Cellphones(val cellphoneList: List<Cellphone>) : UiFragmentState()
        data class CellphoneInfo(val cellphone: Cellphone) : UiFragmentState()
    }

    /**
     * Loads cellphones list from the repository.
     * Updates the UiState
     */
    fun loadCellphones() {
        uiFragmentState.value = UiFragmentState.Loading
        viewModelScope.launch {
            when (val result = cellphoneRepositoryImp.getCellphonesList()) {
                is RepositoryStatus.Success<List<Cellphone>> -> {
                    uiFragmentState.postValue(UiFragmentState.Cellphones(result.data!!))
                }
                is RepositoryStatus.Failed -> {
                    uiFragmentState.postValue(UiFragmentState.Error(result.message!!))
                }
            }
        }
    }

    /**
     * Loads cellphone detail given an id. from the repository.
     * Updates the UiState
     * @param position -> cellphone id.
     */
    fun loadCellphoneInfo(position: Int) {
        uiFragmentState.value = UiFragmentState.Loading
        viewModelScope.launch {
            when (val result = cellphoneRepositoryImp.getCellphoneDetail(position)) {
                is RepositoryStatus.Success<Cellphone> -> {
                    uiFragmentState.postValue(UiFragmentState.CellphoneInfo(result.data!!))
                }
                is RepositoryStatus.Failed -> {
                    uiFragmentState.postValue(UiFragmentState.Error(result.message!!))
                }
            }
        }
    }



    private var cellphones = MutableLiveData<ArrayList<Cellphone>>()
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    private var cellphone: MutableLiveData<Cellphone> = MutableLiveData()
    private var apiConnectionError: MutableLiveData<Boolean> = MutableLiveData()

    @Deprecated("use loadCellphones()")
    /**
     *
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

    @Deprecated("use loadCellphoneInfo()")
    /**
     * This method calls the Api Service for the Cellphone Details from and id an a cellphone obj. Then load the info in the liveData Cellphone List via parseResponse method.
     * @param cellphone cellphone object for request.
     * @param position cellphone Id for request.
     */
    fun getCellphoneByIdFromAPI(cellphone: Cellphone, position: Int) {
        isLoading.value = true;
        apiService.getCellphoneById(cellphone, position, object : Callback<String> {
            override fun onSucces(result: String?) {
                parseResponseToCellphone(result!!)
                isLoading.value = false;
            }

            override fun onFailure(exception: Exception) {
                println(exception)
                isLoading.value = false;
            }
        })

    }

    fun parseResponseToArray(strResponse: String) {
        cellphones.value = Gson().fromJson(strResponse, Array<Cellphone>::class.java)
            .toList() as ArrayList<Cellphone>
    }

    fun parseResponseToCellphone(strResponse: String) {
        cellphone.value = Gson().fromJson(strResponse, Cellphone::class.java)
    }

}