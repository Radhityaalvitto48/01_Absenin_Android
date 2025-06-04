package com.example.project_pas

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class IsiSekolah : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_isi_sekolah)

        val db = FirebaseFirestore.getInstance()
            val inputNama = findViewById<EditText>(R.id.editTextNamaSekolah)
        val buttonSelesai = findViewById<ImageButton>(R.id.buttonSelesai1)

        ViewCompat.setOnApplyWindowInsetsListener(buttonSelesai) { view, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom

            if (imeVisible) {
                view.updatePadding(bottom = imeHeight + 16)
            } else {
                view.updatePadding(bottom = 16)
            }
            insets
        }

        buttonSelesai.setOnClickListener {
            val namaSekolah = inputNama.text.toString().trim()

            if (namaSekolah.isNotEmpty()) {
                val sekolah = hashMapOf(
                    "nama_sekolah" to namaSekolah,
                    "timestamp" to FieldValue.serverTimestamp()
                )

                db.collection("sekolah")
                    .add(sekolah)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Sekolah disimpan!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Gagal menyimpan sekolah", Toast.LENGTH_SHORT).show()
                    }
            } else {
                inputNama.error = "Nama sekolah tidak boleh kosong"
            }
        }

        findViewById<ImageView>(R.id.iv_Back).setOnClickListener {
            startActivity(Intent(this, LandingPageMasuk::class.java))
        }
    }
}
