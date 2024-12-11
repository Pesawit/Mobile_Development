package com.example.pesawit.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pesawit.R
import com.example.pesawit.data.response.ArticlesItem

class UserAdapter(
    private val articles: List<ArticlesItem>,
    private val onReadMoreClick: (ArticlesItem) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val btnReadMore: Button = itemView.findViewById(R.id.btn_read_more)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val article = articles[position]
        holder.tvTitle.text = article.title ?: "No Title"
        holder.btnReadMore.setOnClickListener { onReadMoreClick(article) }
    }

    override fun getItemCount(): Int = articles.size
}
