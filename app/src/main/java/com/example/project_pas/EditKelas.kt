package com.example.project_pas

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class EditKelas : AppCompatActivity() {

    private lateinit var etNamaKelas: EditText
    private lateinit var btnSelesai: ImageButton
    private lateinit var ivBack: ImageView
    private lateinit var db: FirebaseFirestore

    private var kelasId: String? = null
    private var namaKelas: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_kelas) // Pastikan nama file layout kamu ini benar

        // Init Firestore
        db = FirebaseFirestore.getInstance()

        // Ambil data dari intent
        kelasId = intent.getStringExtra("KELAS_ID")
        namaKelas = intent.getStringExtra("NAMA_KELAS")

        // Bind views
        etNamaKelas = findViewById(R.id.editTextNamaKelas)
        btnSelesai = findViewById(R.id.buttonSelesai)
        ivBack = findViewById(R.id.iv_back)

        // Set isi EditText
        etNamaKelas.setText(namaKelas)

        // Tombol kembali
        ivBack.setOnClickListener {
            finish()
        }

        // Tombol update
        btnSelesai.setOnClickListener {
            updateKelas()
        }
    }

    private fun updateKelas() {
        val newNama = etNamaKelas.text.toString().trim()

        if (newNama.isEmpty()) {
            Toast.makeText(this, "Nama kelas tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        kelasId?.let { id ->
            db.collection("kelas")
                .document(id)
                .update("nama_kelas", newNama)
                .addOnSuccessListener {
                    Toast.makeText(this, "Kelas berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.e("EditKelas", "Gagal update", e)
                    Toast.makeText(this, "Gagal memperbarui kelas: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
