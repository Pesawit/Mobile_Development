package com.example.pesawit.ui.home.artikel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.pesawit.R

class ArticleDetailFragment : Fragment() {

    private lateinit var tvTitle: TextView
    private lateinit var tvAuthor: TextView
    private lateinit var tvContent: TextView
    private lateinit var tvTags: TextView
    private lateinit var tvDate: TextView

    private val args: ArticleDetailFragmentArgs by navArgs()

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

        val article = args.article

        article?.let {
            tvTitle.text = it.title
            tvAuthor.text = getString(R.string.author_label, it.author ?: "Unknown Author")
            tvContent.text = it.content
            tvTags.text = getString(R.string.tags_label, it.tags?.joinToString(", ") ?: "No Tags")
            tvDate.text = getString(R.string.published_date_label, it.createdAt ?: "Unknown Date")
        }
    }
}
