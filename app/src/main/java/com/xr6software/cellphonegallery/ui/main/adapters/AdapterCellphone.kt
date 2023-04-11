package com.xr6software.cellphonegallery.ui.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.xr6software.cellphonegallery.R
import com.xr6software.cellphonegallery.model.Cellphone


/**
@author Hernán Carrera
@version 1.0
This is class extends Recycler view. Adapter to parse data from the cellphones list to the listview items
 */

class AdapterCellphone(private val adapterCellphoneClickListener: AdapterCellphoneClickListener) : RecyclerView.Adapter<AdapterCellphone.ViewHolder>() {

    var cellphoneList = ArrayList<Cellphone>()

    fun updateDataOnView(cellphones : List<Cellphone>) {
        cellphoneList.addAll(cellphones)
        notifyDataSetChanged()
    }

    fun setData(newCellphoneList: ArrayList<Cellphone>){
        val cellphoneListDiffUtil = CellphoneListDiffUtil(cellphoneList, newCellphoneList)
        val diffResults = DiffUtil.calculateDiff(cellphoneListDiffUtil)
        cellphoneList = newCellphoneList
        diffResults.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return   ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_cellphone, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cellphone : Cellphone = cellphoneList[position]
        holder.textViewName.text = cellphone.name
        holder.textViewDesc.text = cellphone.discountTag
        holder.textViewOther.text = cellphone.promotionTag
        holder.imgViewCellphone.load(cellphone.image.url){
            error(com.google.android.material.R.drawable.mtrl_ic_error)
        }
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

    class CellphoneListDiffUtil(private val oldList : List<Cellphone>, private val newList : List<Cellphone>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldList[oldItemPosition].name == newList[newItemPosition].name)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return when {
                oldList[oldItemPosition].promotionTag != newList[newItemPosition].promotionTag -> false
                oldList[oldItemPosition].name != newList[newItemPosition].name -> false
                else -> {true}
            }
        }
    }

}