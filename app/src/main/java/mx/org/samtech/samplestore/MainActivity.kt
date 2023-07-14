package mx.org.samtech.samplestore

import android.annotation.SuppressLint
import android.os.Bundle
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
import mx.org.samtech.samplestore.utils.Utils.isOnline
import mx.org.samtech.samplestore.utils.Utils.mayTapButton
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@SuppressLint("NotifyDataSetChanged")
class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private var mAdapter: AppsAdapter? = null
    private var appsList: ArrayList<AppsModel> = ArrayList()
    lateinit var recyclerView: RecyclerView
    lateinit var searchView: SearchView
    lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.act_main)

        swipeRefresh = findViewById(R.id.act_sample_store_swipe_refresh)
        searchView = findViewById(R.id.act_sample_store_search_view)
        recyclerView = findViewById(R.id.act_sample_store_recycler_view)
        recyclerView.layoutManager = GridLayoutManager(applicationContext, 3)
        swipeRefresh.setOnRefreshListener(this)

        var oldSizeList = 0
        callService(false)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    if (mAdapter != null) {
                        mAdapter!!.filter.filter(query)
                    }
                }
                if (mAdapter != null) {
                    mAdapter!!.notifyDataSetChanged()
                }
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText != null) {
                    if (oldSizeList == 0) {
                        oldSizeList = appsList.size
                    }

                    if (mAdapter != null) {
                        mAdapter!!.filter.filter(newText)
                    }
                }

                if (mAdapter != null) {
                    mAdapter!!.notifyDataSetChanged()
                }

                if (oldSizeList != appsList.size) {
                    callService(true)
                }

                return false
            }
        })
    }

    fun setUpRecyclerView() {
        if (appsList.size != 0) {
            mAdapter = AppsAdapter(appsList)
            recyclerView.adapter = mAdapter
        }
    }

    fun callService(isResultsPosted: Boolean) {
        if (isOnline(this)) {
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
                            appsList.clear()
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

                                if (appsList.size == it.apps.size) {
                                    if (!isResultsPosted) {
                                        setUpRecyclerView()
                                    }
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
            }
            swipeRefresh.isRefreshing = false
        } else {
            customToast(this, getString(R.string.no_internet))
        }
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
            callService(false)
        } else {
            customToast(this, getString(R.string.msg_refresh))
        }
        swipeRefresh.isRefreshing = false
    }

}