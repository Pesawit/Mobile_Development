package com.example.pesawit.ui.history


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pesawit.R
import com.example.pesawit.data.response.History
import com.example.pesawit.ui.history.HistoryAdapter.*
import com.squareup.picasso.Picasso

class HistoryAdapter : ListAdapter<History, HistoryViewHolder>(HistoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        private val tvPrediction: TextView = itemView.findViewById(R.id.tv_prediction)
        private val tvRecommendation: TextView = itemView.findViewById(R.id.tv_recommendation)
        private val ivImage: ImageView = itemView.findViewById(R.id.iv_image)

        fun bind(item: History) {
            tvDate.text = item.createdAt
            tvPrediction.text = item.result
            tvRecommendation.text = item.recommendation

            val imageUrl = item.image
            Log.d("HistoryAdapter", "Attempting to load image: $imageUrl")

            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error_placeholder)
                .into(ivImage, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        Log.d("HistoryAdapter", "Image loaded successfully: $imageUrl")
                    }

                    override fun onError(e: Exception?) {
                        Log.e("HistoryAdapter", "Error loading image: $imageUrl", e)
                        e?.printStackTrace()
                    }
                })
        }
    }

    class HistoryDiffCallback : DiffUtil.ItemCallback<History>() {
        override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
            return oldItem == newItem
        }
    }

}
