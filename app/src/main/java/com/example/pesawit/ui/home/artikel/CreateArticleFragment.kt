package com.example.pesawit.ui.home.artikel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pesawit.R
import com.example.pesawit.data.response.Article
import com.example.pesawit.viewmodel.HomeViewModel

class CreateArticleFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_create_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etTitle = view.findViewById<EditText>(R.id.et_title)
        val etContent = view.findViewById<EditText>(R.id.et_content)
        val btnUploadImage = view.findViewById<Button>(R.id.btn_upload_image)
        val btnPublish = view.findViewById<Button>(R.id.btn_publish)

        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        btnUploadImage.setOnClickListener {
            // Logika upload gambar
        }

        btnPublish.setOnClickListener {
            val title = etTitle.text.toString()
            val content = etContent.text.toString()
            if (title.isNotEmpty() && content.isNotEmpty()) {
                val article = Article(
                    title = title,
                    content = content,
                    isPublished = true,
                    createdAt = System.currentTimeMillis().toString()
                )
                viewModel.createArticle(article)
                // Kembali ke HomeFragment
                parentFragmentManager.popBackStack()
            }
        }
    }
}
