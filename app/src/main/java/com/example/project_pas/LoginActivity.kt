package com.example.project_pas

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project_pas.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    // Deklarasi variabel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Inisialisasi Firebase Auth
        auth = FirebaseAuth.getInstance()
        // Set OnClickListener untuk tombol
        binding.btnLogin.setOnClickListener(this)
        binding.btnGoogle.setOnClickListener(this)
        binding.tvDaftar.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnLogin-> {
                // Login dengan Email/Password
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                Login(email, password)
            }
            R.id.tvDaftar -> {
                // Navigasi ke halaman Sign Up
                val intent = Intent(this, DaftarActivity::class.java)
                startActivity(intent)
            }
            R.id.btnGoogle -> {
                // Login dengan Google
                val intent = Intent(this, LandingPage::class.java)
                startActivity(intent)
            }
        }
    }

    private fun Login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                this, "Email and Password cannot be empty",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login berhasil, navigasi ke MainActivity
                    val intent = Intent(this, LandingPage::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Login gagal, tampilkan pesan error
                    Toast.makeText(
                        this, "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
