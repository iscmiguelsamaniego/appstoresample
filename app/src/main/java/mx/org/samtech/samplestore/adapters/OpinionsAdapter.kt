package mx.org.samtech.samplestore.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mx.org.samtech.samplestore.R
import mx.org.samtech.samplestore.retrofit.Opinions

class OpinionsAdapter(var opinionsList: ArrayList<Opinions>) :
    RecyclerView.Adapter<OpinionsAdapter.OpinionsViewHolder>() {

    class OpinionsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userTView: TextView = view.findViewById(R.id.item_op_user)
        val commentTView: TextView = view.findViewById(R.id.item_op_user_comment)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OpinionsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_opinion, parent, false)

        return OpinionsViewHolder(view)
    }

    override fun onBindViewHolder(holder: OpinionsViewHolder, position: Int) {
        val app = opinionsList.get(position)
        holder.userTView.text = app.username
        holder.commentTView.text = app.opinion
    }

    override fun getItemCount(): Int {
        return opinionsList.size
    }

}