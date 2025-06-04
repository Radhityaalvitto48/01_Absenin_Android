package com.example.project_pas.Model

data class AbsensiSiswa(
    val nama: String = "",
    var status: String = "",   // Bisa diubah di AbsensiAdapter
    val kelas: String? = null  // Optional, buat display di Rekap
)