package com.xr6software.cellphonegallery.model

import java.io.Serializable
import java.util.*
//class for the Api call for the cellphones list and the inner json classes
data class Cellphone (

    val id: String = "",
    val href: String = "",
    val brand: String = "",
    val name: String = "",
    val productNumber: String = "",
    val version: String = "",
    val priority : Int = 0,
    val slug: String = "",
    val codeNMU: String = "",
    val published_at: Date = Date(),
    val type: String = "",
    val chargeCode: String = "",
    val model: String = "",
    val sourceSystemName: String = "",
    val taxIncludedSellingPrice : Int = 0,
    val refurbished : Boolean = false,
    val processor: String = "",
    val internalStorage: String = "",
    val externalStorage: String = "",
    val deviceStorage: String = "",
    val mainCamera: String = "",
    val secondaryCamera: String = "",
    val screenSize : Double = 0.0,
    val screenType: String = "",
    val installmentsTag: String = "",
    val discountTag: String = "",
    val topTag: String = "",
    val bottomTag: String = "",
    val taxIncludedChargeAmount : Int = 0,
    val taxIncludeAmount : Int = 0,
    val manufacture: String = "",
    val legal: String = "",
    val seoDescription: String = "",
    val videoUrl: String = "",
    val featureTitle: String = "",
    val installments : Int = 0,
    val cybermonday : Boolean = false,
    val unpublishedByUser: String = "",
    val installmentsTagAmount: String = "",
    val featureImage: String = "",
    val images: ArrayList<Image> = ArrayList(),
    val mainImage: MainImage = MainImage(),
    val topDecorator: Any? = null,
    val bottomDecorator: BottomDecorator = BottomDecorator(),
    val color: Color = Color(),
    val collections: ArrayList<String> = ArrayList()

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
    val alternativeText: String = "",
    val caption: String = "",
    val width : Int = 0,
    val height : Int = 0,
    val url: String = "",
    val thumbnailUrl: String = ""
) : Serializable

data class BottomDecorator (
    val alternativeText: String = "",
    val caption: String = "",
    val url: String = "",
    val thumbnailUrl: String = ""
) : Serializable

data class Color(
    val name : String = "",
    val hex : String = ""
) : Serializable




