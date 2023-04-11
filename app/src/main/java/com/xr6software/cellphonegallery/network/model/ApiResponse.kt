package com.xr6software.cellphonegallery.network.model

data class ApiResponse<T> (
    var error: String?,
    val values: T?
)