package com.xr6software.cellphonegallery.repositories.model

sealed class RepositoryStatus <out T>{
    data class Success<out T>(val data: T? = null): RepositoryStatus<T>()
    data class Failed(val message: String? = null): RepositoryStatus<Nothing>()
}