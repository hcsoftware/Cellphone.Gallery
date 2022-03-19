package com.xr6software.cellphonegallery.model

import java.io.Serializable

//class for the second service api (b) response.
data class CellphoneDetail(

    val id: String,
    val brand: String,
    val name: String,
    val legal: String,
    val images: ArrayList<Image>,
    val mainImage: MainImage
) : Serializable