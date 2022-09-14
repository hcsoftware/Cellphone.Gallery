package com.xr6software.cellphonegallery.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xr6software.cellphonegallery.R
import com.xr6software.cellphonegallery.databinding.ActivityMainBinding
import com.xr6software.cellphonegallery.model.Cellphone
import com.xr6software.cellphonegallery.viewmodel.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AdapterCellphoneClickListener {

    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var cellphoneAdapter: AdapterCellphone
    @Inject
    lateinit var cellphoneDialog: CellphoneDialog
    private val viewModel: ActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = viewBinding.root
        setContentView(view)

        supportActionBar?.title = resources.getString(R.string.topbar_text)
        cellphoneAdapter = AdapterCellphone(this)
        val recyclerView: RecyclerView = viewBinding.maItemsViewHolder

        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 1)
            adapter = cellphoneAdapter
        }

        viewModel.getCellphonesFromAPI()

        setObservers()
    }

    private fun setObservers() {
        viewModel.getCellphones().observe(this, Observer {
            cellphoneAdapter.updateDataOnView(it)
        })

        viewModel.getLoading().observe(this, Observer {
            if (it == true) {
                viewBinding.maProgressbarHolder.visibility = View.VISIBLE
            } else {
                viewBinding.maProgressbarHolder.visibility = View.INVISIBLE
            }
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

    private suspend fun showErrorAndFinish() {
        var toast = Toast.makeText(
            applicationContext,
            resources.getString(R.string.error_loading),
            Toast.LENGTH_LONG
        )
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
        delay(3500)
        finish()
    }

    override fun onClick(cellphone: Cellphone, position: Int) {
        if ((!cellphoneDialog.isShown) ) {
            viewModel.getCellphoneByIdFromAPI(cellphone, position)
            cellphoneDialog.isShown = true
        }
    }

    override fun onBackPressed() {
        showQuitDialog()
    }

    private fun showQuitDialog() {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage(R.string.quit_dialog_msg)
            .setCancelable(false)
            .setPositiveButton(
                R.string.quit_dialog_positive,
                DialogInterface.OnClickListener { dialog, id ->
                    finish()
                })
            .setNegativeButton(
                R.string.quit_dialog_negative,
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                })
        val alert = dialogBuilder.create()
        alert.setTitle(R.string.quit_dialog_title)
        alert.show()
    }

}