    package com.example.project_pas.Adapter

    import android.view.LayoutInflater
    import android.view.ViewGroup
    import androidx.recyclerview.widget.RecyclerView
    import com.example.project_pas.Model.AbsensiSiswa
    import com.example.project_pas.R
    import com.example.project_pas.databinding.LayoutItemSiswaBinding// PAKAI YANG INI!

    class AbsensiAdapter(private val listSiswa: List<AbsensiSiswa>) :
        RecyclerView.Adapter<AbsensiAdapter.ViewHolder>() {

        inner class ViewHolder(val binding: LayoutItemSiswaBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = LayoutItemSiswaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val siswa = listSiswa[position]
            holder.binding.txtNama.text = siswa.nama

            // Reset RadioGroup biar gak reuse status sebelumnya
            holder.binding.radioGroup.clearCheck()
            when (siswa.status) {
                "Hadir" -> holder.binding.radioGroup.check(R.id.rbHadir)
                "Izin" -> holder.binding.radioGroup.check(R.id.rbIzin)
                "Sakit" -> holder.binding.radioGroup.check(R.id.rbSakit)
                "Alfa" -> holder.binding.radioGroup.check(R.id.rbAlfa)
            }

            // Update status pas user pilih radio
            holder.binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
                siswa.status = when (checkedId) {
                    R.id.rbHadir -> "Hadir"
                    R.id.rbIzin -> "Izin"
                    R.id.rbSakit -> "Sakit"
                    R.id.rbAlfa -> "Alfa"
                    else -> ""
                }
            }
        }

        override fun getItemCount(): Int = listSiswa.size

        fun getUpdatedList(): List<AbsensiSiswa> = listSiswa
    }