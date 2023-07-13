package mx.org.samtech.samplestore

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mx.org.samtech.samplestore.adapters.AppsAdapter
import mx.org.samtech.samplestore.model.AppsModel
import mx.org.samtech.samplestore.retrofit.Apps
import mx.org.samtech.samplestore.retrofit.AppsApi
import mx.org.samtech.samplestore.retrofit.AppsResponse
import mx.org.samtech.samplestore.utils.Constants.BASE_URL
import mx.org.samtech.samplestore.utils.Constants.INTERVAL_TIME_FOR_REFRESH
import mx.org.samtech.samplestore.utils.Utils.customToast
import mx.org.samtech.samplestore.utils.Utils.mayTapButton
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@SuppressLint("NotifyDataSetChanged")
class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private var mAdapter: AppsAdapter? = null;
    private var appsList: MutableList<AppsModel> = ArrayList()
    lateinit var recyclerView: RecyclerView
    lateinit var searchView: android.widget.SearchView
    lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.act_main)

        swipeRefresh = findViewById(R.id.act_sample_store_swipe_refresh)
        searchView = findViewById(R.id.act_sample_store_search_view)
        recyclerView = findViewById(R.id.act_sample_store_recycler_view)

        swipeRefresh.setOnRefreshListener(this)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        mAdapter = AppsAdapter(appsList)
        recyclerView.adapter = mAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    mAdapter!!.filter.filter(query)
                }
                mAdapter!!.notifyDataSetChanged()
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    mAdapter!!.filter.filter(newText)
                }
                mAdapter!!.notifyDataSetChanged()
                return false
            }
        })

        callService()
    }

    fun callService() {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(AppsApi::class.java)

        swipeRefresh.isRefreshing = true

        var errorMsg = ""

        CoroutineScope(Dispatchers.IO).launch {
            val response = retrofit.getApps()
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        response.body()?.let { mapperAppsResponse(it) }?.let {
                            for (e in it.apps) {

                                appsList.add(
                                    AppsModel(
                                        it.iconsurl + e.urlimage,
                                        it.screenshotsurl + e.urlscreenshot,
                                        e.name!!,
                                        e.description,
                                        e.price,
                                        e.rating!!,
                                        e.opinions
                                    )
                                )
                            }
                        }
                    } else {
                        errorMsg = response.code().toString()
                    }
                } catch (e: HttpException) {
                    errorMsg = e.message()
                } catch (e: Throwable) {
                    errorMsg = getString(R.string.error_admin)
                }
            }
        }

        if (errorMsg.isNotBlank()) {
            customToast(this, errorMsg)
        } else {
            mAdapter!!.notifyDataSetChanged()
        }
        swipeRefresh.isRefreshing = false
    }

    fun mapperAppsResponse(response: AppsResponse): AppsResponse {
        val appsListResponse = ArrayList<Apps>()

        for (e in response.apps) {
            appsListResponse.add(e)
        }

        return AppsResponse(
            iconsurl = response.iconsurl,
            screenshotsurl = response.screenshotsurl,
            apps = appsListResponse
        )
    }

    override fun onRefresh() {
        if (mayTapButton(INTERVAL_TIME_FOR_REFRESH)) {
            callService()
        } else {
            customToast(this, getString(R.string.msg_refresh))
        }
        swipeRefresh.isRefreshing = false
    }
}