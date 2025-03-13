package com.example.project_pas


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TambahKelas : AppCompatActivity() {

    private var isFabOpen = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_kelas)

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

        val back = findViewById<ImageView>(R.id.iv_Back)
        back.setOnClickListener {
            val intent = Intent(this, LandingPageMasuk::class.java)
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
        val pageprofile = findViewById<ImageView>(R.id.imageView8)
        pageprofile.setOnClickListener{
            intent = Intent(this,Profile::class.java)
            startActivity(intent)
        }

        fabTambahKelas.setOnClickListener {
            // Tambahkan logika untuk membuka halaman Tambah Kelas di sini
            intent = Intent(this, IsiKelas::class.java)
            startActivity(intent)
            if (isFabOpen) {
                hideFab(fabTambahKelas)
                isFabOpen = false
            } else {
                showFab(fabTambahKelas)
                isFabOpen = true
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