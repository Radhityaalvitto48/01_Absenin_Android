package com.example.project_pas.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project_pas.Model.SiswaModel
import com.example.project_pas.R

class AbsensiAdapter(
    private val siswaList: List<SiswaModel>
) : RecyclerView.Adapter<AbsensiAdapter.AbsensiViewHolder>() {

    private val absensiMap = mutableMapOf<String, Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbsensiViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_siswa, parent, false)
        return AbsensiViewHolder(view)
    }

    override fun onBindViewHolder(holder: AbsensiViewHolder, position: Int) {
        val siswa = siswaList[position]
        holder.tvNamaSiswa.text = siswa.nama

        // Cegah efek recyclerview saat scroll
        val isChecked = absensiMap[siswa.id] ?: true
        holder.cbHadir.setOnCheckedChangeListener(null)
        holder.cbHadir.isChecked = isChecked

        // Update status saat checkbox diubah
        holder.cbHadir.setOnCheckedChangeListener { _, checked ->
            absensiMap[siswa.id] = checked
        }
    }

    override fun getItemCount(): Int = siswaList.size

    fun getAbsensiMap(): Map<String, Boolean> = absensiMap

    inner class AbsensiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaSiswa: TextView = itemView.findViewById(R.id.tv_nama_siswa)
        val cbHadir: CheckBox = itemView.findViewById(R.id.cb_hadir)
    }
}

