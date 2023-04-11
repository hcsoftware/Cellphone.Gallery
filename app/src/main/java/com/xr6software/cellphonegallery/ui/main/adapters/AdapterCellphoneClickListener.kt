package com.xr6software.cellphonegallery.ui.main.adapters

import com.xr6software.cellphonegallery.model.Cellphone

interface AdapterCellphoneClickListener {

    fun onClick(cellphone: Cellphone, position: Int)

}