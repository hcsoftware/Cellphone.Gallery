package com.xr6software.cellphonegallery.ui.main.activities

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xr6software.cellphonegallery.R
import com.xr6software.cellphonegallery.databinding.ActivityMainBinding
import com.xr6software.cellphonegallery.model.Cellphone
import com.xr6software.cellphonegallery.ui.main.adapters.AdapterCellphone
import com.xr6software.cellphonegallery.ui.main.adapters.AdapterCellphoneClickListener
import com.xr6software.cellphonegallery.ui.main.dialogs.CellphoneDialog
import com.xr6software.cellphonegallery.viewmodels.main.MainActivityViewModel
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
    private val viewModel: MainActivityViewModel by viewModels()

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

        viewModel.loadCellphones()
        setObservers()
    }

    private fun setObservers() {

        viewModel.getUiFragmentState().observe(this) {

            when (it) {
                MainActivityViewModel.UiFragmentState.Loading -> {
                    viewBinding.maProgressbarHolder.visibility = View.VISIBLE
                }
                is MainActivityViewModel.UiFragmentState.Error -> {
                    viewBinding.maProgressbarHolder.visibility = View.INVISIBLE
                    CoroutineScope(Main).launch {
                        showErrorAndFinish()
                    }
                }
                is MainActivityViewModel.UiFragmentState.Cellphones -> {
                    viewBinding.maProgressbarHolder.visibility = View.INVISIBLE
                    cellphoneAdapter.setData(it.cellphoneList as ArrayList<Cellphone>)
                }
                is MainActivityViewModel.UiFragmentState.CellphoneInfo -> {
                    viewBinding.maProgressbarHolder.visibility = View.INVISIBLE
                    cellphoneDialog.showDialog(it.cellphone)
                }
            }

        }

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
            viewModel.loadCellphoneInfo(position)
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