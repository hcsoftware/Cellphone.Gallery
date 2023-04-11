package com.xr6software.cellphonegallery.repositories

import com.xr6software.cellphonegallery.model.Cellphone
import com.xr6software.cellphonegallery.repositories.model.RepositoryStatus

interface CellphoneRepository {

    suspend fun getCellphonesList() : RepositoryStatus<List<Cellphone>>

    suspend fun getCellphoneDetail(cellphoneId: Int) : RepositoryStatus<Cellphone>

}