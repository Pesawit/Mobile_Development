package com.example.pesawit.ui.home

import com.example.pesawit.R
import com.example.pesawit.model.Article
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class UserAdapter(
    private val articles: List<Article> // List Artikel
) : RecyclerView.Adapter<UserAdapter.ArticleViewHolder>() {

    // ViewHolder untuk mengikat data ke item layout
    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_title)
        val date: TextView = itemView.findViewById(R.id.tv_date)
        val description: TextView = itemView.findViewById(R.id.tv_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article_user, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.title.text = article.title
        holder.date.text = article.date
        holder.description.text = article.description
    }

    override fun getItemCount(): Int = articles.size
}
