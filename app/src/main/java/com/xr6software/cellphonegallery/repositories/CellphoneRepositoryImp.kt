package com.xr6software.cellphonegallery.repositories

import com.xr6software.cellphonegallery.model.*
import com.xr6software.cellphonegallery.network.APIService
import com.xr6software.cellphonegallery.repositories.model.RepositoryStatus
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@InstallIn(SingletonComponent::class)
@Module
class CellphoneRepositoryImp @Inject constructor(
    private val apiService: APIService
) : CellphoneRepository {

    /**
     *  returns the cellphones list from ApiService
     *  @return List<Cellphone>
     */
    override suspend fun getCellphonesList(): RepositoryStatus<List<Cellphone>> {
        val apiResponse = apiService.getCellphonesFromApiRetrofit()
        return if (apiResponse.error.isNullOrBlank()) {
            RepositoryStatus.Success(apiResponse.values)
        } else {
            RepositoryStatus.Failed(apiResponse.error)
        }
    }

    /**
     *  returns the cellphone detail given an id from ApiService
     *  @param cellphoneId cellphone unique id.
     *  @return Cellphone object
     */
    override suspend fun getCellphoneDetail(cellphoneId: Int): RepositoryStatus<Cellphone> {
        val apiResponse = apiService.getCellphoneFromApiRetrofit(cellphoneId)
        return if (apiResponse.error.isNullOrBlank()) {
            RepositoryStatus.Success(apiResponse.values)
        } else {
            RepositoryStatus.Failed(apiResponse.error)
        }
    }


}