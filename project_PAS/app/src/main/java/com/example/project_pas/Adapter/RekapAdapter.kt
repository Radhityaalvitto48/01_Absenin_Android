package com.example.project_pas.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_pas.Model.AbsensiSiswa
import com.example.project_pas.databinding.LayoutItemAbsenBinding

class RekapAdapter(private val listRekap: List<AbsensiSiswa>) :
    RecyclerView.Adapter<RekapAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: LayoutItemAbsenBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutItemAbsenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val siswa = listRekap[position]
        holder.binding.txtNama.text = siswa.nama
        holder.binding.tvAttendance.text = "Status: ${siswa.status}" // Ini TextView buat nampilin status
    }

    override fun getItemCount(): Int = listRekap.size
}