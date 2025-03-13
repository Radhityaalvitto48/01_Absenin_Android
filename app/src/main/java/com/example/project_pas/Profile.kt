package com.example.project_pas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val pagehome = findViewById<ImageView>(R.id.imageView10)
        pagehome.setOnClickListener{
            intent = Intent(this,LandingPage::class.java)
            startActivity(intent)
        }
        val pageabsen = findViewById<ImageView>(R.id.imageView9)
        pageabsen.setOnClickListener{
            intent = Intent(this,MenuAbsensi::class.java)
            startActivity(intent)
        }

        val btnKeluar = findViewById<ImageView>(R.id.logout)
        btnKeluar.setOnClickListener{
            intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}