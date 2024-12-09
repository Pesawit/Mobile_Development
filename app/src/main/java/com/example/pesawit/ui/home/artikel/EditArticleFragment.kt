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
import com.example.pesawit.data.response.Article

class EditArticleFragment : Fragment() {

    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var switchPublish: Switch
    private lateinit var btnSave: Button

    private var article: Article? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi View
        etTitle = view.findViewById(R.id.et_title)
        etContent = view.findViewById(R.id.et_content)
        switchPublish = view.findViewById(R.id.switch_publish)
        btnSave = view.findViewById(R.id.btn_save)

        // Ambil data artikel dari arguments
        article = arguments?.getParcelable("article")

        // Isi data artikel ke dalam form
        article?.let {
            etTitle.setText(it.title)
            etContent.setText(it.content)
            switchPublish.isChecked = it.isPublished == true
        }

        // Tombol Simpan
        btnSave.setOnClickListener {
            onSaveArticle()
        }
    }

    private fun onSaveArticle() {
        val updatedTitle = etTitle.text.toString().trim()
        val updatedContent = etContent.text.toString().trim()
        val isPublished = switchPublish.isChecked

        // Validasi input
        if (updatedTitle.isEmpty() || updatedContent.isEmpty()) {
            Toast.makeText(requireContext(), "Title and content cannot be empty!", Toast.LENGTH_SHORT).show()
            return
        }

        // Simpan perubahan (Anda dapat memanggil API atau database untuk update artikel)
        article?.let {
            it.title = updatedTitle
            it.content = updatedContent
            it.isPublished = isPublished
            Toast.makeText(requireContext(), "Article updated successfully!", Toast.LENGTH_SHORT).show()

            // Kembali ke fragment sebelumnya
            parentFragmentManager.popBackStack()
        }
    }
}
