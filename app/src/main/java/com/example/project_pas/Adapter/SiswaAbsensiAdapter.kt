package com.example.project_pas.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project_pas.Model.SiswaModel
import com.example.project_pas.R

class SiswaAbsensiAdapter(
    private val siswaList: List<SiswaModel>
) : RecyclerView.Adapter<SiswaAbsensiAdapter.SiswaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SiswaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_siswa, parent, false)
        return SiswaViewHolder(view)
    }

    override fun onBindViewHolder(holder: SiswaViewHolder, position: Int) {
        val siswa = siswaList[position]
        holder.bind(siswa)
    }

    override fun getItemCount(): Int = siswaList.size

    class SiswaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNamaSiswa: TextView = itemView.findViewById(R.id.tv_nama_siswa)
        private val cbHadir: CheckBox = itemView.findViewById(R.id.cb_hadir)

        fun bind(siswa: SiswaModel) {
            tvNamaSiswa.text = siswa.nama
//            cbHadir.isChecked = siswa.isHadir
//
//            // Handle checkbox change
//            cbHadir.setOnCheckedChangeListener { _, isChecked ->
//                siswa.isHadir = isChecked
//            }
        }
    }
}