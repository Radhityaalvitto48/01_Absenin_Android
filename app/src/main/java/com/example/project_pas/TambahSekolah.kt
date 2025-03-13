package com.example.project_pas

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TambahSekolah : AppCompatActivity() {

    private var isFabOpen = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_sekolah)

        val fabMain: FloatingActionButton = findViewById(R.id.fab_main)
        val fabTambahKelas: FloatingActionButton = findViewById(R.id.fab_tambah_kelas)

        fabMain.setOnClickListener {
            if (isFabOpen) {
                fabTambahKelas.visibility = View.GONE
                isFabOpen = false
            } else {
                fabTambahKelas.visibility = View.VISIBLE
                isFabOpen = true
            }
        }
        // Deklarasi tombol back di dalam onCreate
        val back = findViewById<ImageView>(R.id.iv_Back)
        back.setOnClickListener {
            // Intent kembali ke LandingPageMasuk
            val intent = Intent(this, LandingPageMasuk::class.java)
            startActivity(intent)
        }

        fabTambahKelas.setOnClickListener {
            // Tambahkan logika untuk membuka halaman Tambah Kelas di sini
            intent = Intent(this, IsiSekolah::class.java)
            startActivity(intent)
            if (isFabOpen) {
                hideFab(fabTambahKelas)
                isFabOpen = false
            } else {
                showFab(fabTambahKelas)
                isFabOpen = true
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

    private fun showFab(fab: FloatingActionButton) {
        fab.visibility = View.VISIBLE
        fab.animate().alpha(1f).setDuration(300).start()
    }

    private fun hideFab(fab: FloatingActionButton) {
        fab.animate().alpha(0f).setDuration(200).withEndAction {
            fab.visibility = View.GONE
        }.start()
    }
}