package com.example.project_pas

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class EditSekolah : AppCompatActivity() {

    private lateinit var etNamaSekolah: EditText
    private lateinit var btnSelesai: ImageButton
    private lateinit var ivBack: ImageView
    private lateinit var db: FirebaseFirestore

    private var sekolahId: String? = null
    private var namaSekolah: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_sekolah) // ganti sesuai nama file layout kamu

        db = FirebaseFirestore.getInstance()

        // Ambil data dari intent
        sekolahId = intent.getStringExtra("SEKOLAH_ID")
        namaSekolah = intent.getStringExtra("NAMA_SEKOLAH")

        // Bind view
        etNamaSekolah = findViewById(R.id.editTextNamaSekolah)
        btnSelesai = findViewById(R.id.buttonSelesai)
        ivBack = findViewById(R.id.iv_back)

        etNamaSekolah.setText(namaSekolah)

        ivBack.setOnClickListener {
            finish()
        }

        btnSelesai.setOnClickListener {
            updateSekolah()
        }
    }

    private fun updateSekolah() {
        val newNama = etNamaSekolah.text.toString().trim()

        if (newNama.isEmpty()) {
            Toast.makeText(this, "Nama sekolah tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        sekolahId?.let { id ->
            db.collection("sekolah")
                .document(id)
                .update("nama_sekolah", newNama)
                .addOnSuccessListener {
                    Toast.makeText(this, "Sekolah berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.e("EditSekolah", "Gagal update", e)
                    Toast.makeText(this, "Gagal memperbarui sekolah: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
