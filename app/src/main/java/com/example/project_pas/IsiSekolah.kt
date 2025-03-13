package com.example.project_pas

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

class IsiSekolah : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_isi_sekolah)

        val buttonSelesai = findViewById<View>(R.id.buttonSelesai)

        // Pantau visibilitas keyboard dan geser tombol jika keyboard muncul
        ViewCompat.setOnApplyWindowInsetsListener(buttonSelesai) { view, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom

            if (imeVisible) {
                view.updatePadding(bottom = imeHeight + 16) // Naikkan tombol
            } else {
                view.updatePadding(bottom = 16) // Kembalikan posisi
            }

            insets
        }

        // Tambahkan logika untuk tombol "Selesai" di sini
        buttonSelesai.setOnClickListener {
            intent = Intent (this, TambahSekolah::class.java)
            startActivity(intent)
        }
        val back = findViewById<ImageView>(R.id.iv_Back)
        back.setOnClickListener {
            // Intent kembali ke LandingPageMasuk
            val intent = Intent(this, LandingPageMasuk::class.java)
            startActivity(intent)
        }
    }
}