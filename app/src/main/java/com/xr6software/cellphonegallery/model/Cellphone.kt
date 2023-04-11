package com.xr6software.cellphonegallery.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class Cellphone (
    val name: String,
    @SerializedName("installmentsTag")
    val discountTag: String?,
    @SerializedName("topTag")
    val promotionTag: String?,
    @SerializedName("mainImage")
    val image: MainImage,
    val legal: String,
    val processor: String,
    val internalStorage: String,
    @SerializedName("images")
    val imagesDetail: ArrayList<Image>
)

data class MainImage (
    @SerializedName("thumbnailUrl")
    val url: String,
)

data class  Image(
    val alternativeText : String,
    val url : String,
    val thumbnailUrl : String
)

fun parseResponseToCellphoneList(response: String): List<Cellphone> =
    Gson().fromJson(response, Array<Cellphone>::class.java).toList()

fun parseResponseToCellphone(response: String): Cellphone =
    Gson().fromJson(response, Cellphone::class.java)








