package com.example.pesawit.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pesawit.R
import com.example.pesawit.data.response.Article

class AdminAdapter(
    private val articles: List<Article>,
    private val onEditClick: (Article) -> Unit,
    private val onDeleteClick: (Article) -> Unit
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

        // Tombol Edit
        holder.btnEdit.setOnClickListener {
            onEditClick(article) // Memanggil callback edit dengan artikel terkait
        }

        // Tombol Delete
        holder.btnDelete.setOnClickListener {
            onDeleteClick(article) // Memanggil callback delete dengan artikel terkait
        }
    }

    override fun getItemCount(): Int = articles.size
}
