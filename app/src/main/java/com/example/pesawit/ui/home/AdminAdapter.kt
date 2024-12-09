package com.example.pesawit.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pesawit.R
import com.example.pesawit.data.response.ArticlesItem

class AdminAdapter(
    private val articles: List<ArticlesItem>,
    private val onEditClick: (ArticlesItem) -> Unit,
    private val onDeleteClick: (ArticlesItem) -> Unit,
    private val onItemClick: (ArticlesItem) -> Unit // Menambahkan listener klik item
) : RecyclerView.Adapter<AdminAdapter.AdminViewHolder>() {

    inner class AdminViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvStatus: TextView = itemView.findViewById(R.id.tv_status)
        val btnEdit: Button = itemView.findViewById(R.id.btn_edit)
        val btnDelete: Button = itemView.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article_admin, parent, false)
        return AdminViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminViewHolder, position: Int) {
        val article = articles[position]
        holder.tvTitle.text = article.title ?: "No Title"
        holder.tvStatus.text = if (article.isPublished == true) "Published" else "Unpublished"

        holder.btnEdit.setOnClickListener { onEditClick(article) }
        holder.btnDelete.setOnClickListener { onDeleteClick(article) }

        // Menambahkan listener untuk membuka detail artikel saat item diklik
        holder.itemView.setOnClickListener { onItemClick(article) }
    }

    override fun getItemCount(): Int = articles.size
}
