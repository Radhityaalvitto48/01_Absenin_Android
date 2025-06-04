package com.example.project_pas

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class IsiKelas : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_isi_kelas)

        val db = FirebaseFirestore.getInstance()
        val editTextNamaKelas = findViewById<EditText>(R.id.editTextNamaKelas)
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
            val namaKelas = editTextNamaKelas.text.toString().trim()

            if (namaKelas.isNotEmpty()) {
                val kelas = hashMapOf(
                    "nama_kelas" to namaKelas,
                    "timestamp" to FieldValue.serverTimestamp()
                )

                db.collection("kelas")
                    .add(kelas)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Kelas disimpan!", Toast.LENGTH_SHORT).show()
                        finish() // Kembali ke halaman TambahKelas
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Gagal menyimpan kelas", Toast.LENGTH_SHORT).show()
                    }
            } else {
                editTextNamaKelas.error = "Nama kelas tidak boleh kosong"
            }
        }
        val back = findViewById<ImageView>(R.id.iv_Back)
        back.setOnClickListener {
            // Intent kembali ke LandingPageMasuk
            val intent = Intent(this, LandingPageMasuk::class.java)
            startActivity(intent)
        }
    }
}