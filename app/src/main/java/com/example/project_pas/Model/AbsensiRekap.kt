package com.example.project_pas.Model

data class AbsensiRekap(
    val id: String = "", // Tidak digunakan untuk identifikasi unik
    val sekolah: String = "",
    val kelas: String = "",
    val tanggal: String = "",
    val totalSiswa: Int = 0,
    val totalHadir: Int = 0,
    val totalTidakHadir: Int = 0,
    val timestamp: Long = 0L,
    val siswaList: List<Map<String, Any>> = emptyList()
) {
    // Method untuk mendapatkan identifikasi unik berdasarkan field
    fun getUniqueKey(): String {
        return "${sekolah}_${kelas}_${tanggal}_${timestamp}"
    }

    // Method untuk mendapatkan daftar siswa hadir
    fun getSiswaHadir(): List<String> {
        return siswaList.filter {
            it["isHadir"] as? Boolean == true
        }.map {
            it["nama"] as? String ?: "Unknown"
        }
    }

    // Method untuk mendapatkan daftar siswa tidak hadir
    fun getSiswaTidakHadir(): List<String> {
        return siswaList.filter {
            it["isHadir"] as? Boolean == false
        }.map {
            it["nama"] as? String ?: "Unknown"
        }
    }

    // Method untuk menghitung persentase kehadiran
    fun getPersentaseKehadiran(): Int {
        return if (totalSiswa > 0) {
            (totalHadir.toFloat() / totalSiswa.toFloat() * 100).toInt()
        } else {
            0
        }
    }
}