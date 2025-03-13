package com.example.project_pas

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuAbsensi : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_absensi)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val pageprofile = findViewById<ImageView>(R.id.imageView8)
        pageprofile.setOnClickListener{
            intent = Intent(this,Profile::class.java)
            startActivity(intent)
        }
        val pagehome = findViewById<ImageView>(R.id.imageView10)
        pagehome.setOnClickListener{
            intent = Intent(this,LandingPage::class.java)
            startActivity(intent)
        }

    }
}