package mx.org.samtech.samplestore.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import mx.org.samtech.samplestore.R
import mx.org.samtech.samplestore.model.AppsModel
import mx.org.samtech.samplestore.utils.Utils.setGlideImage
import mx.org.samtech.samplestore.utils.Utils.showAlertWithAction

class AppsAdapter(var appsList: MutableList<AppsModel>) :
    RecyclerView.Adapter<AppsAdapter.AppsViewHolder>(), Filterable {

    var filterList: MutableList<AppsModel> = appsList
    var appsFilter = AppsFilter()

    class AppsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appCardView: CardView = view.findViewById(R.id.app_item_cardview)
        val appCustomIcon: ImageView = view.findViewById(R.id.app_item_icon)
        val appName: TextView = view.findViewById(R.id.app_item_name)
        val appRating: AppCompatRatingBar = view.findViewById(R.id.app_item_rating)
        val appPrice: TextView = view.findViewById(R.id.app_item_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app, parent, false)

        return AppsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun onBindViewHolder(holder: AppsViewHolder, position: Int) {
        val app = filterList.get(position)

        holder.appName.text = app.appName
        holder.appPrice.text = "$ ${app.appPrice}"
        holder.appRating.rating = app.appRating.toFloat()

        setGlideImage(holder.itemView.context, app.appUrlImage, holder.appCustomIcon)

        holder.appCardView.setOnClickListener {
            showAlertWithAction(
                it.context,
                app.appUrlScreenShoot,
                app.appUrlImage,
                app.appName,
                app.appDescription,
                app.appPrice,
                app.opinions
            )
        }
    }

    override fun getFilter(): Filter {
        return appsFilter
    }

    inner class AppsFilter : Filter() {
        override fun performFiltering(charSequence: CharSequence?): FilterResults {
            val filterString = charSequence.toString()
            val results = FilterResults()

            val filterList: ArrayList<AppsModel> = ArrayList()

            if (filterString.trim { it <= ' ' }.isEmpty()) {
                results.values = appsList
                results.count = appsList.size
                return results
            } else if (filterString.trim { it <= ' ' }.length <= 20) {
                for (search in appsList) {
                    if (search.appName.lowercase().contains(filterString.lowercase())) {
                        filterList.add(search)
                    }
                }
            }
            results.values = filterList
            results.count = filterList.size
            return results
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(charSequence: CharSequence?, results: FilterResults?) {
            filterList.clear()
            filterList.addAll(results?.values as ArrayList<AppsModel>)
            notifyDataSetChanged()
        }
    }
}