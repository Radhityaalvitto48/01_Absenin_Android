package com.example.project_pas

import android.annotation.SuppressLint
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
import com.example.project_pas.Adapter.SekolahAdapter
import com.example.project_pas.Model.SekolahModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class TambahSekolah : AppCompatActivity(), SekolahAdapter.OnSekolahClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sekolahAdapter: SekolahAdapter
    private lateinit var fabMain: FloatingActionButton
    private lateinit var fabTambahSekolah: FloatingActionButton

    private val sekolahList = mutableListOf<SekolahModel>()
    private var isFabOpen = false
    private lateinit var db: FirebaseFirestore

    // Simpan listener untuk bisa di-remove nanti
    private var sekolahListener: com.google.firebase.firestore.ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_sekolah)

        db = FirebaseFirestore.getInstance()

        initViews()
        setupRecyclerView()
        setupFAB()
        loadDataFromFirestore() // Cukup panggil sekali di onCreate


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
        recyclerView = findViewById(R.id.recyclerViewSekolah)
        fabMain = findViewById(R.id.fab_main)
        fabTambahSekolah = findViewById(R.id.fab_tambah_sekolah)

        findViewById<ImageView>(R.id.iv_Back).setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        sekolahAdapter = SekolahAdapter(sekolahList, this)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@TambahSekolah)
            adapter = sekolahAdapter
        }
    }

    private fun setupFAB() {
        fabMain.setOnClickListener {
            toggleFAB()
        }

        fabTambahSekolah.setOnClickListener {
            val intent = Intent(this, IsiSekolah::class.java)
            startActivity(intent)
            toggleFAB()
        }
    }

    private fun toggleFAB() {
        if (isFabOpen) {
            fabTambahSekolah.visibility = View.GONE
            fabMain.setImageResource(R.drawable.ic_add)
        } else {
            fabTambahSekolah.visibility = View.VISIBLE
        }
        isFabOpen = !isFabOpen
    }

    private fun loadDataFromFirestore() {
        // Remove previous listener dulu kalo ada
        sekolahListener?.remove()

        sekolahListener = db.collection("sekolah")
            .orderBy("nama_sekolah", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("TambahSekolah", "Error loading data", exception)
                    Toast.makeText(this, "Error loading data: ${exception.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                sekolahList.clear() // Bersihin list dulu biar gak dobel

                snapshot?.documents?.forEach { document ->
                    val sekolah = SekolahModel(
                        id = document.id,
                        nama_sekolah = document.getString("nama_sekolah") ?: ""
                    )
                    sekolahList.add(sekolah)
                }

                sekolahAdapter.notifyDataSetChanged()
                Log.d("TambahSekolah", "Loaded ${sekolahList.size} sekolah")
            }
    }

    override fun onEdit(SekolahId: String, namaSekolah: String) {
        val intent = Intent(this, EditSekolah::class.java).apply {
            putExtra("SEKOLAH_ID", SekolahId)
            putExtra("NAMA_SEKOLAH", namaSekolah)
            putExtra("IS_EDIT", true)
        }
        startActivity(intent)
    }

    override fun onDelete(SekolahId: String, position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Sekolah")
            .setMessage("Apakah Anda yakin ingin menghapus sekolah ini?")
            .setPositiveButton("Ya") { _, _ ->
                deleteSekolah(SekolahId, position)
            }
            .setNegativeButton("Tidak", null)
            .show()
    }

    private fun deleteSekolah(sekolahId: String, position: Int) {
        db.collection("sekolah")
            .document(sekolahId)
            .delete()
            .addOnSuccessListener {
                Log.d("TambahSekolah", "Sekolah berhasil dihapus")
                Toast.makeText(this, "Sekolah berhasil dihapus", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Log.e("TambahSekolah", "Gagal menghapus sekolah", exception)
                Toast.makeText(this, "Gagal menghapus: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove listener saat activity dihancurin supaya gak memory leak & gak dobel data
        sekolahListener?.remove()
    }

    // Hilangkan override onResume karena gak perlu reload lagi pakai snapshotListener realtime
}