package com.example.project_pas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_pas.Adapter.AbsensiAdapter
import com.example.project_pas.Model.AbsensiRekap
import com.example.project_pas.Model.AbsensiSiswa
import com.example.project_pas.Model.SiswaModel
import com.example.project_pas.databinding.ActivityMenuAbsensiBinding
import com.google.firebase.firestore.FirebaseFirestore

class MenuAbsensi : AppCompatActivity() {

    private lateinit var binding: ActivityMenuAbsensiBinding
    private lateinit var adapter: AbsensiAdapter
    private val siswaList = mutableListOf<SiswaModel>()
    private val db = FirebaseFirestore.getInstance()

    private val selectedSekolah = "SMK X"
    private val selectedKelas = "X RPL 1"
    private val selectedTanggal = "2025-06-04"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuAbsensiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pageprofile = findViewById<ImageView>(R.id.imageView8)
        pageprofile.setOnClickListener {
            intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }

        val pagehome = findViewById<ImageView>(R.id.imageView10)
        pagehome.setOnClickListener {
            intent = Intent(this, LandingPage::class.java)
            startActivity(intent)
        }

        val pageabsen = findViewById<ImageView>(R.id.imageView9)
        pageabsen.setOnClickListener {
            intent = Intent(this, MenuAbsensi::class.java)
            startActivity(intent)
        }

        db.collection("siswa")
            .whereEqualTo("kelas", selectedKelas)
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    val nama = doc.getString("nama") ?: ""
                    siswaList.add(SiswaModel(nama))
                }
                val absensiSiswaList = siswaList.map { AbsensiSiswa(it.nama) }
                adapter = AbsensiAdapter(absensiSiswaList)
                binding.recyclerViewAbsen.layoutManager = LinearLayoutManager(this)
                binding.recyclerViewAbsen.adapter = adapter
            }

        binding.fabMain.setOnClickListener {
            val absensiRekap = AbsensiRekap(
                sekolah = selectedSekolah,
                kelas = selectedKelas,
                tanggal = selectedTanggal,
                siswa = adapter.getUpdatedList()
            )

            db.collection("absensi")
                .add(absensiRekap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Absensi berhasil disimpan", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.e("FIRESTORE", "Gagal simpan: ", e)
                }
        }
    }
}