package kr.or.mrhi.cinemastorage.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.or.mrhi.cinemastorage.data.Cinema
import kr.or.mrhi.cinemastorage.databinding.AdapterListBinding

class ListAdapter(private val cinemaList: ArrayList<Cinema>) :
    RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    inner class ListViewHolder(private val binding: AdapterListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Cinema) {
            Glide.with(itemView.context).load(data.thumbnail).into(binding.ivThumbnail)
            binding.tvTitle.text = data.title
            binding.tvDirector.text = data.director
            binding.tvActor.text = data.actor
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = AdapterListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(cinemaList[position])
    }

    override fun getItemCount(): Int = cinemaList.size

}