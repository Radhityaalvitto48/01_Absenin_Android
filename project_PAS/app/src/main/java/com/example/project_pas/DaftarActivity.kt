package com.example.project_pas

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project_pas.databinding.ActivityDaftarBinding
import com.google.firebase.auth.FirebaseAuth

class DaftarActivity : AppCompatActivity(), View.OnClickListener {

    // Deklarasi variabel
    private lateinit var binding: ActivityDaftarBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.daftar)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi Firebase Auth
        auth = FirebaseAuth.getInstance()
        // Set OnClickListener untuk tombol
        binding.btnDaftar.setOnClickListener(this)
        binding.txtLogin.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnDaftar-> {
                // Ambil email dan password dari input
                val email = binding.edEmail.text.toString()
                val password = binding.edPassword.text.toString()
                createAccount(email, password)
            }
            R.id.txt_Login -> {
                // Navigasi ke SignInActivity
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
    private fun createAccount(email: String, password: String) {
        if (!validateForm(email, password)) {
            return
        }
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Akun berhasil dibuat, navigasi ke SignInActivity
                Toast.makeText(this, "Create User Success.", Toast.LENGTH_SHORT).show()
                finish() // Tutup SignUpActivity
            } else {
                // Gagal membuat akun, tampilkan pesan error
                Toast.makeText(
                    this, "Authentication failed: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        // Fungsi untuk memvalidasi form
    }
    private fun validateForm(email: String, password: String): Boolean {
        var valid = true
        if (TextUtils.isEmpty(email)) {
            binding.edEmail.error = "Required."
            valid = false
        } else {
            binding.edEmail.error = null
        }
        if (TextUtils.isEmpty(password)) {
            binding.edPassword.error = "Required."
            valid = false
        } else {
            binding.edPassword.error = null
        }
        return valid
    }
}

