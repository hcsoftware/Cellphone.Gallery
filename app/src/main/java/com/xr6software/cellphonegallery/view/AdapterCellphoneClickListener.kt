package com.xr6software.cellphonegallery.view

import com.xr6software.cellphonegallery.model.Cellphone

interface AdapterCellphoneClickListener {

    fun onClick(cellphone: Cellphone, position: Int)

}