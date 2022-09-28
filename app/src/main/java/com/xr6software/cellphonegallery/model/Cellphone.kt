package com.xr6software.cellphonegallery.model

import java.io.Serializable
import java.util.*

//Data classes
data class Cellphone (

    val name: String = "",
    val installmentsTag: String = "",
    val topTag: String = "",
    val mainImage: MainImage = MainImage(),

) : Serializable

data class CellphoneDetail(

    val id: String,
    val brand: String,
    val name: String,
    val legal: String,
    val images: ArrayList<Image>,
    val mainImage: MainImage
) : Serializable

data class  Image(
    val alternativeText : String = "",
    val caption : String = "" ,
    val  width : Int = 0,
    val height : Int = 0,
    val  url : String = "",
    val thumbnailUrl : String =""
) : Serializable

data class MainImage (
    val url: String = "",
) : Serializable




