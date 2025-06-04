package com.example.project_pas.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init
import com.example.project_pas.Model.KelasModel
import com.example.project_pas.R

class KelasAdapter (
    private var kelasList: MutableList<KelasModel>,
    private val listener: OnKelasClickListener,
    private val onItemClick: (KelasModel) -> Unit,
) : RecyclerView.Adapter<KelasAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val namaKelas: TextView = itemView.findViewById(R.id.tvNamaKelas)
        private val jumSiswa: TextView = itemView.findViewById(R.id.tvJumSiswa)
        private val btnMenu: ImageView = itemView.findViewById(R.id.btnMenu)
        val tvNamaKelas: TextView = itemView.findViewById(R.id.tvNamaKelas)
        val tvJumlahSiswa: TextView = itemView.findViewById(R.id.tvJumSiswa)

        fun bind(kelas: KelasModel, position: Int) {
            tvNamaKelas.text = kelas.nama_kelas
            tvJumlahSiswa.text = "${kelas.jumlahSiswa} siswa"

            itemView.setOnClickListener {
                onItemClick(kelas) // Panggil fungsi klik dari luar
            }

            btnMenu.setOnClickListener { view ->
                val popup = PopupMenu(view.context, btnMenu)
                popup.menuInflater.inflate(R.menu.menu_kelas, popup.menu)

                popup.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.action_edit -> {
                            listener.onEdit(kelas.id, kelas.nama_kelas)
                            true
                        }
                        R.id.action_delete -> {
                            listener.onDelete(kelas.id, position)
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
            }
        }

        fun bind(kelas: KelasModel) {
            tvNamaKelas.text = kelas.nama_kelas
            tvJumlahSiswa.text = "${kelas.jumlahSiswa} siswa"

            itemView.setOnClickListener {
                onItemClick(kelas)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_kelas, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val kelas = kelasList[position]
        holder.bind(kelas, position)
    }

    override fun getItemCount(): Int = kelasList.size

    fun updateData(newList: List<KelasModel>) {
        kelasList.clear()
        kelasList.addAll(newList)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        if (position >= 0 && position < kelasList.size) {
            kelasList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, kelasList.size)
        }
    }

    interface OnKelasClickListener {
        fun onEdit(kelasId: String, namaKelas: String)
        fun onDelete(kelasId: String, position: Int)
    }

}
