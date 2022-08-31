package com.xr6software.cellphonegallery.network

import java.lang.Exception
/**
Callback Interface for APIService and ViewModel interaction.
 */
interface Callback<T> {

    fun onSucces(result: T?)

    fun onFailure(exception: Exception)

}