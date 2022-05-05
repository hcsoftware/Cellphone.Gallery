package com.xr6software.cellphonegallery

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xr6software.cellphonegallery.databinding.ActivityMainBinding
import com.xr6software.cellphonegallery.model.Cellphone
import com.xr6software.cellphonegallery.view.AdapterCellphone
import com.xr6software.cellphonegallery.view.AdapterCellphoneClickListener
import com.xr6software.cellphonegallery.view.CellphoneDialog
import com.xr6software.cellphonegallery.viewmodel.ActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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

        //Set ActionBar Title
        supportActionBar?.title = resources.getString(R.string.topbar_text)

        //Init adapter and links it view recycler view
        cellphoneAdapter = AdapterCellphone(this)
        val recyclerView : RecyclerView = viewBinding.maItemsViewHolder

        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 1)
            adapter = cellphoneAdapter
        }

        //make main call to get cellphones list
        viewModel.getCellphonesFromAPI(this)

        //Set the different observers to the viewmodel
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
        viewModel.getApiConnectionError().observe(this, Observer {
            if (it) {
                CoroutineScope(Main).launch {
                    showErrorAndFinish()
                }
            }
        })
    }

    private suspend fun showErrorAndFinish()  {
        var toast = Toast.makeText(applicationContext, resources.getString(R.string.error_loading), Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
        delay(3500)
        finish()
    }

    override fun onClick(cellphone: Cellphone, position: Int) {
        viewModel.getCellphoneByIdFromAPI(this, cellphone, position)
    }

    override fun onBackPressed() {
        showQuitDialog()
    }

    fun showQuitDialog(){
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage(R.string.quit_dialog_msg)
            .setCancelable(false)
            .setPositiveButton(R.string.quit_dialog_positive, DialogInterface.OnClickListener {
                    dialog, id -> finish()
            })
            .setNegativeButton(R.string.quit_dialog_negative, DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle(R.string.quit_dialog_title)
        alert.show()
    }

}