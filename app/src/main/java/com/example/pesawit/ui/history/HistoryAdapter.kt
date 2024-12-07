package com.example.pesawit.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pesawit.data.response.ResponseItem
import com.example.pesawit.databinding.ItemHistoryBinding
import com.squareup.picasso.Picasso

class HistoryAdapter : ListAdapter<ResponseItem, HistoryAdapter.HistoryViewHolder>(HistoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResponseItem) {
            item.data.let { data ->
                if (data != null) {
                    binding.tvDate.text = data.createdAt
                }
                if (data != null) {
                    binding.tvPrediction.text = data.title
                }
                if (data != null) {
                    Picasso.get().load(data.image).into(binding.ivImage)
                }
            }
        }
    }

    class HistoryDiffCallback : DiffUtil.ItemCallback<ResponseItem>() {
        override fun areItemsTheSame(oldItem: ResponseItem, newItem: ResponseItem): Boolean {
            return oldItem.data?.id == newItem.data?.id
        }

        override fun areContentsTheSame(oldItem: ResponseItem, newItem: ResponseItem): Boolean {
            return oldItem == newItem
        }
    }
}