package com.xr6software.cellphonegallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xr6software.cellphonegallery.databinding.ActivityMainBinding
import com.xr6software.cellphonegallery.model.Cellphone
import com.xr6software.cellphonegallery.model.CellphoneDetail
import com.xr6software.cellphonegallery.view.AdapterCellphone
import com.xr6software.cellphonegallery.view.AdapterCellphoneClickListener
import com.xr6software.cellphonegallery.view.CellphoneDialog
import com.xr6software.cellphonegallery.viewmodel.ActivityViewModel


class MainActivity : AppCompatActivity(), AdapterCellphoneClickListener {

    private lateinit var viewBinding : ActivityMainBinding
    private lateinit var cellphoneAdapter: AdapterCellphone;
    private lateinit var viewModel : ActivityViewModel;
    private lateinit var cellphoneDialog: CellphoneDialog;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //set viewmodel , cellphone dialog and viewbinding.
        viewModel = ViewModelProvider(this).get(ActivityViewModel::class.java)
        cellphoneDialog = CellphoneDialog(this)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        val view : View = viewBinding.root
        setContentView(view)

        //Init adapter and links it view recycler view
        cellphoneAdapter = AdapterCellphone(this)
        val recyclerView : RecyclerView = viewBinding.maItemsViewHolder

        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 1)
            adapter = cellphoneAdapter
        }

        //make main call to get cellphones list
        viewModel.getCellphonesFromAPI(this)

        //as the names says, set the different observers to the viewmodel objects
        setObservers()

    }

    fun setObservers() {
        viewModel.getCellphones().observe(this, Observer {
            cellphoneAdapter.updateDataOnView(it)
        })

        viewModel.getLoading().observe(this, Observer {
            if (it == true) { viewBinding.maProgressbarHolder.visibility = View.VISIBLE}
            else {viewBinding.maProgressbarHolder.visibility = View.INVISIBLE}
        })
        viewModel.getCellphone().observe(this, Observer {
            cellphoneDialog.showDialog(it)
        })
    }

    override fun onClick(cellphone: Cellphone, position: Int) {
        viewModel.getCellphoneByIdFromAPI(this, cellphone, position)
    }

}