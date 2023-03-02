package kr.or.mrhi.cinemastorage.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.or.mrhi.cinemastorage.BuildConfig
import kr.or.mrhi.cinemastorage.data.cinema.Cinema
import kr.or.mrhi.cinemastorage.databinding.AdapterListBinding

class ListAdapter : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<Cinema>() {

        override fun areItemsTheSame(oldItem: Cinema, newItem: Cinema): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cinema, newItem: Cinema): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)

    inner class ListViewHolder(private val binding: AdapterListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cinema: Cinema) {
            Glide.with(itemView.context).load(BuildConfig.TMDB_IMG_URL + cinema.poster)
                .into(binding.ivPoster)

            binding.root.setOnClickListener {
                onItemClickListener?.let {
                    it(cinema)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = AdapterListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    private var onItemClickListener: ((Cinema) -> Unit)? = null

    fun setOnItemClickListener(listener: (Cinema) -> Unit) {
        onItemClickListener = listener
    }
}