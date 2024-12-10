package com.example.pesawit.ui.home.artikel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pesawit.R
import com.example.pesawit.data.response.ArticlesItem
import com.example.pesawit.ui.home.HomeFragment


class EditArticleFragment : Fragment() {

    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var switchPublish: Switch
    private lateinit var btnSave: Button

    private var article: ArticlesItem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_edit_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etTitle = view.findViewById(R.id.et_title)
        etContent = view.findViewById(R.id.et_content)
        switchPublish = view.findViewById(R.id.switch_publish)
        btnSave = view.findViewById(R.id.btn_save)

        article = arguments?.getParcelable("article")

        article?.let {
            etTitle.setText(it.title)
            etContent.setText(it.content)
            switchPublish.isChecked = it.isPublished == true
        }

        btnSave.setOnClickListener { onSaveArticle() }
    }

    private fun onSaveArticle() {
        val updatedTitle = etTitle.text.toString().trim()
        val updatedContent = etContent.text.toString().trim()
        val isPublished = switchPublish.isChecked

        if (updatedTitle.isEmpty() || updatedContent.isEmpty()) {
            Toast.makeText(requireContext(), "Title and content cannot be empty!", Toast.LENGTH_SHORT).show()
            return
        }

        article?.let {
            it.title = updatedTitle
            it.content = updatedContent
            it.isPublished = isPublished

            // Call the ViewModel to update the article
            it.id?.let { it1 -> (activity as HomeFragment).viewModel.editArticle(it1, it) }

            Toast.makeText(requireContext(), "Article updated successfully!", Toast.LENGTH_SHORT).show()

            parentFragmentManager.popBackStack()
        }
    }
}
