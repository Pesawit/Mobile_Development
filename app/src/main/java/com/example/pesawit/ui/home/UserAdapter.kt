package com.example.pesawit.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pesawit.R
import com.example.pesawit.data.response.Article

class UserAdapter(
    private val articles: List<Article> // List of Article objects
) : RecyclerView.Adapter<UserAdapter.ArticleViewHolder>() {

    // ViewHolder to bind data to the item layout
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

        // Bind the data from the Article object to the corresponding views
        holder.title.text = article.title // Set the title of the article
        holder.date.text = article.createdAt // Set the createdAt date as the article's date
        holder.description.text = article.content // Set the article content as the description
    }

    override fun getItemCount(): Int = articles.size
}
