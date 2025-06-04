package com.example.project_pas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_pas.Adapter.AbsensiRekapAdapter
import com.example.project_pas.Model.AbsensiRekap
import com.example.project_pas.databinding.ActivityMenuAbsensiBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MenuAbsensi : AppCompatActivity() {

    private lateinit var binding: ActivityMenuAbsensiBinding
    private lateinit var adapter: AbsensiRekapAdapter
    private val absensiList = mutableListOf<AbsensiRekap>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuAbsensiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupNavigationButtons()
        loadAbsensiData()

        // FAB click listener - Navigate to DetailAbsensi
        binding.fabMain.setOnClickListener {
            val intent = Intent(this, DetailAbsensi::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh data ketika kembali ke activity ini
        loadAbsensiData()
    }

    private fun setupRecyclerView() {
        adapter = AbsensiRekapAdapter(absensiList) { absensiRekap ->
            // Handle item click - bisa untuk melihat detail atau edit
            showAbsensiDetail(absensiRekap)
        }
        binding.recyclerViewAbsen.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewAbsen.adapter = adapter
    }

    private fun setupNavigationButtons() {
        val pageprofile = findViewById<ImageView>(R.id.imageView8)
        pageprofile.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }

        val pagehome = findViewById<ImageView>(R.id.imageView10)
        pagehome.setOnClickListener {
            val intent = Intent(this, LandingPage::class.java)
            startActivity(intent)
        }

        val pageabsen = findViewById<ImageView>(R.id.imageView9)
        pageabsen.setOnClickListener {
            // Already in MenuAbsensi, do nothing or refresh
            loadAbsensiData()
        }
    }

    private fun loadAbsensiData() {
        // Show loading state
        binding.recyclerViewAbsen.alpha = 0.5f

        db.collection("absensi")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                absensiList.clear()

                for (doc in result) {
                    try {
                        val sekolah = doc.getString("sekolah") ?: ""
                        val kelas = doc.getString("kelas") ?: ""
                        val tanggal = doc.getString("tanggal") ?: ""
                        val totalSiswa = doc.getLong("totalSiswa")?.toInt() ?: 0
                        val totalHadir = doc.getLong("totalHadir")?.toInt() ?: 0
                        val totalTidakHadir = doc.getLong("totalTidakHadir")?.toInt() ?: 0
                        val timestamp = doc.getLong("timestamp") ?: 0L

                        // Ambil data siswa - hanya field yang diperlukan
                        val siswaList = mutableListOf<Map<String, Any>>()
                        val siswaData = doc.get("siswa") as? List<Map<String, Any>>
                        if (siswaData != null) {
                            // Filter hanya field yang diperlukan dari data siswa
                            for (siswa in siswaData) {
                                val filteredSiswa = mapOf(
                                    "nama" to (siswa["nama"] ?: ""),
                                    "isHadir" to (siswa["isHadir"] ?: true)
                                )
                                siswaList.add(filteredSiswa)
                            }
                        }

                        val absensiRekap = AbsensiRekap(
                            id = "", // Tidak menggunakan document ID
                            sekolah = sekolah,
                            kelas = kelas,
                            tanggal = tanggal,
                            totalSiswa = totalSiswa,
                            totalHadir = totalHadir,
                            totalTidakHadir = totalTidakHadir,
                            timestamp = timestamp,
                            siswaList = siswaList
                        )
                        absensiList.add(absensiRekap)
                    } catch (e: Exception) {
                        Log.e("FIRESTORE", "Error parsing document", e)
                    }
                }

                adapter.notifyDataSetChanged()
                binding.recyclerViewAbsen.alpha = 1.0f

                if (absensiList.isEmpty()) {
                    Toast.makeText(this, "Belum ada data absensi", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e("FIRESTORE", "Gagal load absensi: ", e)
                Toast.makeText(this, "Gagal memuat data absensi", Toast.LENGTH_SHORT).show()
                binding.recyclerViewAbsen.alpha = 1.0f
            }
    }

    private fun showAbsensiDetail(absensiRekap: AbsensiRekap) {
        // Tampilkan detail absensi tanpa menggunakan ID dokumen
        val siswaHadirCount = absensiRekap.siswaList.count {
            it["isHadir"] as? Boolean == true
        }
        val siswaTidakHadirCount = absensiRekap.siswaList.size - siswaHadirCount

        val message = """
            Sekolah: ${absensiRekap.sekolah}
            Kelas: ${absensiRekap.kelas}
            Tanggal: ${absensiRekap.tanggal}
            
            Total Siswa: ${absensiRekap.totalSiswa}
            Hadir: ${absensiRekap.totalHadir}
            Tidak Hadir: ${absensiRekap.totalTidakHadir}
            
            Daftar Siswa Tidak Hadir:
            ${getSiswaTidakHadirList(absensiRekap.siswaList)}
        """.trimIndent()

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun getSiswaTidakHadirList(siswaList: List<Map<String, Any>>): String {
        val siswaTidakHadir = siswaList.filter {
            it["isHadir"] as? Boolean == false
        }.map {
            it["nama"] as? String ?: "Unknown"
        }

        return if (siswaTidakHadir.isEmpty()) {
            "Semua siswa hadir"
        } else {
            siswaTidakHadir.joinToString(", ")
        }
    }
}