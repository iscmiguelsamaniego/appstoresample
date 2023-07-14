package mx.org.samtech.samplestore.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.SystemClock
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mx.org.samtech.samplestore.R
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
        price.text = buildString {
        append("$ ")
        append(paramPrice)
    }

        recyclerViewOpinions.layoutManager =
            LinearLayoutManager(paramContext, LinearLayoutManager.VERTICAL ,false)
        val opinionsAdapter = OpinionsAdapter(opinionsList)
        recyclerViewOpinions.adapter = opinionsAdapter

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

    fun customToast(ctx: Context, message: String?) {
        val toast = Toast.makeText(ctx, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    @Suppress("DEPRECATION")
    fun isOnline(context: Context?): Boolean {

        var result = false
        if (context != null) {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            result = true
                        }

                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            result = true
                        }

                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                            result = true
                        }

                    }
                }
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    if (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    }

                    if (activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    }
                }
            }
        }

        return result
    }
}