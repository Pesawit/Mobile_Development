package com.example.pesawit.ui.home.artikel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.pesawit.R
import com.example.pesawit.data.response.ArticlesItem

class ArticleDetailFragment : Fragment() {

    private lateinit var tvTitle: TextView
    private lateinit var tvAuthor: TextView
    private lateinit var tvContent: TextView
    private lateinit var tvTags: TextView
    private lateinit var tvDate: TextView

    private var article: ArticlesItem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_article_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTitle = view.findViewById(R.id.tv_title)
        tvAuthor = view.findViewById(R.id.tv_author)
        tvContent = view.findViewById(R.id.tv_content)
        tvTags = view.findViewById(R.id.tv_tags)
        tvDate = view.findViewById(R.id.tv_date)

        // Ambil artikel yang dikirimkan dari argumen
        article = arguments?.getParcelable("article")

        article?.let {
            tvTitle.text = it.title
            tvAuthor.text = "Author: ${it.author}"
            tvContent.text = it.content
            tvTags.text = "Tags: ${it.tags.joinToString(", ")}"
            tvDate.text = "Published on: ${it.createdAt}"
        }
    }
}
