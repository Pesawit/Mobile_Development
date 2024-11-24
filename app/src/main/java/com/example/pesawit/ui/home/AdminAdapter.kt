package com.example.pesawit.ui.home


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pesawit.model.Article

class AdminAdapter(
    private val articles: List<Article>,
    private val onEditClick: (Article) -> Unit,
    private val onDeleteClick: (Article) -> Unit
) : RecyclerView.Adapter<AdminAdapter.AdminViewHolder>() {

    // ViewHolder for item_article_admin.xml
    inner class AdminViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvStatus: TextView = itemView.findViewById(R.id.tv_status)
        val btnEdit: Button = itemView.findViewById(R.id.btn_edit)
        val btnDelete: Button = itemView.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article_admin, parent, false)
        return AdminViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminViewHolder, position: Int) {
        val article = articles[position]

        // Set data to the views
        holder.tvTitle.text = article.title
        holder.tvStatus.text = if (article.isPublished) "Published" else "Unpublished"

        // Set listeners for buttons
        holder.btnEdit.setOnClickListener { onEditClick(article) }
        holder.btnDelete.setOnClickListener { onDeleteClick(article) }
    }

    override fun getItemCount(): Int = articles.size
}
