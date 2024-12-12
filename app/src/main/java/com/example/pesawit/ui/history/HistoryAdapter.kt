package com.example.pesawit.ui.history


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pesawit.R
import com.example.pesawit.data.response.DetectionHistory
import com.example.pesawit.databinding.ItemHistoryBinding
import com.example.pesawit.ui.history.HistoryAdapter.*
import com.squareup.picasso.Picasso

class HistoryAdapter : ListAdapter<DetectionHistory, HistoryViewHolder>(HistoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DetectionHistory) {
            binding.tvDate.text = item.createdAt
            binding.tvPrediction.text = item.result
            Picasso.get().load(item.image).placeholder(R.drawable.ic_placeholder).into(binding.ivImage)
        }
    }

    class HistoryDiffCallback : DiffUtil.ItemCallback<DetectionHistory>() {
        override fun areItemsTheSame(oldItem: DetectionHistory, newItem: DetectionHistory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DetectionHistory, newItem: DetectionHistory): Boolean {
            return oldItem == newItem
        }
    }

}
