package com.xr6software.cellphonegallery.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.xr6software.cellphonegallery.R
import com.xr6software.cellphonegallery.model.Cellphone


/**
@author Hernán Carrera
@version 1.0
This is class extends Recycler view. Adapter to parse data from the cellphones list to the listview items
 */

class AdapterCellphone(val adapterCellphoneClickListener: AdapterCellphoneClickListener) : RecyclerView.Adapter<AdapterCellphone.ViewHolder>() {

    var cellphoneList = ArrayList<Cellphone>()

    fun updateDataOnView(cellphones : ArrayList<Cellphone>) {
        cellphoneList.addAll(cellphones)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return   AdapterCellphone.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_cellphone, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cellphone : Cellphone = cellphoneList[position]
        holder.textViewName.text = cellphone.name
        holder.textViewDesc.text = cellphone.installmentsTag
        holder.textViewOther.text = cellphone.topTag
        Picasso.get().load(cellphone.mainImage.url)
            .resize(100,100)
            .into(holder.imgViewCellphone)
        holder.itemView.setOnClickListener {
            adapterCellphoneClickListener.onClick(cellphone, position)
        }
    }

    override fun getItemCount() = cellphoneList.size

    class ViewHolder(v : View) : RecyclerView.ViewHolder(v) {

        val textViewName : TextView = v.findViewById(R.id.item_textview_title)
        val textViewDesc : TextView = v.findViewById(R.id.item_textview_implements)
        val textViewOther : TextView = v.findViewById(R.id.item_textview_toptag)
        val imgViewCellphone: ImageView = v.findViewById(R.id.item_imgview)

    }


}