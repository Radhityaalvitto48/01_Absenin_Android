        package com.example.project_pas.Adapter

        import android.view.LayoutInflater
        import android.view.View
        import android.view.ViewGroup
        import android.widget.ImageView
        import android.widget.PopupMenu
        import android.widget.TextView
        import androidx.recyclerview.widget.RecyclerView
        import com.example.project_pas.Model.SekolahModel
        import com.example.project_pas.R

        class SekolahAdapter(
            private var sekolahList: MutableList<SekolahModel>,
            private val listener: OnSekolahClickListener
        ) : RecyclerView.Adapter<SekolahAdapter.ViewHolder>() {

            inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                val namaSekolah: TextView = itemView.findViewById(R.id.tvNamaSekolah)
                val btnMenu: ImageView = itemView.findViewById(R.id.btnMenu)


                fun bind(sekolah: SekolahModel, position: Int) {
                    namaSekolah.text = sekolah.nama_sekolah

                    btnMenu.setOnClickListener { view ->
                        val popup = PopupMenu(view.context, btnMenu)
                        popup.menuInflater.inflate(R.menu.menu_sekolah, popup.menu)
                        popup.setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.action_edit_sekolah -> {
                                    listener.onEdit(sekolah.id, sekolah.nama_sekolah)
                                    true
                                }
                                R.id.action_delete_sekolah -> {
                                    listener.onDelete(sekolah.id, position)
                                    true
                                }
                                else -> false
                            }
                        }
                        popup.show()
                    }
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_item_sekolah, parent, false)
                return ViewHolder(view)
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.bind(sekolahList[position], position)
            }

            override fun getItemCount(): Int = sekolahList.size

            fun updateData(newList: List<SekolahModel>) {
                sekolahList.clear()
                sekolahList.addAll(newList)
                notifyDataSetChanged()
            }

            // Method untuk remove item
            fun removeItem(position: Int) {
                if (position >= 0 && position < sekolahList.size) {
                    sekolahList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, sekolahList.size)
                }
            }

            interface OnSekolahClickListener {
                fun onEdit(sekolahId: String, namaSekolah: String)
                fun onDelete(sekolahId: String, position: Int)
            }
        }
