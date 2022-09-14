package com.xr6software.cellphonegallery.view

import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import coil.load
import com.xr6software.cellphonegallery.R
import com.xr6software.cellphonegallery.model.CellphoneDetail
import com.xr6software.cellphonegallery.model.Image
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

/**
@author Hernán Carrera
@version 1.0
@param context via Hilt injection.
This is class extends Recycler view. Adapter to parse data from the cellphones list to the listview items
This class inflates an alert dialog with the specific info of the cellphone.
 */

@Module
@InstallIn(ActivityComponent::class)
class CellphoneDialog @Inject constructor(@ActivityContext val context: Context) {

    lateinit var image: ImageView;
    lateinit var imagesList: ArrayList<Image>
    var imageIndex: Int = 0
    var isShown : Boolean = false

    fun showDialog(cellphoneDetail: CellphoneDetail) {

        imagesList = cellphoneDetail.images

        val builder = AlertDialog.Builder(context, androidx.appcompat.R.style.AlertDialog_AppCompat)
            .create()

        var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view: View = inflater.inflate(R.layout.dialog_cellphone, null)
        val button = view.findViewById<Button>(R.id.dialog_button)
        image = view.findViewById<ImageView>(R.id.dialog_imageView)
        val text = view.findViewById<TextView>(R.id.dialog_textView)
        val buttonBack = view.findViewById<ImageButton>(R.id.dialog_button_back)
        val buttonNext = view.findViewById<ImageButton>(R.id.dialog_button_next)
        text.movementMethod = ScrollingMovementMethod()

        text.setText(HtmlCompat.fromHtml(cellphoneDetail.legal, 0))

        loadImagesOnCarousel(imageIndex)

        builder.setView(view)

        buttonBack.setOnClickListener {
            if ((imageIndex - 1) >= 0) {
                loadImagesOnCarousel(--imageIndex)
            }
        }
        buttonNext.setOnClickListener {
            if ((imageIndex + 1) < imagesList.size) {
                loadImagesOnCarousel(++imageIndex)
            }
        }

        button.setOnClickListener {
            builder.dismiss()
            isShown = false
        }

        builder.setCanceledOnTouchOutside(false)
        builder.setCancelable(false)
        builder.show()
        isShown = true
        builder.window?.setLayout(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        builder.window?.setIcon(R.drawable.ic_launcher_foreground)
    }

    private fun loadImagesOnCarousel(index: Int) {

        image.load(imagesList[index].thumbnailUrl) {
            size(150,150)
            crossfade(700)
            error(com.google.android.material.R.drawable.mtrl_ic_error)
        }

    }

}