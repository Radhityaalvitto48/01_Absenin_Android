package com.example.project_pas

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project_pas.databinding.ActivityLandingPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LandingPage : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLandingPageBinding
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pageprofile = findViewById<ImageView>(R.id.imageView8)
        pageprofile.setOnClickListener {
            intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }

        val pagehome = findViewById<ImageView>(R.id.imageView10)
        pagehome.setOnClickListener {
            intent = Intent(this, LandingPage::class.java)
            startActivity(intent)
        }

        val pageabsen = findViewById<ImageView>(R.id.imageView9)
        pageabsen.setOnClickListener {
            intent = Intent(this, MenuAbsensi::class.java)
            startActivity(intent)
        }

        val btnMasuk = findViewById<Button>(R.id.btnmasuk)
        btnMasuk.setOnClickListener {
            intent = Intent(this, LandingPageMasuk::class.java)
            startActivity(intent)
        }

        val btnKeluar = findViewById<Button>(R.id.btnKeluar)
        btnKeluar.setOnClickListener {
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Inisialisasi Firebase Auth
        auth = FirebaseAuth.getInstance()
        // Set OnClickListener untuk tombol
        binding.btnKeluar.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        // Periksa apakah pengguna sudah login
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // Jika belum login, navigasi ke SignInActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // Jika sudah login, perbarui UI dengan informasi pengguna
            updateUI(currentUser)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnKeluar -> {
                // Logout pengguna
                signOut()
            }
        }
    }

    private fun signOut() {
        auth.signOut()
        // Navigasi ke SignInActivity setelah logout
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Fungsi untuk memperbarui UI dengan informasi pengguna
    private fun updateUI(user: FirebaseUser?) {
        user?.let {
//            // Tampilkan nama pengguna
//            val name = it.displayName ?: "No Name"
//            binding.tvName.text = name
            // Tampilkan email pengguna
            val email = it.email ?: "No Email"
            binding.tvName.text = email
            // Sembunyikan tombol verifikasi email jika email sudah terverifikasi
//            if (it.isEmailVerified) {
//                binding.btnEmailVerify.visibility = View.GONE
//            } else {
//                binding.btnEmailVerify.visibility = View.VISIBLE
//            }
        }
    }
}


