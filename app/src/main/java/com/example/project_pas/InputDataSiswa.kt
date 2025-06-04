package com.example.project_pas

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore

class InputDataSiswa : AppCompatActivity() {
    private lateinit var idKelas: String
    private lateinit var namaKelas: String
    private lateinit var etNamaSiswa: EditText
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_data_siswa)

        idKelas = intent.getStringExtra("id_kelas") ?: ""
        namaKelas = intent.getStringExtra("nama_kelas") ?: ""

        findViewById<TextView>(R.id.tvJudulKelas).text = "Input Siswa untuk $namaKelas"
        etNamaSiswa = findViewById(R.id.etNamaSiswa)
        val btnSimpan = findViewById<ImageButton>(R.id.btnSimpanSiswa)

        db = FirebaseFirestore.getInstance()

        btnSimpan.setOnClickListener {
            val namaSiswa = etNamaSiswa.text.toString().trim()
            if (namaSiswa.isNotEmpty()) {
                val siswa = hashMapOf(
                    "nama" to namaSiswa,
                    "kelas_id" to idKelas
                )

                db.collection("siswa")
                    .add(siswa)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Siswa disimpan!", Toast.LENGTH_SHORT).show()
                        etNamaSiswa.text.clear()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Gagal menyimpan siswa", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Nama siswa tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }
}