    package com.example.project_pas.Adapter

    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.TextView
    import androidx.recyclerview.widget.RecyclerView
    import com.example.project_pas.Model.AbsensiRekap
    import com.example.project_pas.R
    import java.text.SimpleDateFormat
    import java.util.*

    class AbsensiRekapAdapter(
        private val absensiList: List<AbsensiRekap>,
        private val onItemClick: (AbsensiRekap) -> Unit
    ) : RecyclerView.Adapter<AbsensiRekapAdapter.AbsensiRekapViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbsensiRekapViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_item_absen, parent, false)
            return AbsensiRekapViewHolder(view)
        }

        override fun onBindViewHolder(holder: AbsensiRekapViewHolder, position: Int) {
            val absensiRekap = absensiList[position]
            holder.bind(absensiRekap, onItemClick)
        }

        override fun getItemCount(): Int = absensiList.size

        class AbsensiRekapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val txtNama: TextView = itemView.findViewById(R.id.txtNama)
            private val tvKelas: TextView = itemView.findViewById(R.id.tvKelas)
            private val tvAttendance: TextView = itemView.findViewById(R.id.tvAttendance)

            fun bind(absensiRekap: AbsensiRekap, onItemClick: (AbsensiRekap) -> Unit) {
                // Header - Nama sekolah dan tanggal
                txtNama.text = "${absensiRekap.sekolah} - ${absensiRekap.tanggal}"

                // Kelas
                tvKelas.text = "Kelas ${absensiRekap.kelas}"

                // Informasi kehadiran
                val persentase = if (absensiRekap.totalSiswa > 0) {
                    (absensiRekap.totalHadir.toFloat() / absensiRekap.totalSiswa.toFloat() * 100).toInt()
                } else {
                    0
                }

                val attendanceInfo = """
                    Total Siswa: ${absensiRekap.totalSiswa}
                    Hadir: ${absensiRekap.totalHadir}
                    Tidak Hadir: ${absensiRekap.totalTidakHadir}
                    Persentase Kehadiran: $persentase%
                """.trimIndent()

                tvAttendance.text = attendanceInfo

                // Format waktu jika ada timestamp
                if (absensiRekap.timestamp > 0) {
                    val date = Date(absensiRekap.timestamp)
                    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    val waktu = timeFormat.format(date)
                    txtNama.text = "${absensiRekap.sekolah} - ${absensiRekap.tanggal} (${waktu})"
                }

                // Handle item click
                itemView.setOnClickListener {
                    onItemClick(absensiRekap)
                }
            }
        }
    }