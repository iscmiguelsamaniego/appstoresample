package mx.org.samtech.samplestore.utils

import android.app.Dialog
import android.content.Context
import android.os.SystemClock
import android.view.Gravity
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mx.org.samtech.samplestore.R
import mx.org.samtech.samplestore.adapters.AppsAdapter
import mx.org.samtech.samplestore.adapters.OpinionsAdapter
import mx.org.samtech.samplestore.retrofit.Opinions

object Utils {

    private var mLastClickTime: Long = 0

    fun showAlertWithAction(
        paramContext: Context,
        paramScreenShootUrl: String,
        paramIconUrl: String,
        paramName: String?,
        paramDescription: String?,
        paramPrice: Double?,
        opinionsList: ArrayList<Opinions>
    ) {
        val customDialog = Dialog(paramContext)
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customDialog.setCancelable(false)
        customDialog.setContentView(R.layout.custom_dialog)
        val screenShoot =
            customDialog.findViewById(R.id.custom_dialog_app_screen_shoot) as ImageView
        val icon = customDialog.findViewById(R.id.custom_dialog_app_icon) as ImageView
        val title = customDialog.findViewById(R.id.custom_dialog_app_name) as TextView
        val detail = customDialog.findViewById(R.id.custom_dialog_app_detail) as TextView
        val price = customDialog.findViewById(R.id.custom_dialog_app_price) as TextView
        val recyclerViewOpinions = customDialog.findViewById(R.id.custom_dialog_opinions) as RecyclerView
        val btnClose = customDialog.findViewById(R.id.custom_dialog_close) as Button
        val btnInstall = customDialog.findViewById(R.id.custom_dialog_install) as Button

        setGlideImage(paramContext, paramScreenShootUrl, screenShoot)
        setGlideImage(paramContext, paramIconUrl, icon)

        title.text = paramName
        detail.text = paramDescription
        price.text = "$ $paramPrice"

        //TODO Recycler view STARS
        recyclerViewOpinions.layoutManager =
            LinearLayoutManager(paramContext, LinearLayoutManager.VERTICAL ,false)
        var opinionsAdapter = OpinionsAdapter(opinionsList)
        recyclerViewOpinions.adapter = opinionsAdapter
        //opinions
        //TODO Recycler view ENDS

        btnClose.setOnClickListener {
            customDialog.dismiss()
        }

        btnInstall.setOnClickListener {
            customToast(paramContext, paramContext.getString(R.string.installing))
        }


        customDialog.setCancelable(false)
        customDialog.show()
    }

    fun setGlideImage(paramContext: Context, paramUrl: String, paramImageView: ImageView) {
        Glide
            .with(paramContext)
            .load(paramUrl)
            .placeholder(R.drawable.ic_no_image)
            .into(paramImageView)
    }

    fun mayTapButton(time: Long): Boolean {
        return if (SystemClock.elapsedRealtime() - mLastClickTime < time) {
            false
        } else {
            mLastClickTime = SystemClock.elapsedRealtime()
            true
        }
    }

    fun customToast(ctx: Context?, message: String?) {
        if (ctx != null) {
            val toast = Toast.makeText(ctx, message, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }
}