package com.example.project_pas

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class LandingPageMasuk : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_landing_page_masuk)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.masuk)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val pindahFormKelas = findViewById<TextView>(R.id.textKelas)
        pindahFormKelas.setOnClickListener {
            val intent = Intent(this, TambahKelas::class.java)
            startActivity(intent)
        }
        val back = findViewById<ImageView>(R.id.iv_Back)
        back.setOnClickListener {
            // Intent kembali ke LandingPageMasuk
            val intent = Intent(this, LandingPage::class.java)
            startActivity(intent)
        }
        val pindahFormSekolah = findViewById<TextView>(R.id.tv_sekolah)
        pindahFormSekolah.setOnClickListener {
            val intent = Intent(this, TambahSekolah::class.java)
            startActivity(intent)
        }

        val pageprofile = findViewById<ImageView>(R.id.imageView8)
        pageprofile.setOnClickListener{
            intent = Intent(this,Profile::class.java)
            startActivity(intent)
        }

        val pageabsen = findViewById<ImageView>(R.id.imageView9)
        pageabsen.setOnClickListener{
            intent = Intent(this,MenuAbsensi::class.java)
            startActivity(intent)
        }
        val pagehome = findViewById<ImageView>(R.id.imageView10)
        pagehome.setOnClickListener{
            intent = Intent(this,LandingPage::class.java)
            startActivity(intent)
        }
    }
}