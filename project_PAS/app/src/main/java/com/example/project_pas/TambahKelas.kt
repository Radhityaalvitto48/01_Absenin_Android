package com.example.project_pas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_pas.Adapter.KelasAdapter
import com.example.project_pas.Model.KelasModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.WriteBatch

class TambahKelas : AppCompatActivity(), KelasAdapter.OnKelasClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var kelasAdapter: KelasAdapter
    private lateinit var fabMain: FloatingActionButton
    private lateinit var fabTambahKelas: FloatingActionButton

    private val kelasList = mutableListOf<KelasModel>()
    private var isFabOpen = false
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_kelas)

        db = FirebaseFirestore.getInstance()

        initViews()
        setupRecyclerView()
        setupFAB()
        loadDataFromFirestore()


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
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerViewKelas)
        fabMain = findViewById(R.id.fab_main)
        fabTambahKelas = findViewById(R.id.fab_tambah_kelas)

        findViewById<ImageView>(R.id.iv_Back).setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        kelasAdapter = KelasAdapter(kelasList, this) { kelas ->
            val intent = Intent(this, InputDataSiswa::class.java)
            intent.putExtra("id_kelas", kelas.id)
            intent.putExtra("nama_kelas", kelas.nama_kelas)
            startActivity(intent)
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@TambahKelas)
            adapter = kelasAdapter
        }
    }

    private fun setupFAB() {
        fabMain.setOnClickListener { toggleFAB() }
        fabTambahKelas.setOnClickListener {
            startActivity(Intent(this, IsiKelas::class.java))
            toggleFAB()
        }
    }

    private fun toggleFAB() {
        if (isFabOpen) {
            fabTambahKelas.visibility = View.GONE
            fabMain.setImageResource(R.drawable.ic_add)
        } else {
            fabTambahKelas.visibility = View.VISIBLE
        }
        isFabOpen = !isFabOpen
    }

    private fun loadDataFromFirestore() {
        db.collection("kelas")
            .orderBy("nama_kelas", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { kelasSnapshot ->
                val kelasMap = mutableMapOf<String, KelasModel>()
                kelasList.clear()

                for (doc in kelasSnapshot.documents) {
                    val kelas = KelasModel(
                        id = doc.id,
                        nama_kelas = doc.getString("nama_kelas") ?: "",
                        jumlahSiswa = 0
                    )
                    kelasMap[kelas.id] = kelas
                }

                db.collection("siswa")
                    .get()
                    .addOnSuccessListener { siswaSnapshot ->
                        val groupedSiswa = siswaSnapshot.documents.groupBy {
                            it.getString("kelas_id")
                        }

                        kelasMap.forEach { (id, kelas) ->
                            kelas.jumlahSiswa = groupedSiswa[id]?.size ?: 0
                        }

                        kelasList.addAll(kelasMap.values.sortedBy { it.nama_kelas })
                        kelasAdapter.notifyDataSetChanged()
                    }
                    .addOnFailureListener {
                        // Tampilkan data tanpa jumlah siswa
                        kelasList.addAll(kelasMap.values.sortedBy { it.nama_kelas })
                        kelasAdapter.notifyDataSetChanged()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal ambil data kelas", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onEdit(kelasId: String, namaKelas: String) {
        val intent = Intent(this, EditKelas::class.java).apply {
            putExtra("KELAS_ID", kelasId)
            putExtra("NAMA_KELAS", namaKelas)
            putExtra("IS_EDIT", true)
        }
        startActivity(intent)
    }

    override fun onDelete(kelasId: String, position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Kelas")
            .setMessage("Apakah kamu yakin mau hapus kelas ini beserta semua siswanya?")
            .setPositiveButton("Ya") { _, _ ->
                deleteKelasBesertaSiswa(kelasId, position)
            }
            .setNegativeButton("Tidak", null)
            .show()
    }

    private fun deleteKelasBesertaSiswa(kelasId: String, position: Int) {
        val batch: WriteBatch = db.batch()

        // Hapus siswa yang kelas_id = kelasId
        db.collection("siswa")
            .whereEqualTo("kelas_id", kelasId)
            .get()
            .addOnSuccessListener { siswaSnapshot ->
                for (doc in siswaSnapshot.documents) {
                    batch.delete(doc.reference)
                }

                // Hapus dokumen kelas
                val kelasRef = db.collection("kelas").document(kelasId)
                batch.delete(kelasRef)

                // Commit semua penghapusan
                batch.commit()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Kelas & siswa dihapus", Toast.LENGTH_SHORT).show()
                        kelasList.removeAt(position)
                        kelasAdapter.notifyItemRemoved(position)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Gagal hapus data", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal ambil data siswa", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onResume() {
        super.onResume()
        loadDataFromFirestore()
    }
}