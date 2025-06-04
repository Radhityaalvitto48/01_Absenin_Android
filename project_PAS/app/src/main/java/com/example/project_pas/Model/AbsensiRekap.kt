package com.example.project_pas.Model

data class AbsensiRekap(
    val sekolah: String = "",
    val kelas: String = "",
    val tanggal: String = "",
    val siswa: List<AbsensiSiswa> = listOf()
)