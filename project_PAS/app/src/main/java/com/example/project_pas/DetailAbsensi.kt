package com.example.project_pas
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.project_pas.Model.SiswaModel
import com.example.project_pas.databinding.ActivityDetailAbsensiBinding
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class DetailAbsensi : AppCompatActivity() {

    private lateinit var binding: ActivityDetailAbsensiBinding
    private lateinit var database: DatabaseReference

    private var selectedSekolah = ""
    private var selectedKelas = ""
    private var selectedTanggal = ""

    private val checkboxMap = mutableMapOf<String, Pair<String, CheckBox>>() // id -> (nama, checkbox)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAbsensiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference

        setupSpinners()
        setupDatePicker()

        binding.btnSelesai.setOnClickListener {
            if (selectedSekolah.isNotEmpty() && selectedKelas.isNotEmpty() && selectedTanggal.isNotEmpty()) {
                simpanAbsensiKeFirebase()
            } else {
                Toast.makeText(this, "Lengkapi sekolah, kelas, dan tanggal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSpinners() {
        val sekolahList = listOf("Pilih Sekolah", "SMK Negeri 1 Subang", "SMK Negeri 2 Subang")
        val kelasList = listOf("Pilih Kelas", "X RPL 1", "X RPL 2", "XI RPL 1")

        binding.spinnerSekolah.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sekolahList)
        binding.spinnerKelas.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, kelasList)

        binding.spinnerSekolah.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                selectedSekolah = if (position != 0) sekolahList[position] else ""
                fetchDataSiswa()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.spinnerKelas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                selectedKelas = if (position != 0) kelasList[position] else ""
                fetchDataSiswa()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupDatePicker() {
        binding.editTextTanggal.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this, { _, y, m, d ->
                val picked = Calendar.getInstance().apply { set(y, m, d) }
                selectedTanggal = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(picked.time)
                binding.editTextTanggal.setText(selectedTanggal)
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun fetchDataSiswa() {
        if (selectedSekolah.isEmpty() || selectedKelas.isEmpty()) return

        val ref = database.child("siswa").child(selectedSekolah).child(selectedKelas)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                checkboxMap.clear()
                binding.layoutDaftarSiswa.removeAllViews()

                for (child in snapshot.children) {
                    val siswa = child.getValue(SiswaModel::class.java)
                    if (siswa != null) {
                        val checkBox = CheckBox(this@DetailAbsensi).apply {
                            text = siswa.nama
                        }
                        binding.layoutDaftarSiswa.addView(checkBox)
                        checkboxMap[siswa.id] = Pair(siswa.nama, checkBox)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DetailAbsensi, "Gagal ambil data siswa", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun simpanAbsensiKeFirebase() {
        val ref = database.child("absensi")
            .child(selectedSekolah)
            .child(selectedKelas)
            .child(selectedTanggal)

        for ((id, pair) in checkboxMap) {
            val (nama, checkbox) = pair
            val status = if (checkbox.isChecked) "Hadir" else "Tidak Hadir"
            ref.child(id).setValue(mapOf("nama" to nama, "status" to status))
        }

        Toast.makeText(this, "Absensi tersimpan!", Toast.LENGTH_SHORT).show()
    }
}
