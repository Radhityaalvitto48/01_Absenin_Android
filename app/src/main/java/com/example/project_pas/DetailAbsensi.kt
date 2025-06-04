import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_pas.Adapter.AbsensiAdapter
import com.example.project_pas.MenuAbsensi
import com.example.project_pas.Model.SiswaModel
import com.example.project_pas.databinding.ActivityDetailAbsensiBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class DetailAbsensi : AppCompatActivity() {

    private lateinit var binding: ActivityDetailAbsensiBinding
    private lateinit var absensiAdapter: AbsensiAdapter
    private val siswaList = mutableListOf<SiswaModel>()
    private val db = FirebaseFirestore.getInstance()
    private val calendar = Calendar.getInstance()
    private val kelasIdMap = mutableMapOf<String, String>()
    private val sekolahIdMap = mutableMapOf<String, String>()


    private var selectedSekolahId = ""
    private var selectedKelasId = ""
    private var selectedDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAbsensiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        loadSekolahData()
        loadKelasData()
        setupDatePicker()
        setupRecyclerView()
    }

    private fun setupUI() {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        selectedDate = dateFormat.format(calendar.time)
        binding.etTanggal.setText(selectedDate)

        binding.spinnerSekolah.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                if (position > 0) {
                    val selectedNamaSekolah = parent?.getItemAtPosition(position) as? String ?: ""
                    selectedSekolahId = sekolahIdMap[selectedNamaSekolah] ?: ""
                } else {
                    selectedSekolahId = ""
                }
                loadSiswaData()
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        })

        binding.spinnerKelas.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                if (position > 0) {
                    val selectedNamaKelas = parent?.getItemAtPosition(position) as? String ?: ""
                    selectedKelasId = kelasIdMap[selectedNamaKelas] ?: ""
                } else {
                    selectedKelasId = ""
                }
                loadSiswaData()
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        })

        binding.fabSimpan.setOnClickListener {
            simpanAbsensi()
        }
    }

    private fun setupDatePicker() {
        binding.etTanggal.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    selectedDate = dateFormat.format(calendar.time)
                    binding.etTanggal.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setupRecyclerView() {
        //absensiAdapter = AbsensiAdapter(this, siswaList)
        binding.recyclerViewSiswa.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewSiswa.adapter = absensiAdapter
    }

    private fun loadSekolahData() {
        db.collection("sekolah")
            .get()
            .addOnSuccessListener { result ->
                val sekolahList = mutableListOf("Pilih Sekolah")
                sekolahIdMap.clear()

                for (doc in result) {
                    val namaSekolah = doc.getString("nama_sekolah") ?: ""
                    val sekolahId = doc.id
                    sekolahList.add(namaSekolah)
                    sekolahIdMap[namaSekolah] = sekolahId
                }

                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sekolahList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerSekolah.adapter = adapter
            }
            .addOnFailureListener { e ->
                Log.e("FIRESTORE", "Error loading sekolah: ", e)
                Toast.makeText(this, "Gagal memuat data sekolah", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadKelasData() {
        db.collection("kelas")
            .get()
            .addOnSuccessListener { result ->
                val kelasList = mutableListOf("Pilih Kelas")
                kelasIdMap.clear()

                for (doc in result) {
                    val namaKelas = doc.getString("nama_kelas") ?: ""
                    val kelasId = doc.id
                    kelasList.add(namaKelas)
                    kelasIdMap[namaKelas] = kelasId
                }

                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, kelasList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerKelas.adapter = adapter
            }
            .addOnFailureListener { e ->
                Log.e("FIRESTORE", "Error loading kelas: ", e)
                Toast.makeText(this, "Gagal memuat data kelas", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadSiswaData() {
        if (selectedSekolahId.isEmpty() || selectedKelasId.isEmpty()) {
            siswaList.clear()
            absensiAdapter.notifyDataSetChanged()
            return
        }

        db.collection("siswa")
            .whereEqualTo("sekolahId", selectedSekolahId)
            .whereEqualTo("kelasId", selectedKelasId)
            .orderBy("nama")
            .get()
            .addOnSuccessListener { result ->
                siswaList.clear()
                for (doc in result) {
                    val siswa = SiswaModel(
                        id = doc.id,
                        nama = doc.getString("nama") ?: "",
                        kelasId = doc.getString("kelasId") ?: "",
                        isHadir = true
                    )
                    siswaList.add(siswa)
                }
                absensiAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("FIRESTORE", "Error loading siswa: ", e)
                Toast.makeText(this, "Gagal memuat data siswa", Toast.LENGTH_SHORT).show()
            }
    }

    private fun simpanAbsensi() {
        if (selectedSekolahId.isEmpty() || selectedKelasId.isEmpty()) {
            Toast.makeText(this, "Harap pilih sekolah dan kelas", Toast.LENGTH_SHORT).show()
            return
        }

        if (siswaList.isEmpty()) {
            Toast.makeText(this, "Tidak ada data siswa", Toast.LENGTH_SHORT).show()
            return
        }

        //val absensiMap = absensiAdapter.absensiMap

        val siswaData = siswaList.map { siswa ->
            mapOf(
                "id" to siswa.id,
                "nama" to siswa.nama,
               // "isHadir" to absensiMap[siswa.id] ?: true
            )
        }

        val totalSiswa = siswaData.size
       // val totalHadir = siswaData.count { it["isHadir"] == true }
       // val totalTidakHadir = totalSiswa - totalHadir

        val sekolahName = binding.spinnerSekolah.selectedItem as? String ?: ""
        val kelasName = binding.spinnerKelas.selectedItem as? String ?: ""

        val absensiData = hashMapOf(
            "sekolah" to sekolahName,
            "kelas" to kelasName,
            "tanggal" to selectedDate,
            "totalSiswa" to totalSiswa,
         //   "totalHadir" to totalHadir,
        //    "totalTidakHadir" to totalTidakHadir,
            "timestamp" to System.currentTimeMillis(),
            "siswa" to siswaData
        )

        db.collection("absensi")
            .add(absensiData)
            .addOnSuccessListener {
                Toast.makeText(this, "Absensi berhasil disimpan", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MenuAbsensi::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Log.w("FIRESTORE", "Error adding absensi", e)
                Toast.makeText(this, "Gagal menyimpan absensi", Toast.LENGTH_SHORT).show()
            }
    }
}