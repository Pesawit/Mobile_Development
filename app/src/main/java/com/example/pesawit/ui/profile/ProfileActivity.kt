import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pesawit.R
import com.example.pesawit.data.response.ResponseItem

class ProfileActivity : AppCompatActivity() {

    private lateinit var ivProfilePicture: ImageView
    private lateinit var tvUsername: TextView
    private lateinit var btnEditProfile: Button
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        ivProfilePicture = findViewById(R.id.iv_profile_picture)
        tvUsername = findViewById(R.id.tv_username)
        btnEditProfile = findViewById(R.id.btn_edit_profile)
        btnLogout = findViewById(R.id.btn_logout)

        // Mendapatkan data ResponseItem (misalnya dari intent atau ViewModel)
        val user = getUserData()  // Ambil data user, bisa dari API atau database

        // Setel gambar profil menggunakan Glide
        Glide.with(this)
            .load(user.image)  // Pastikan image URL valid atau gunakan gambar default jika null
            .into(ivProfilePicture)

        // Setel nama pengguna
        tvUsername.text = user.name ?: "Nama tidak tersedia"

        // Tombol Edit Profil
        btnEditProfile.setOnClickListener {
            // Tindakan ketika tombol Edit Profile diklik
        }

        // Tombol Logout
        btnLogout.setOnClickListener {
            // Tindakan ketika tombol Logout diklik
        }
    }

    private fun getUserData(): ResponseItem {
        // Contoh data ResponseItem, ganti dengan pengambilan data dari sumber yang sesuai
        return ResponseItem(
            image = "https://example.com/profile.jpg", // Ganti dengan URL gambar nyata
            name = "John Doe"
        )
    }
}
